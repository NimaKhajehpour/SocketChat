package com.nima.socketchat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.navigation.Screens
import com.nima.socketchat.utils.Constants

@Composable
fun MakeServerScreen (
    navController: NavController
){

    val context = LocalContext.current

    val localIP by remember {
        mutableStateOf(Constants.getLocalIPAddress(context))
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Local IP Address:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 4.dp)
        )
        
        Text(text = localIP!!,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 4.dp, bottom = 4.dp)
        )

        OutlinedButton(onClick = {
            // go to server chat room
            navController.navigate(Screens.ServerChatRoom.name)
        },
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp)
        ) {
            Text(text = "Start Server")
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

            Text(text = "How to:" +
                    "\n- Wait for other users to use the join button on the app." +
                    "\n- Share your local IP address to them." +
                    "\n- Make sure to Start the server before they try to connect." +
                    "\n- Have fun!",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }
    }
}