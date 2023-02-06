package com.nima.socketchat.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nima.socketchat.ChatTextSizeDataStore
import com.nima.socketchat.RoundedCornerDataStore

@Composable
fun ChatBubble (
    name: String,
    text: String,
    time: String
){
    val context = LocalContext.current
    val dpDataStore = RoundedCornerDataStore(context)
    val spDataStore = ChatTextSizeDataStore(context)
    val dpValue = dpDataStore.getDp.collectAsState(initial = 0f).value
    val spValue = spDataStore.getTextSize.collectAsState(initial = 9f).value

    Card(
        shape = RoundedCornerShape(dpValue!!.dp),
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp)
            .wrapContentWidth()
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 12.dp, end = 40.dp, top = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 5.dp),
                fontSize = (spValue!! + 3f).sp
            )

            Text(text = text,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = (spValue!! + 1f).sp
            )

            Text(text = time,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light,
                fontSize = spValue.sp
            )
        }
    }
}