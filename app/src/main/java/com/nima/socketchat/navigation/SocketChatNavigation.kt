package com.nima.socketchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.socketchat.screens.*

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
        composable(Screens.MakeClientScreen.name){
            MakeClientScreen(navController = navController)
        }
        composable(Screens.ClientChatRoom.name+"/{ip}",
            arguments = listOf(
                navArgument(name = "ip"){type = NavType.StringType}
            )
        ){
            ClientChatRoom(navController = navController, ipAddress = it.arguments?.getString("ip"))
        }

        composable(Screens.SessionScreen.name+"/{id}",
            arguments = listOf(
                navArgument(name = "id"){type = NavType.StringType}
            )
        ){
            SessionScreen(navController = navController, id = it.arguments?.getString("id"))
        }
    }
}