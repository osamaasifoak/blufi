package com.example.freepowersocket.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.*
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import blufi.espressif.BlufiCallback
import blufi.espressif.BlufiClient
import blufi.espressif.params.BlufiConfigureParams
import blufi.espressif.params.BlufiParameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

class BleScanner(private val activity: Activity) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val handler = Handler(Looper.getMainLooper())
    private val scanPeriod: Long = 10000 // 10 seconds

    private val scannedDevices = mutableMapOf<String, BluetoothDevice>()
    private var scanCallback: ScanCallback? = null

    suspend fun startScan(): Map<String, BluetoothDevice> = withContext(Dispatchers.IO) {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return@withContext emptyMap()
        }

        bluetoothAdapter?.bluetoothLeScanner?.let { scanner ->
            scannedDevices.clear()
            scanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    result?.device?.let { device ->
                        scannedDevices[device.address] = device
                    }
                }

                override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                    results?.forEach { result ->
                        result.device?.let { device ->
                            scannedDevices[device.address] = device
                        }
                    }
                }

                override fun onScanFailed(errorCode: Int) {
                    // Handle scan failure
                }
            }

            scanner.startScan(null, buildScanSettings(), scanCallback)
            handler.postDelayed({
                scanner.stopScan(scanCallback)
            }, scanPeriod)
        }

        // Wait until the scan period is over
        withContext(Dispatchers.Main) {
            kotlinx.coroutines.delay(scanPeriod)
        }

        return@withContext scannedDevices
    }

    @SuppressLint("MissingPermission")
    fun stopScan() {
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }

    private fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
    }

    suspend fun provisionDevice(device: BluetoothDevice, ssid: String, password: String) =
        withContext(Dispatchers.IO) {
            val blufiClient = BlufiClient(activity, device)
            blufiClient.setBlufiCallback(object : BlufiCallback() {
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
                        announceDeviceUsingMdns(device)
                    } else {
                        // Provisioning failed
                        Log.e("$status", client.toString())
                    }
                }

                override fun onError(client: BlufiClient?, errCode: Int) {
                    Log.e("$errCode", client.toString())
                    // Handle error
                }
            })

            blufiClient.connect()

            val params = BlufiConfigureParams()
            params.opMode = BlufiParameter.OP_MODE_STA
            params.staSSIDBytes = ssid.toByteArray()
            params.staPassword = password
            blufiClient.configure(params)
        }

    @SuppressLint("MissingPermission")
    private fun announceDeviceUsingMdns(device: BluetoothDevice) {
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
