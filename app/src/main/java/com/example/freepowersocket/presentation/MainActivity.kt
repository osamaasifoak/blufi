package com.example.freepowersocket.presentation

import ChargerSocketManager
import android.bluetooth.BluetoothManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import com.example.freepowersocket.PermissionHelper
import com.example.freepowersocket.services.BleScanner
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import com.example.freepowersocket.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val host =
        "10.0.2.2" // Special alias to refer to the host machine from the Android emulator
    private val port = 12345
    private val command = byteArrayOf(0x01, 0x02, 0x03) // Replace with your actual command
    private val socketManager = ChargerSocketManager(host, port)
    private val permissionHelper: PermissionHelper by lazy { PermissionHelper(this) }
    private val bleScanner: BleScanner by lazy { BleScanner(this) }

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { /* Not needed */ }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val canEnableBluetooth = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else true

            if(canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }

        enableEdgeToEdge()
        setContent {
            FreePowerSocketTheme {
                Surface(
                    content = {
                        AppNavigation(activity = Activity(), context = baseContext)
                    }
                )
            }
        }

        if (permissionHelper.hasPermissions()) {
            CoroutineScope(Dispatchers.Main).launch {
//                bleScanner.startScan()
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

