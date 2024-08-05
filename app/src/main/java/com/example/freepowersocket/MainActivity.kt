package com.example.freepowersocket

import ChargerSocketManager
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val host =
        "10.0.2.2" // Special alias to refer to the host machine from the Android emulator
    private val port = 12345
    private val command = byteArrayOf(0x01, 0x02, 0x03) // Replace with your actual command
    private val socketManager = ChargerSocketManager(host, port)
    private val permissionHelper: PermissionHelper by lazy { PermissionHelper(this) }
    private val bleScanner: BleScanner by lazy { BleScanner(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreePowerSocketTheme {
                Scaffold(
                    content = { innerPadding ->
                        MyApp(
                            context = baseContext,
                            bleScanner, Modifier
                                .padding(innerPadding)
                        )
                    }
                )

            }
        }

        if (permissionHelper.hasPermissions()) {
            CoroutineScope(Dispatchers.Main).launch {
                bleScanner.startScan()
            }

        } else {
            permissionHelper.requestPermissions()
        }

        CoroutineScope(Dispatchers.Main).launch {
            val success = socketManager.sendCommand(command)
            if (success) {
                // Handle successful command send
            } else {
                // Handle failed command send
            }
            val systemVersion = socketManager.getSystemVersions()
            if (systemVersion) {
                // Handle successful command send
            } else {
                // Handle failed command send
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bleScanner.stopScan()
    }
}


@Composable
fun MyApp(context: Context, bleScanner: BleScanner, modifier: Modifier = Modifier) {
    var isScanning by remember { mutableStateOf(false) }
    var scannedDevices by remember { mutableStateOf<Map<String, BluetoothDevice>>(emptyMap()) }
    val coroutineScope = rememberCoroutineScope()
    val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo;
    val ssid = wifiInfo.ssid

    Column(modifier = modifier.fillMaxSize()) {
        Row {
            Box(
                modifier = Modifier.height(48.dp),
            ) {
                Button(
                    onClick = {
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
                Button(
                    onClick = {
                        if (scannedDevices.isNotEmpty()) {
                            for (device in scannedDevices.values.toList())
                                coroutineScope.launch {
                                    bleScanner.provisionDevice(device, "$ssid", "Pakistan123+")
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

