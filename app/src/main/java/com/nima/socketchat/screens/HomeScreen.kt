package com.nima.socketchat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.NameDataStore
import com.nima.socketchat.navigation.Screens
import kotlinx.coroutines.launch

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

    when(username){
        "" -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "You need an username. Lets select one.",
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
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Welcome, $username",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )

                Button(onClick = {
                    // start server
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(text = "Start a server")
                }
                Button(onClick = {
                    // join server
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(text = "Join a server")
                }
                Button(onClick = {
                    // Chat history
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(text = "Chat history")
                }
                Button(onClick = {
                    // Settings
                    navController.navigate(Screens.SettingScreen.name)
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    shape = RoundedCornerShape(5.dp),
                ) {
                    Text(text = "Settings")
                }
            }
        }
    }
}