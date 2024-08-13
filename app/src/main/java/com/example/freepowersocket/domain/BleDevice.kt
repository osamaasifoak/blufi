package com.example.freepowersocket.domain

import android.bluetooth.BluetoothDevice

data class BleDevice(
    val name: String?,
    val address: String,
    val device: BluetoothDevice
)
