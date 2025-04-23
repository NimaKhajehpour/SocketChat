package com.nima.socketchat.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.components.AppButton
import com.nima.socketchat.components.AppTextField
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.viewmodel.JoinServerUiState
import com.nima.socketchat.viewmodel.JoinServerViewModel
import kotlinx.coroutines.launch

@Composable
fun JoinServerScreen(
    navController: NavController,
    viewModel: JoinServerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val scope = rememberCoroutineScope()

    var ip by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Socket Chat",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 16.dp, horizontal = 16.dp
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(60.dp))

        Text("Join a Server",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 8.dp, horizontal = 16.dp
            ),
            color = MaterialTheme.colorScheme.tertiary
        )

        Text(
            text = "Before Joining a server, make sure you have the right setup:" +
                    "\n1- You and your host both are in the same network." +
                    "\n2- Ask server host to share their server ip to you in order to connect." +
                    "\n3- Make sure to ask for the password too, some servers may need password to connect." +
                    "\n4- Have fun :)",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(
                    vertical = 8.dp, horizontal = 16.dp
                )
                .fillMaxWidth()
        )

        AppTextField (
            value = ip,
            onValueChange = { ip = it },
            label = "Server IP" ,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            label = "Server Password",
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        AppButton(
            onClick = {
                Log.d("LOL", "JoinServerScreen: Clicked")
                scope.launch {
                    viewModel.connectToServer(ip, password)
                } },
            enabled = uiState !is JoinServerUiState.Loading,
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text("Connect")
            }
        )

        Spacer(Modifier.height(16.dp))

        when (uiState) {
            is JoinServerUiState.Loading -> CircularProgressIndicator()
            is JoinServerUiState.Error -> {
                Log.d("LOL", "JoinServerScreen: Failed")
                Text(
                    text = (uiState as JoinServerUiState.Error).message,
                    color = Color.Red)
            }
            is JoinServerUiState.Success -> {
                Log.d("LOL", "JoinServerScreen: Succeeded")
                // Trigger navigation
                navController.navigate(Screens.ClientUsersListScreen.name){
                    popUpTo(Screens.ClientUsersListScreen.name){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            else -> {}
        }
    }
}