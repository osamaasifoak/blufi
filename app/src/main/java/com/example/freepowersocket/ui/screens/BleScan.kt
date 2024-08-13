package com.example.freepowersocket.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freepowersocket.services.BleScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BleScan(context: Context, activity: Activity, modifier: Modifier =Modifier) {
    var isScanning by remember { mutableStateOf(false) }
    var scannedDevices by remember { mutableStateOf<Map<String, BluetoothDevice>>(emptyMap()) }
    val bleScanner: BleScanner by lazy { BleScanner(activity) }
    val coroutineScope = rememberCoroutineScope()
    val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo;
    val ssid = wifiInfo.ssid

    Column(modifier = modifier.fillMaxSize()) {
        Row {
            Box(
                modifier = Modifier.height(48.dp),
            ) {
                Button(onClick = {
                    if (isScanning) {
                        bleScanner.stopScan()
                    } else {
                        coroutineScope.launch(Dispatchers.Main) {
                            scannedDevices = bleScanner.startScan()
                        }
                    }
                    isScanning = !isScanning
                }) {
                    Text(if (isScanning) "Stop Scan" else "Start Scan")
                }
            }
            Box(
                modifier = Modifier.height(48.dp),
            ) {
                Button(onClick = {
                    if (scannedDevices.isNotEmpty()) {
                        for (device in scannedDevices.values.toList()) coroutineScope.launch {
                            bleScanner.provisionDevice(device, ssid, "Pakistan123+")
                        }
                    }
                }) {
                    Text("Provision Devices")
                }
            }
        }
        LazyColumn {
            items(scannedDevices.values.toList()) { device ->
                DeviceItem(device = device)
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(device: BluetoothDevice, modifier: Modifier = Modifier) {
    Text(
        text = "${device.name ?: "Unknown"} - ${device.address}",
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
