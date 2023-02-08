package com.nima.socketchat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nima.socketchat.R
import com.nima.socketchat.components.ChatBubble
import com.nima.socketchat.viewmodel.SessionViewModel
import java.util.*

@Composable
fun SessionScreen(
    navController: NavController,
    id: String?
) {

    val viewModel: SessionViewModel = hiltViewModel()

    var showDialog by remember {
        mutableStateOf(false)
    }

    val messages =
        viewModel.getSessionMessage(UUID.fromString(id)).collectAsState(initial = emptyList()).value

    Box(
        modifier = Modifier.fillMaxSize(),
    ){

        if (messages.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_message_24),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.padding(end = 5.dp)
                    )

                    Text(
                        text = "No messages yet",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(4.dp)
            ) {
                items(items = messages) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (it.received) Arrangement.Start else Arrangement.End
                    ) {
                        ChatBubble(name = it.senderName, text = it.messageBody)
                    }
                }
            }
        }

        SmallFloatingActionButton(onClick = {
            showDialog = true
        },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }

        if (showDialog){
            AlertDialog(onDismissRequest = {
                showDialog = false
            },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteSession(UUID.fromString(id))
                        viewModel.getSessionMessage(UUID.fromString(id))
                        showDialog = false
                        navController.popBackStack()
                    }) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text(text = "Cancel")
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                },
                title = {
                    Text(text = "Delete This Session?")
                },
                text = {
                    Text(text = "You are about to delete this session and all of its messages.\nAre you sure you want to do this?")
                }
            )
        }
    }
}