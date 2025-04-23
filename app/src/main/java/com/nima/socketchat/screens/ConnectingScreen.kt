package com.nima.socketchat.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.nima.socketchat.R
import com.nima.socketchat.components.AppButton
import com.nima.socketchat.components.AppIconButton
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.viewmodel.ConnectingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ConnectingScreen(
    navController: NavController,
    viewModel: ConnectingViewModel
) {
    val user = viewModel.user
    var backPressedOnce by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    BackHandler {
        if (backPressedOnce){
            (context as Activity).finish()
        }else{
            backPressedOnce = true
            scope.launch {
                Toast.makeText(context, "Press Again to Exit the app!", Toast.LENGTH_SHORT).show()
                delay(2000)
                backPressedOnce = false
            }
        }
    }

    if (user != null){
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

            Text("Hello, ${user.username}",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    vertical = 8.dp, horizontal = 16.dp
                ),
                color = MaterialTheme.colorScheme.tertiary
            )
            LazyVerticalGrid(columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                ) {
                item {
                    AppIconButton(
                        icon = R.drawable.connect,
                        text = "Connect to Server",
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(Screens.JoinServerScreen.name)
                    }
                }

                item {
                    AppIconButton(
                        icon = R.drawable.host,
                        text = "Host a Server",
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(Screens.HostSetupScreen.name)
                    }
                }

                item{
                    AppIconButton(
                        icon = R.drawable.settings,
                        text = "Settings",
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(Screens.SettingsScreen.name)
                    }
                }

                item{
                    AppIconButton(
                        icon = R.drawable.chat_history,
                        text = "All Chats",
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(Screens.LocalUserListScreen.name)
                    }
                }

                item{
                    AppIconButton(
                        icon = R.drawable.about,
                        text = "About",
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(Screens.AboutScreen.name)
                    }
                }

                item{
                    AppIconButton(
                        icon = R.drawable.donate,
                        text = "Donate to developer",
                        modifier = Modifier.padding(8.dp)
                    ) {
                        navController.navigate(Screens.DonateScreen.name)
                    }
                }
            }
        }
    }
}