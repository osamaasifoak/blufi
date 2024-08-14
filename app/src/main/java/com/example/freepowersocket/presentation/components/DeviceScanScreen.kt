package com.example.freepowersocket.presentation.components

import android.bluetooth.BluetoothClass.Device
import android.provider.Settings.Global.getString
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freepowersocket.R
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable
fun DeviceScanScreen(navController: NavController) {

    Scaffold(topBar = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = Color.Black)
                    .alpha(0.6f)
            ) {
                Image(
                    painter = painterResource(R.drawable.product_close),
                    contentDescription = "product banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.connect_new_devices),
                    fontSize = 32.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)

                )
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.card_bg)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = stringResource(id = R.string.scanning_for_new_devices),
                            fontSize = 20.sp,
                            color = Color.White,
                            )
                        Text(
                            text = "5 found",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 1.dp),

                            )
                    }

                }
            }

        }
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceScreenPreview() {
    FreePowerSocketTheme {
        DeviceScanScreen(navController = rememberNavController())
    }
}