package com.example.freepowersocket.presentation

import com.example.freepowersocket.domain.BleDevice

sealed class ProvisioningDeviceUiState {
    data class Loading(val device: BleDevice) : ProvisioningDeviceUiState()
    data class Success(val device: BleDevice) : ProvisioningDeviceUiState()
    data class Error(val device: BleDevice, val exception: Throwable) : ProvisioningDeviceUiState()
}