package com.example.freepowersocket.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.freepowersocket.domain.BleDevice


@SuppressLint("MissingPermission")
fun BluetoothDevice.toBleDeviceDomain(): BleDevice{
    return BleDevice(
        name = name,
        address = address,
        device = this
    )
}