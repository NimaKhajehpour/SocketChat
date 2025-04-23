package com.nima.socketchat.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.nima.socketchat.components.AppButton
import com.nima.socketchat.components.AppTextField
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.utils.UserIdentityManager
import com.nima.socketchat.viewmodel.UserIdentityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserIdentityScreen(
    navController: NavController,
    viewModel: UserIdentityViewModel
) {

    var username by remember {
        mutableStateOf("")
    }
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

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text("Welcome to Socket Chat",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 8.dp, horizontal = 16.dp
            ),
            color = MaterialTheme.colorScheme.primary
            )

        Spacer(Modifier.height(60.dp))

        Text("Profile setup",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 8.dp, horizontal = 16.dp
            ),
            color = MaterialTheme.colorScheme.tertiary
            )

        Text(
            text = "To be able to use the application, you have to set a username for yourself. This username will be shown to others" +
                    " when you connect to a server.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                vertical = 8.dp, horizontal = 16.dp
            )
                .fillMaxWidth()
        )

        AppTextField(
            value = username,
            label = "Username",
            singleLine = true,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 32.dp)
                .fillMaxWidth()
        ) {
            username = it
        }

        AppButton(
            content = {
                Text("Save")
            },
            enabled = username.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 32.dp)
        ) {
            scope.launch {
                viewModel.saveUsername(username) {
                        navController.navigate(Screens.ConnectingScreen.name) {
                            launchSingleTop = true
                            popUpTo(
                                Screens.ConnectingScreen.name
                            ) {inclusive = true}
                        }
                }
            }
        }
    }
}