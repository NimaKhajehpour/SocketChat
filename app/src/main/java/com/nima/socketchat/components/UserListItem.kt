package com.nima.socketchat.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserListItem(
    modifier: Modifier = Modifier,
    username: String,
    online: Boolean,
    onClick: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Text(username,
                Modifier.padding(end = 10.dp)
                )
            AnimatedVisibility(online) {
                Text("Online", color = Color.Green, modifier = Modifier.padding(end = 10.dp))
            }
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    onClick()
                },
            ) {
                Text("Start Chat")
            }
        }
    }
}