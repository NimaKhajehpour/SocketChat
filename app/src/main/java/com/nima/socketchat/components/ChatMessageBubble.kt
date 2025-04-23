package com.nima.socketchat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatMessageBubble(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Float,
    cornerRadius: Float,
    sentMessage: Boolean
    ) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (sentMessage) Arrangement.End else Arrangement.Start
    ){
        Card (
            shape = RoundedCornerShape(cornerRadius.dp)
        ){
            Column (
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text,
                    fontSize = fontSize.sp,
                )
            }
        }
    }

}