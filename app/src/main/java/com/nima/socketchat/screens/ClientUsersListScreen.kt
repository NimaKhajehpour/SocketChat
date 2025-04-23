package com.nima.socketchat.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.R
import com.nima.socketchat.components.UserListItem
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.viewmodel.ClientUsersListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClientUsersListScreen(
    navController: NavController,
    viewModel: ClientUsersListViewModel
) {

    val users by viewModel.connectedUsers.collectAsState()
    val chatUsers = viewModel.getAllUsers().collectAsState(emptyList()).value
    val user = viewModel.user
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var backPressed by remember {
        mutableStateOf(false)
    }

    BackHandler {
        scope.launch{
            if (backPressed) {
                viewModel.disconnect()
                navController.navigate(Screens.ConnectingScreen.name) {
                    popUpTo(Screens.ConnectingScreen.name) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            } else {
                backPressed = true
                Toast.makeText(context, "Press Again to Exit Server", Toast.LENGTH_SHORT).show()
                delay(2000)
                backPressed = false
            }
        }
    }

    if (user != null){
        Scaffold (
            modifier = Modifier.fillMaxWidth(),
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.SettingsScreen.name)
                            },
                        ) {
                            Icon(painter = painterResource(R.drawable.settings), null,
                                )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.DonateScreen.name)
                            },
                        ) {
                            Icon(painter = painterResource(R.drawable.donate), null,
                            )
                        }
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.AboutScreen.name)
                            },
                        ) {
                            Icon(painter = painterResource(R.drawable.about), null,
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton (
                            onClick = {
                                viewModel.disconnect()
                                navController.navigate(Screens.ConnectingScreen.name) {
                                    popUpTo(Screens.ConnectingScreen.name) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, null,
                            )
                        }
                    }
                )
            }
        ){
            LazyColumn (
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                items(items = (users+chatUsers).distinctBy { it.uuid }.filter { it.uuid != user.uuid },
                    key = {
                        it.uuid
                    }
                ){
                    UserListItem(
                        username = it.username,
                        online = it in users,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        navController.navigate(Screens.ClientUserChatScreen.name+"/${it.uuid}/${it.username}")
                    }
                }
            }
        }
    }

}