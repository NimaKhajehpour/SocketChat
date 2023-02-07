package com.nima.socketchat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeClientScreen (
    navController: NavController
){

    var address by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Server Address:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 4.dp)
        )

        OutlinedTextField(value = address,
            onValueChange ={
                address = it
            },
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 4.dp, bottom = 4.dp)
        )

        OutlinedButton(onClick = {
            // go to Client chat room
            navController.navigate(Screens.ClientChatRoom.name+"/${address.trim()}")
        },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            enabled = address.isNotBlank()
        ) {
            Text(text = "Join Server")
        }

        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )

            Text(text = "How to:"+
                    "\n- Make sure you are connected to the same network as server."+
                    "\n- Enter the server address in the field above" +
                    "\n- Make sure you enter the address correctly" +
                    "\n- Wait for the server to start first" +
                    "\n- Have fun!",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
    }
}