package com.nima.socketchat.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.socketchat.ChatTextSizeDataStore
import com.nima.socketchat.RoundedCornerDataStore
import com.nima.socketchat.components.ChatMessageBubble
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val dpDataStore = RoundedCornerDataStore(context)
    val spDataStore = ChatTextSizeDataStore(context)

    val dpValue = dpDataStore.getDp.collectAsState(initial = 0f).value
    val spValue = spDataStore.getTextSize.collectAsState(initial = 9f).value

    var dpInput by remember{
        mutableStateOf(dpValue)
    }

    var spInput by remember(spValue){
        mutableStateOf(spValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 32.dp, top = 8.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(text = "Settings",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChatMessageBubble(
                text = "This is a test message",
                fontSize = spValue!!,
                cornerRadius = dpValue!!,
                sentMessage = true,
                modifier = Modifier.wrapContentWidth()
            )
        }

        Text(text = "Chat Bubble Corner Radius:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
        )

        Slider(value = dpInput!!,
            onValueChange = {
                dpInput = it
                scope.launch {
                    dpDataStore.saveDp(dpInput!!)
                }
            },
            valueRange = 0f..25f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp)
        )

        Text(text = "Chat Bubble Text Size:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 4.dp)
        )

        Slider(value = spInput!!,
            onValueChange = {
                spInput = it
                scope.launch {
                    spDataStore.saveTextSize(spInput!!)
                }
            },
            valueRange = 7f..17f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp)
        )

    }
}