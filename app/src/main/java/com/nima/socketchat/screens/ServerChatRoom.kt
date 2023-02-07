package com.nima.socketchat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nima.socketchat.NameDataStore
import com.nima.socketchat.components.ChatBubble
import com.nima.socketchat.model.Message
import com.nima.socketchat.viewmodel.ServerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerChatRoom (
    navController: NavController
){

    val context = LocalContext.current
    val nameDataStore = NameDataStore(context)
    val name = nameDataStore.getUsername.collectAsState(initial = "").value

    val viewModel: ServerViewModel = hiltViewModel()

    val messages = viewModel.readMessages().collectAsState(initial = emptyList()).value

    LaunchedEffect(key1 = viewModel){
        launch {
            viewModel.sendReceiveMessage()
        }
    }

    var messageBody by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp)
        ){
            items(items = messages){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (it.received) Arrangement.Start else Arrangement.End
                ) {
                    ChatBubble(name = it.senderName, text = it.messageBody)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            OutlinedTextField(
                value = messageBody,
                onValueChange = {
                    messageBody = it
                },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = {
                val message = Message(
                    senderName = name!!,
                    messageBody = messageBody,
                    received = false,
                    foreignKey = viewModel.fk!!
                )

                viewModel.sentMessage = message
                messageBody = ""
            },
                enabled = messageBody.isNotBlank()
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        }
    }
}