package com.example.freepowersocket.domain

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow


interface BleControllerInterface {
    val scannedDevices: StateFlow<List<BleDevice>>
    val pairedDevices: StateFlow<List<BleDevice>>

    fun startScan ()
    fun stopScan()
   suspend fun provisionDevice(activity: Activity, device: BleDevice, ssid: String, password: String)

    fun release()
}