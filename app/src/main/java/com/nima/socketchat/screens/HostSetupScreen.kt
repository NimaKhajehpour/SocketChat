package com.nima.socketchat.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.components.AppButton
import com.nima.socketchat.components.AppTextField
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.viewmodel.HostSetupViewModel

@Composable
fun HostSetupScreen(
    navController: NavController,
    viewModel: HostSetupViewModel
) {
    val password = viewModel.password
    val ip = viewModel.serverIp
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth(),
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

        Text("Start a Server",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 8.dp, horizontal = 16.dp
            ),
            color = MaterialTheme.colorScheme.tertiary
        )

        Text(
            text = "Before running a server, make sure you have the right setup:" +
                    "\n1- you are either connected to a wifi or sharing your network to others." +
                    "\n2- you are not using any vpn and no proxy server is turned on." +
                    "\n3- if vpn or proxy server is set, make sure to turn them off first and restart the app." +
                    "\n4- share the ip that is given to you to whomever is going to connect to your server." +
                    "\n5- you can put a password for your server if you see a need, make sure to share the password to clients." +
                    "\n6- have fun :)",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(
                vertical = 8.dp, horizontal = 16.dp
            )
                .fillMaxWidth()
        )

        Text("Your Local IP Address: $ip", style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 32.dp)
            )


        AppTextField (
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp),
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label ="Server Password",
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp),
            content = {
                Text("Start Server")
            }
        ) {
            viewModel.startServer(
                onStarted = {
                    navController.navigate(Screens.ServerUsersListScreen.name)
                },
                onError = {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}