package com.nima.socketchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.socketchat.screens.HomeScreen
import com.nima.socketchat.screens.MakeServerScreen
import com.nima.socketchat.screens.ServerChatRoom
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

        composable(Screens.MakeServerScreen.name){
            MakeServerScreen(navController = navController)
        }
        composable(Screens.ServerChatRoom.name){
            ServerChatRoom(navController = navController)
        }
    }
}