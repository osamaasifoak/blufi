package com.example.freepowersocket.domain

import android.app.Activity
import com.example.freepowersocket.presentation.ProvisioningDeviceUiState
import kotlinx.coroutines.flow.StateFlow


interface BleControllerInterface {
    val scannedDevices: StateFlow<List<BleDevice>>
    val pairedDevices: StateFlow<List<BleDevice>>
    val provisioningDeviceStates: StateFlow<Map<String, ProvisioningDeviceUiState>>

    suspend fun startScan()
    fun stopScan()
    suspend fun provisionDevice(device: BleDevice, ssid: String, password: String)

    fun release()
}