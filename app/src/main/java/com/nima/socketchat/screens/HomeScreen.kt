package com.nima.socketchat.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nima.socketchat.NameDataStore
import com.nima.socketchat.R
import com.nima.socketchat.components.ChatHistoryItem
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val nameDataStore = NameDataStore(context)

    val username = nameDataStore.getUsername.collectAsState(initial = "").value

    var usernameInput by remember (username) {
        mutableStateOf(username)
    }

    var expandFab by remember {
        mutableStateOf(false)
    }

    val viewModel: HomeViewModel = hiltViewModel()

    val sessions = viewModel.getSessions().collectAsState(initial = emptyList()).value

    when(username){
        "" -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "You need an username:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )

                OutlinedTextField(value = usernameInput!!,
                    onValueChange = {
                        usernameInput = it
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    label = {
                        Text(text = "Username")
                    }
                )

                OutlinedButton(onClick = {
                    scope.launch {
                        nameDataStore.saveUsername(usernameInput!!.trim())
                    }
                },
                    enabled = !usernameInput.isNullOrBlank(),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(text = "Continue")
                }
            }
        }else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
            ){
                if (sessions.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            tonalElevation = 2.dp,
                            shadowElevation = 16.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "Hello, $username",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    navController.navigate(Screens.SettingScreen.name)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
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
                                text = "No message history yet",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Light,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            tonalElevation = 2.dp,
                            shadowElevation = 16.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 2.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "Hello, $username",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    navController.navigate(Screens.SettingScreen.name)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            itemsIndexed(items = sessions) { index, item ->
                                ChatHistoryItem(index = index, id = item.sessionId) {
                                    navController.navigate(Screens.SessionScreen.name + "/${it.toString()}")
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.BottomEnd),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {

                    AnimatedVisibility(visible = expandFab) {
                        Column(
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(end = 5.dp)
                        ) {
                            SmallFloatingActionButton(onClick = {
                                navController.navigate(Screens.MakeClientScreen.name)
                            }) {
                                Text(text = "Join a Session",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            SmallFloatingActionButton(onClick = {
                                navController.navigate(Screens.MakeServerScreen.name)
                            }) {
                                Text(text = "Start a Session",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                    FloatingActionButton(onClick = {
                        expandFab = !expandFab
                    }) {
                        Icon(imageVector = if (expandFab) Icons.Default.Close else Icons.Default.Menu,
                            contentDescription = null)
                    }
                }
            }
        }
    }
}