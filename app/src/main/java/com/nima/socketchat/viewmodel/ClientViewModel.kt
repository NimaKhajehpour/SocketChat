package com.nima.socketchat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.socketchat.model.Message
import com.nima.socketchat.model.Session
import com.nima.socketchat.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: MessageRepository)
    :ViewModel(){

    private var socket: Socket? = null
    private var session: Session? = null

    var ipAddress: String? = null
    var sentMessage: Message? = null
    var receivedMessage: Message? = null
    var fk: UUID? = null

    init {
        viewModelScope.launch {
            session = Session(currentSession = true)
            fk = session?.sessionId
            repository.addSession(session!!)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            session?.currentSession = false
            repository.updateSession(session!!)
        }
    }

    fun readMessages(): Flow<List<Message>> = repository.getSessionMessages(fk!!)

    suspend fun sendReceiveMessage(){
        try {
            withContext(Dispatchers.IO){
                Log.d("LOL", "sendReceiveMessage: BRUH")
                socket = Socket(InetAddress.getByName(ipAddress), 8080)
                Log.d("LOL", "sendReceiveMessage: BRUH2")

            }
            while (true){
                withContext(Dispatchers.IO){
                    val bufferReader =
                        BufferedReader(InputStreamReader(socket?.getInputStream()))
                    val bufferWriter =
                        BufferedWriter(OutputStreamWriter(socket?.getOutputStream()))

                    while (socket?.isConnected == true){
                        Log.d("LOL", "sendReceiveMessage: Connected")
                        if (sentMessage != null){
                            Log.d("LOL", "sendReceiveMessage: Sent")
                            repository.addMessage(sentMessage!!)
                            bufferWriter
                                .write("${sentMessage?.senderName}-${sentMessage?.messageBody}")
                            bufferWriter.newLine()
                            bufferWriter.flush()
                            sentMessage = null
                        }else{
                            bufferWriter
                                .write("")
                            bufferWriter.newLine()
                            bufferWriter.flush()
                        }
                        val received = bufferReader.readLine()
                        if (!received.isNullOrBlank()){
                            Log.d("LOL", "sendReceiveMessage: Received")
                            val (name, body) = received.split("-", limit = 2)
                            receivedMessage = Message(
                                senderName = name,
                                messageBody = body,
                                received = true,
                                foreignKey = fk!!
                            )
                            repository.addMessage(receivedMessage!!)
                        }
                    }
                }
            }
        }catch (e: UnknownHostException) {
            e.printStackTrace()
            Log.d("LOL", "sendReceiveMessage: BRUH3")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("LOL", "sendReceiveMessage: BRUH4")
        } catch (e: ConnectException) {
            e.printStackTrace()
            Log.d("LOL", "sendReceiveMessage: BRUH5")
        }
    }
}