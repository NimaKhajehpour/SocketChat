package com.nima.socketchat.utils

import android.util.Log
import com.nima.socketchat.model.client.ClientConnection
import com.nima.socketchat.model.message.ChatMessage
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import java.util.UUID

class ServerSocketManager(private val messageRepository: MessageRepository) {
    private var coroutineScope: CoroutineScope? = null
    private var serverSocket: ServerSocket? = null
    private val clientSockets = mutableMapOf<UUID, ClientConnection>()

    private val connectedUsers = mutableMapOf<UUID, LocalUser>()

    private val _connectedUserFlow = MutableStateFlow<List<LocalUser>>(emptyList())
    val connectedUserFlow: StateFlow<List<LocalUser>> = _connectedUserFlow

    private var isRunning = false
    private lateinit var serverPassword: String
    private lateinit var hostUser: LocalUser

    suspend fun startServer(password: String, hostUser: LocalUser, scope: CoroutineScope) {
        if (isRunning) return

        this.serverPassword = password
        this.hostUser = hostUser
        this.coroutineScope = scope

        serverSocket = withContext(Dispatchers.IO) {
            ServerSocket(SERVER_PORT)
        }

        isRunning = true

        coroutineScope!!.launch(Dispatchers.IO) {
            Log.d("ServerSocketManager", "Server loop started")
            try {
                while (isRunning) {
                    Log.d("ServerSocketManager", "Waiting for client to connect...")
                    val clientSocket = serverSocket?.accept()
                    Log.d("ServerSocketManager", "Client connected: ${clientSocket?.inetAddress}")
                    updateUserList()
                    clientSocket?.let {
                        handleClientConnection(it)
                    }
                }
            } catch (e: Exception) {
                Log.e("ServerSocketManager", "Server error: ${e.message}", e)
            }
        }
        coroutineScope?.launch(Dispatchers.IO) {
            while (isRunning) {
                delay(10_000)
                val now = System.currentTimeMillis()
                val toRemove = clientSockets.filterValues {
                    now - it.lastHeartbeat > 30_000
                }
                toRemove.forEach { (uuid, connection) ->
                    connection.socket.close()
                    clientSockets.remove(uuid)
                    updateUserList()
                    Log.d("ServerSocketManager", "Client ${uuid} removed due to timeout")
                    // Notify UI/clients that this user disconnected
                }
            }
        }

    }


    private fun handleClientConnection(socket: Socket) {
        coroutineScope?.launch(Dispatchers.IO) {
            var clientUser: LocalUser? = null
            try {
                val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                val output = PrintWriter(socket.getOutputStream(), true)

                // Step 1: Password handshake
                Log.d("ServerSocketManager", "Waiting for password...")
                val clientPassword = input.readLine()
                Log.d("ServerSocketManager", "Received password: $clientPassword")
                if (clientPassword != serverPassword) {
                    output.println("ERROR:Invalid password")
                    socket.close()
                    return@launch
                }

                output.println("OK:PasswordValid")

                // Step 2: Receive user info
                val userJson = input.readLine()
                if (userJson == null) {
                    Log.w("ServerSocketManager", "Client disconnected before sending user info")
                    return@launch
                }

                clientUser = parseUserFromJson(userJson)

                clientSockets[clientUser.uuid] = ClientConnection(socket, clientUser)
                connectedUsers[clientUser.uuid] = clientUser

                updateUserList()
                broadcastUserList()

                output.println("OK:Connected")

                // Message loop
                while (true) {
                    val msg = input.readLine() ?: break
                    if (msg == "HEARTBEAT") {
                        clientSockets[clientUser.uuid]?.lastHeartbeat = System.currentTimeMillis()
                        continue
                    }
                    // Handle other messages here
                    if (msg.startsWith("MESSAGE:")) {
                        val messageJson = msg.removePrefix("MESSAGE:")
                        val message = Json.decodeFromString<ChatMessage>(messageJson)

                        // Save in server DB if receiver is host
                        if (message.receiverId == hostUser.uuid.toString()) {
                            // Save message to Room
                            val trueMessage = message.copy(chatId = message.senderId)
                            messageRepository.saveMessage(trueMessage)
                            continue
                        }

                        // Forward to receiver
                        sendMessage(message)
                    }
                }

            } catch (e: Exception) {
                Log.e("ServerSocketManager", "Error handling client", e)
            } finally {
                clientUser?.let { user ->
                    connectedUsers.remove(user.uuid)
                    clientSockets.remove(user.uuid)
                    updateUserList()
                    broadcastUserList()
                }
                socket.close()
            }
        }
    }

    suspend fun sendMessage(message: ChatMessage) {
        withContext(Dispatchers.IO){
            var json: String = ""
            val receiverId = UUID.fromString(message.receiverId)
            val senderId = UUID.fromString(message.senderId)
            if (senderId == hostUser.uuid) {
                messageRepository.saveMessage(message)
                json = Json.encodeToString(message.copy(contactName = message.username, username = message.contactName))
            }else{
                json = Json.encodeToString(message)
            }


            if (receiverId == hostUser.uuid) {
                // Message is for the host themselves

            } else {
                // Send to connected client
                val client = clientSockets[receiverId]
                if (client != null) {
                    Log.d("Server", "sending")
                    try {
                        val writer = PrintWriter(client.socket.getOutputStream(), true)
                        writer.println("MESSAGE:$json")
                        Log.d("Server", "Sent message to ${receiverId}")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // TODO: Add to queue for retry later
                    }
                } else {
                    // Client is offline, queue for retry
                    Log.d("Server", "Receiver $receiverId is offline. Queuing message.")
                    // TODO: Save to unsent queue (Room or in-memory)
                }
            }
        }
    }

    private fun broadcastUserList() {
        val allUsers = mutableListOf<LocalUser>()

        // Add host
        allUsers.add(hostUser)

        // Add clients
        allUsers.addAll(connectedUsers.values)

        val userListJson = allUsers.joinToString("#") { "${it.uuid}|${it.username}" }

        clientSockets.values.forEach { connection ->
            try {
                val writer = PrintWriter(connection.socket.getOutputStream(), true)
                writer.println("USERS:$userListJson")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun stopServer() {
        isRunning = false
        coroutineScope?.launch(Dispatchers.IO) {
            clientSockets.values.forEach { it.socket.close() }
            clientSockets.clear()
            serverSocket?.close()
        }
    }

    fun getLocalIpAddress(): String {
        return try {
            NetworkInterface.getNetworkInterfaces().toList()
                .flatMap { it.inetAddresses.toList() }
                .firstOrNull { !it.isLoopbackAddress && it is Inet4Address }
                ?.hostAddress ?: "Unavailable"
        } catch (e: Exception) {
            "Unavailable"
        }
    }

    private fun updateUserList() {
        val users = mutableListOf<LocalUser>()

        // Add the host user manually
        users.add(LocalUser(hostUser.uuid, hostUser.username))

        // Add all connected clients
        users += clientSockets.map { (uuid, conn) ->
            LocalUser(uuid, conn.user.username)
        }

        _connectedUserFlow.value = users
    }


    private fun parseUserFromJson(json: String): LocalUser {
        // Stub – use real parsing method depending on your JSON lib
        val parts = json.split("|")
        return LocalUser(uuid = UUID.fromString(parts[0]), username = parts[1])
    }

    companion object {
        const val SERVER_PORT = 8080
    }
}

