package com.example.freepowersocket.presentation

import com.example.freepowersocket.domain.BleDevice
import kotlinx.coroutines.flow.MutableStateFlow

data class BleUiState(
    val scannedDevices: List<BleDevice> = emptyList(),
    val pairedDevices: List<BleDevice> = emptyList(),
    val provisioningDeviceStates: Map<String, ProvisioningDeviceUiState> = emptyMap()
)
