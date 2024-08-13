package com.example.freepowersocket.presentation

import com.example.freepowersocket.domain.BleDevice

data class BleUiState(
    val scannedDevices: List<BleDevice> = emptyList(),
    val pairedDevices: List<BleDevice> = emptyList()
)
