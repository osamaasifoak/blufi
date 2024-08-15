package com.example.freepowersocket.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import blufi.espressif.BlufiCallback
import blufi.espressif.BlufiClient
import blufi.espressif.params.BlufiConfigureParams
import blufi.espressif.params.BlufiParameter
import com.example.freepowersocket.domain.BleControllerInterface
import com.example.freepowersocket.domain.BleDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

@SuppressLint("MissingPermission")
class BleController(
    private val context: Context
) : BleControllerInterface {
    private val bluetoothManager by lazy {
        context.getSystemService(
            BluetoothManager::class.java
        )
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _scannedDevices = MutableStateFlow<List<BleDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BleDevice>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BleDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<BleDevice>>
        get() = _pairedDevices.asStateFlow()


    private var _scanCallback: ScanCallback = object : ScanCallback() {
        override fun onBatchScanResults(results: List<ScanResult?>) {
            for (result in results) {
                Log.d("onBatchScanResults: ", result.toString())
                _scannedDevices.update { devices ->
                    val newDevice = result?.device?.toBleDeviceDomain()
                    if (newDevice != null && newDevice in devices) {
                        devices
                    } else {
                        if (newDevice != null)
                            devices + newDevice
                        else devices
                    }
                }
//                deviceFoundHandler.found(result?.getDevice(), result?.getRssi())
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("onScanFailed: ", errorCode.toString())
//            scanError(errorCode)
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.d("onScanResult: ", result.toString())
            _scannedDevices.update { devices ->
                val newDevice = result.device.toBleDeviceDomain()
                if (newDevice in devices) devices else devices + newDevice
            }
//            deviceFoundHandler.found(result.getDevice(), result.getRssi())
        }
    }


    override suspend fun startScan() {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return
        }

        bluetoothAdapter?.bluetoothLeScanner?.startScan(_scanCallback)
    }

    override fun stopScan() {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return
        }

        bluetoothAdapter?.bluetoothLeScanner?.stopScan(_scanCallback)
    }


    override fun release() {}

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
    }

    override suspend fun provisionDevice(
        activity: Activity,
        device: BleDevice,
        ssid: String,
        password: String
    ) =
        withContext(Dispatchers.IO) {
            val blufiClient = BlufiClient(activity, device.device)
            blufiClient.setBlufiCallback( object : BlufiCallback() {

                override fun onGattPrepared(
                    client: BlufiClient?,
                    gatt: BluetoothGatt?,
                    service: BluetoothGattService?,
                    writeChar: BluetoothGattCharacteristic?,
                    notifyChar: BluetoothGattCharacteristic?
                ) {
                    super.onGattPrepared(client, gatt, service, writeChar, notifyChar)
                }

                override fun onConfigureResult(client: BlufiClient?, status: Int) {
                    if (status == STATUS_SUCCESS) {
                        // Provisioning successful
                        _pairedDevices.update { devices ->
                            if (device in devices) devices
                            else devices + device

                        }
                        announceDeviceUsingMdns(device)
                    } else {
                        // Provisioning failed
                        Log.e("$status", client.toString())
                        _pairedDevices.update { devices ->
                            if (device in devices) devices
                            else devices + device

                        }
                    }
                }

                override fun onError(client: BlufiClient?, errCode: Int) {
                    Log.e("$errCode", client.toString())
                    // Handle error
                }
            })

            blufiClient.connect()

            val params = BlufiConfigureParams()
            params.opMode = BlufiParameter.OP_MODE_SOFTAP
            params.softAPChannel = 6  // Choose a valid channel (1-13 for 2.4GHz)
            params.softAPMaxConnection = 4
//            params.staSSIDBytes = ssid.toByteArray()
//            params.staPassword = password
            blufiClient.configure(params)
        }

    @SuppressLint("MissingPermission")
    private fun announceDeviceUsingMdns(device: BleDevice) {
        try {
            val jmdns = JmDNS.create(InetAddress.getLocalHost())
            val serviceInfo =
                ServiceInfo.create("_http._tcp.local.", device.name, 80, "path=index.html")
            jmdns.registerService(serviceInfo)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
