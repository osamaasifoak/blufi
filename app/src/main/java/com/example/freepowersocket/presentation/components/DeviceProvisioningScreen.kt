package com.example.freepowersocket.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freepowersocket.R
import com.example.freepowersocket.domain.BleDevice
import com.example.freepowersocket.presentation.BleUiState
import com.example.freepowersocket.presentation.ProvisioningDeviceUiState
import com.example.freepowersocket.presentation.shared.ActivityCard
import com.example.freepowersocket.presentation.shared.Appbar
import com.example.freepowersocket.presentation.shared.DeviceListItem
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DeviceProvisioningScreen(
    navController: NavController,
    ssid: String,
    wifiPassword: String,
    state: BleUiState,
    provisionDeviceState: BleUiState,
    provisionDevice: suspend (BleDevice, String, String) -> Unit,
) {
    var isConfiguringCompleted by remember { mutableStateOf(true) }
    val provisioningDeviceMap = provisionDeviceState.provisioningDeviceStates.keys.toList()
    val configuredDeviceCount =
        provisioningDeviceMap.map { it -> provisionDeviceState.provisioningDeviceStates[it] is ProvisioningDeviceUiState.Success }

    LaunchedEffect(state.scannedDevices) {
        if (state.scannedDevices.isNotEmpty()) {
            for (device in state.scannedDevices) {
                val scanJob = CoroutineScope(Dispatchers.IO).launch {
                    provisionDevice(device, ssid, wifiPassword)
                }
                isConfiguringCompleted = false
            }
        }
    }

    Scaffold(containerColor = Color.White, bottomBar = {
        if (!isConfiguringCompleted) {
            Button(
                onClick = {
                    navController.navigate("WifiPasswordScreen")
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.royal)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.continue_),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    },

        topBar = {
            Appbar(label = stringResource(id = R.string.configuring_devices), widget = {
                ActivityCard(
                    cardTitle = "${configuredDeviceCount + 1}/${state.scannedDevices.size + 1} ${
                        stringResource(
                            id = R.string.devices_configured
                        )
                    }",
                    cardSubtitle = if (provisioningDeviceMap.size == state.scannedDevices.size) stringResource(
                        id = R.string.success
                    )
                    else stringResource(
                        id = R.string.please_wait
                    ),
                    leadingLottieAsset = if (provisioningDeviceMap.size != state.scannedDevices.size) R.raw.cog else null
                )
            })
        }


    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {

            }
            itemsIndexed(provisioningDeviceMap) { index, deviceAddress ->
                val deviceState =
                    provisionDeviceState.provisioningDeviceStates[deviceAddress]

                when (deviceState) {
                    is ProvisioningDeviceUiState.Loading -> {
                        DeviceListItem(
                            label = deviceState.device.name ?: "Unknown",
                            subTitle = stringResource(id = R.string.configuring),
                            hadTopDivider = index == 0,
                            trailingLottieAsset = R.raw.configuring
                        )
                    }

                    is ProvisioningDeviceUiState.Success -> {
                        DeviceListItem(
                            label = deviceState.device.name ?: "Unknown",
                            subTitle = stringResource(id = R.string.configuring),
                            hadTopDivider = index == 0,
                            trailingSvgAsset = R.drawable.ic_success
                        )
                    }

                    is ProvisioningDeviceUiState.Error -> {
                        DeviceListItem(
                            label = deviceState.device.name ?: "Unknown",
                            subTitle = deviceState.exception.message.toString(),
                            hadTopDivider = index == 0,
                            trailingSvgAsset = R.drawable.ic_fail
                        )
                    }

                    else -> {
                        Text("Unknown state for device $deviceAddress")
                    }
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceProvisioningScreenPreview() {
    FreePowerSocketTheme {
        DeviceProvisioningScreen(
            navController = rememberNavController(),
            ssid = "Test",
            wifiPassword = "Test",
            state = BleUiState(),
            provisionDeviceState = BleUiState(),
            provisionDevice = { _: BleDevice, _: String, _: String -> },
        )
    }
}