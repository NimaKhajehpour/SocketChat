package com.nima.socketchat.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.utils.UserIdentityManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LauncherScreen(
    navController: NavController,
    userIdentityManager: UserIdentityManager
    ) {

    LaunchedEffect(Unit) {
        if (userIdentityManager.isUserInitialized()) {
            navController.navigate(Screens.ConnectingScreen.name)
            {
                launchSingleTop = true
                popUpTo(
                    Screens.ConnectingScreen.name
                ) {inclusive = true}
            }
        }else{
           navController.navigate(Screens.UserIdentityScreen.name){
               launchSingleTop = true
               popUpTo(
                   Screens.UserIdentityScreen.name
               ) {inclusive = true}
           }
        }
    }
}