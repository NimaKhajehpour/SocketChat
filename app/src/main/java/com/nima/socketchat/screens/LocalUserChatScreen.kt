package com.nima.socketchat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.ChatTextSizeDataStore
import com.nima.socketchat.RoundedCornerDataStore
import com.nima.socketchat.components.ChatMessageBubble
import com.nima.socketchat.model.message.ChatMessage
import com.nima.socketchat.model.user.LocalUser
import com.nima.socketchat.viewmodel.LocalUserChatViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalUserChatScreen(
    navController: NavController,
    viewModel: LocalUserChatViewModel,
    chatId: String?,
    username: String?
) {
    val user = viewModel.user
    val context = LocalContext.current

    val chatTextSizeDataStore = ChatTextSizeDataStore(context)
    val chatTextSize = chatTextSizeDataStore.getTextSize.collectAsState(9f)

    val roundedCornerDataStore = RoundedCornerDataStore(context)
    val roundedCornerValue = roundedCornerDataStore.getDp.collectAsState(0f)

    val scope = rememberCoroutineScope()

    val userChat = viewModel.getUserMessages(chatId!!).collectAsState(emptyList()).value

    if (user != null){
        Scaffold (
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(username!!)
                    },
                )
            },
        ){ padding ->
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = padding
            ) {
                items(items = userChat) {
                    ChatMessageBubble(
                        text = it.content!!,
                        fontSize = chatTextSize.value!!,
                        cornerRadius = roundedCornerValue.value!!,
                        sentMessage = it.senderId == user.uuid.toString()
                    )
                }
            }
        }
    }
}