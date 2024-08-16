package com.example.freepowersocket.presentation.components

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.TextField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freepowersocket.R
import com.example.freepowersocket.presentation.shared.ActivityCard
import com.example.freepowersocket.presentation.shared.Appbar
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable
fun WifiPasswordScreen(
    navController: NavController,
    hasPreviewMode: Boolean = false
) {
    var ssid: String = ""
    var wifiPassword by remember { mutableStateOf("") }

    if (!hasPreviewMode) {
        val context = LocalContext.current
        val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo;
        ssid = wifiInfo.ssid
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Appbar(
                label = stringResource(id = R.string.configure_wifi),
                widget = {
                    ActivityCard(
                        cardTitle = "$ssid",
                        cardSubtitle = "SSID",
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {

            Text(
                text = stringResource(id = R.string.wifi),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            )

            TextField(
                value = wifiPassword,
                onValueChange = { wifiPassword = it },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                ),
                label = {
                    Text(
                        text = stringResource(id = R.string.password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset((-16).dp, 0.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
                    .fillMaxWidth(),
            )
            Button(
                onClick = {
                    navController.navigate("DeviceProvisioningScreen/$ssid?password=$wifiPassword")
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.royal)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Continue", color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Preview
@Composable
fun WifiPasswordScreenPreview() {
    FreePowerSocketTheme {
        WifiPasswordScreen(navController = rememberNavController(), hasPreviewMode = true)
    }
}