package com.nima.socketchat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.nima.socketchat.*
import com.nima.socketchat.R
import com.nima.socketchat.components.ChatBubble
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    val nameDataStore = NameDataStore(context)
    val themeDataStore = ThemeDataStore(context)
    val dpDataStore = RoundedCornerDataStore(context)
    val spDataStore = ChatTextSizeDataStore(context)

    val theme = themeDataStore.getTheme.collectAsState(initial = 0).value
    val username = nameDataStore.getUsername.collectAsState(initial = "").value
    val dpValue = dpDataStore.getDp.collectAsState(initial = 0f).value
    val spValue = spDataStore.getTextSize.collectAsState(initial = 9f).value

    var dpInput by remember(dpValue){
        mutableStateOf(dpValue)
    }

    var spInput by remember(spValue){
        mutableStateOf(spValue)
    }

    var themeInput by remember(theme){
        mutableStateOf(theme)
    }

    var usernameInput by remember(){
        mutableStateOf("")
    }

    val themeValue by remember(theme){
        when(theme){
            0 -> mutableStateOf("System Theme")
            1 -> mutableStateOf("Light")
            else -> mutableStateOf("Dark")
        }
    }

    val themeInputValue by remember(themeInput) {
        when(themeInput){
            0 -> mutableStateOf("System Theme")
            1 -> mutableStateOf("Light")
            else -> mutableStateOf("Dark")
        }
    }

    var showUsernameDialog by remember {
        mutableStateOf(false)
    }
    var showThemeDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        if (showUsernameDialog){
            AlertDialog(onDismissRequest = {
                showUsernameDialog = false
                usernameInput = ""
            },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            nameDataStore.saveUsername(usernameInput.trim())
                            usernameInput = ""
                        }
                        showUsernameDialog = false
                    },
                        enabled = !usernameInput.isNullOrBlank()
                    ) {
                        Text(text = "Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showUsernameDialog = false
                        usernameInput = ""
                    }) {
                        Text(text = "Cancel")
                    }
                },
                icon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                title = {
                    Text(text = "Enter Username: $username",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                text = {
                    OutlinedTextField(value = usernameInput,
                        onValueChange = {
                            usernameInput = it
                        },
                    )
                }
            )
        }

        if (showThemeDialog){
            AlertDialog(onDismissRequest = {
                showThemeDialog = false
                themeInput = theme
            },
                confirmButton = {
                    TextButton(onClick = {
                        scope.launch {
                            themeDataStore.saveTheme(themeInput!!)
                        }
                        showThemeDialog = false
                    },
                    ) {
                        Text(text = "Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showThemeDialog = false
                        themeInput = theme
                    }) {
                        Text(text = "Cancel")
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_color_lens_24),
                        contentDescription = null)
                },
                title = {
                    Text(text = "Select Theme: $themeInputValue",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                text = {
                    Slider(value = themeInput!!.toFloat(),
                        onValueChange = {
                            themeInput = it.toInt()
                        },
                        valueRange = 0f..2f,
                        steps = 1
                    )
                }
            )
        }

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
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
            Text(text = "Settings",
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Text(text = "App",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Username: $username",
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    showUsernameDialog = true
                },
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "Theme: $themeValue",
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    showThemeDialog = true
                },
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_color_lens_24),
                    contentDescription = null)
            }
        }
        Divider(modifier = Modifier.padding(start = 16.dp))

        Text(text = "Chat",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChatBubble(name = username!!, text = "Hey There! \uD83D\uDE04")
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