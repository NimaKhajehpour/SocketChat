package com.nima.socketchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nima.socketchat.screens.HomeScreen
import com.nima.socketchat.screens.SettingScreen

@Composable
fun SocketChatNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name){
        composable(Screens.HomeScreen.name){
            HomeScreen(navController = navController)
        }

        composable(Screens.SettingScreen.name){
            SettingScreen(navController = navController)
        }
    }
}