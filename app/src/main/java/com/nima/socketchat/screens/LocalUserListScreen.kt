package com.nima.socketchat.screens

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
import com.nima.socketchat.viewmodel.LocalUserListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LocalUserListScreen(
    navController: NavController,
    viewModel: LocalUserListViewModel
) {

    val chatUsers = viewModel.getAllUsers().collectAsState(emptyList()).value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var backPressed by remember {
        mutableStateOf(false)
    }

    LazyColumn (
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        items(items = chatUsers,
            key = {
                it.uuid
            }
        ){
            UserListItem(
                username = it.username,
                online = false,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                navController.navigate(Screens.LocalUserChatScreen.name+"/${it.uuid}/${it.username}")
            }
        }
    }
}