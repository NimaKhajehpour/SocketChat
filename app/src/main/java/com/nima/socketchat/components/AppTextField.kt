package com.nima.socketchat.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    singleLine: Boolean,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        singleLine = singleLine,
        modifier = modifier,
        label = {
            Text(label)
        }
    )
}