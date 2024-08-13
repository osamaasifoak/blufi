package com.example.freepowersocket.presentation

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freepowersocket.domain.BleControllerInterface
import com.example.freepowersocket.domain.BleDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BleViewModel @Inject constructor(
    private val bleController: BleControllerInterface
) : ViewModel() {
    private val _state = MutableStateFlow(BleUiState())
    val state = combine(
        bleController.scannedDevices,
        bleController.pairedDevices,
        _state
    ) { scannedDevice, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevice,
            pairedDevices = pairedDevices
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        _state.value
    )

    fun startScan() {
        bleController.startScan()
    }

    fun stopScan() {
        bleController.stopScan()
    }

    suspend fun provisionDevice(activity: Activity, device: BleDevice, ssid: String, password: String) {
        bleController.provisionDevice(activity, device, ssid, password)
    }
}
