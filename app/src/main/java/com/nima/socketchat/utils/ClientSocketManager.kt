package com.nima.socketchat.utils

import android.util.Log
import com.nima.socketchat.model.message.ChatMessage
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.UUID

class ClientSocketManager(private val messageRepository: MessageRepository) {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null

    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages: SharedFlow<String> = _incomingMessages
    private val _userListFlow = MutableStateFlow<List<LocalUser>>(emptyList())
    val userListFlow: StateFlow<List<LocalUser>> = _userListFlow

    private var isConnected = false

    suspend fun connectToServer(ip: String, password: String, user: LocalUser): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket = Socket(ip, SERVER_PORT)
                Log.d("ClientSocketManager", "Connection established.")
                writer = PrintWriter(socket!!.getOutputStream(), true)
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                Log.d("ClientSocketManager", "buffers made")


                // Step 1: Send password
                writer?.println(password)
                writer?.flush()

                Log.d("ClientSocketManager", "password sent")
                // Step 2: Wait for password confirmation
                val serverResponse = reader?.readLine()
                if (serverResponse?.startsWith("ERROR") == true) {
                    disconnect()
                    return@withContext false
                }
                if (!serverResponse?.startsWith("OK")!!) {
                    Log.e("ClientSocketManager", "Unexpected response: $serverResponse")
                    disconnect()
                    return@withContext false
                }
                Log.d("ClientSocketManager", "password correct")

                // Step 3: Send user info (we'll use simple delimited format for now)
                val userPayload = "${user.uuid}|${user.username}"
                writer?.println(userPayload)
                writer?.flush()
                Log.d("ClientSocketManager", "user sent")
                isConnected = true

                CoroutineScope(Dispatchers.IO).launch {
                    while (isConnected) {
                        delay(10_000)
                        sendMessage("HEARTBEAT")
                    }
                }


                // Step 4: Start listening for incoming messages
                Log.d("ClientSocketManager", "before listen to message")
                CoroutineScope(Dispatchers.IO).launch {
                    listenForMessages()
                }
                Log.d("ClientSocketManager", "Returning true to ViewModel")
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ClientSocketManager", "Failed to connect to server", e)
                disconnect()
                return@withContext false
            }
        }
    }

    private fun listenForMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                while (isConnected) {
                    val msg = reader?.readLine() ?: break
                    if (msg.startsWith("USERS:")) {
                        val userListRaw = msg.removePrefix("USERS:")
                        val users = userListRaw.split("#").mapNotNull {
                            val parts = it.split("|")
                            if (parts.size == 2) LocalUser(UUID.fromString(parts[0]), parts[1]) else null
                        }
                        _userListFlow.emit(users)
                    }else if (msg.startsWith("MESSAGE:")){
                        val messageJson = msg.removePrefix("MESSAGE:")
                        val message = Json.decodeFromString<ChatMessage>(messageJson)
                        val trueMessage = message.copy(chatId = message.senderId)
                        messageRepository.saveMessage(trueMessage)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                disconnect()
            }
        }
    }

    fun sendMessage(message: String) {
        writer?.println(message)
    }
    suspend fun sendMessage(message: ChatMessage) {
        withContext(Dispatchers.IO) {
            try {
                val json = Json.encodeToString(ChatMessage.serializer(), message.copy(contactName = message.username, username = message.contactName))
                val writer = PrintWriter(socket!!.getOutputStream(), true)
                writer.println("MESSAGE:$json")
                messageRepository.saveMessage(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun disconnect() {
        isConnected = false
        try {
            writer?.close()
            reader?.close()
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val SERVER_PORT = 8080
    }
}

