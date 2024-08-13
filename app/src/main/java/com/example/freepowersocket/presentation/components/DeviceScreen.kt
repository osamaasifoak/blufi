package com.example.freepowersocket.presentation.components

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.freepowersocket.domain.BleDevice
import com.example.freepowersocket.presentation.BleUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction4

@Composable
fun DeviceScreen(
    navController: NavController,
    context: Context,
    activity: Activity,
    wifiPassword: String,
    state: BleUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    provisionDevice: KSuspendFunction4<Activity, BleDevice, String, String, Unit>
) {
    val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo;
    val ssid = wifiInfo.ssid

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        BleDeviceList(
            pairedDevices = state.pairedDevices,
            scannedDevices = state.scannedDevices,
            onClick = { bleDevice ->
                CoroutineScope(Dispatchers.Main).launch {
                    provisionDevice(
                        activity,
                        bleDevice,
                        ssid,
                        wifiPassword
                    )
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onStartScan) {
                Text(text = "Start Scan")
            }
            Button(onClick = onStopScan) {
                Text(text = "Stop Scan")
            }
        }

    }
}

@Composable
fun BleDeviceList(
    pairedDevices: List<BleDevice>,
    scannedDevices: List<BleDevice>,
    onClick: (BleDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(pairedDevices) { device ->
            Text(
                text = device.name ?: "Unknown",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(device)
                    }
                    .padding(16.dp)
            )
        }

        item {
            Text(
                text = "Scanned Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(scannedDevices) { device ->
            Text(
                text = device.name ?: "Unknown",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(device)
                    }
                    .padding(16.dp)
            )
        }
    }
}