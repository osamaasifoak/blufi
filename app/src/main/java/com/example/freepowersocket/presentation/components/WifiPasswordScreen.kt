package com.example.freepowersocket.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.TextField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WifiPasswordScreen(
    navController: NavController
) {

    var wifiPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "Enter Wifi Password",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = wifiPassword,
            onValueChange = {  wifiPassword = it },
            modifier = Modifier.padding(16.dp)
        )

        Button(onClick = {
            navController.navigate("DeviceScreen/?password=$wifiPassword")
        }) {
            Text(text = "Continue")
        }
    }
}