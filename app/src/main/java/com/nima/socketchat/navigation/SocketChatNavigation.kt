package com.nima.socketchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.socketchat.screens.*
import com.nima.socketchat.utils.UserIdentityManager
import com.nima.socketchat.viewmodel.ServerUsersListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SocketChatNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.LauncherScreen.name){
        composable(Screens.LauncherScreen.name) {
            LauncherScreen(navController = navController, userIdentityManager = koinInject())
        }
        composable(Screens.UserIdentityScreen.name){
            UserIdentityScreen(navController = navController, koinViewModel())
        }
        composable(Screens.ConnectingScreen.name){
            ConnectingScreen(navController = navController, koinViewModel())
        }
        composable(Screens.HostSetupScreen.name){
            HostSetupScreen(navController = navController, koinViewModel())
        }
        composable(Screens.JoinServerScreen.name){
            JoinServerScreen(navController, koinViewModel())
        }
        composable(Screens.ClientUsersListScreen.name){
            ClientUsersListScreen(navController, koinViewModel())
        }
        composable(Screens.ServerUsersListScreen.name){
            ServerUsersListScreen(navController, koinViewModel())
        }
        composable(Screens.ClientUserChatScreen.name + "/{id}/{username}",
            arguments = listOf(navArgument("id"){NavType.StringType}, navArgument("username"){NavType.StringType})
            ) {
            ClientUserChatScreen(navController, koinViewModel(), it.arguments?.getString("id"), it.arguments?.getString("username"))
        }
        composable(Screens.ServerUserChatScreen.name + "/{id}/{username}",
            arguments = listOf(navArgument("id"){NavType.StringType},navArgument("username"){NavType.StringType})
        ) {
            ServerUserChatScreen(navController, koinViewModel(), it.arguments?.getString("id"), it.arguments?.getString("username"))
        }
        composable(Screens.LocalUserChatScreen.name + "/{id}/{username}",
            arguments = listOf(navArgument("id"){NavType.StringType}, navArgument("username"){NavType.StringType})
        ) {
            LocalUserChatScreen(navController, koinViewModel(), it.arguments?.getString("id"), it.arguments?.getString("username"))
        }
        composable(Screens.LocalUserListScreen.name){
            LocalUserListScreen(navController, koinViewModel())
        }
        composable(Screens.SettingsScreen.name){
            SettingsScreen(navController)
        }
        composable(Screens.AboutScreen.name){
            AboutScreen(navController)
        }
        composable(Screens.DonateScreen.name){
            DonateScreen(navController)
        }
    }
}