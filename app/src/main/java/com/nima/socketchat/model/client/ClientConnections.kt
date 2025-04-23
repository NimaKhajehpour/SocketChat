package com.nima.socketchat.model.client

import com.nima.socketchat.model.user.LocalUser
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket
import java.util.UUID

data class ClientConnection(
    val socket: Socket,
    val user: LocalUser,
    var lastHeartbeat: Long = System.currentTimeMillis()
)

