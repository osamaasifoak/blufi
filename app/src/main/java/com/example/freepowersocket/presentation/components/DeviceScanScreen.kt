package com.example.freepowersocket.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freepowersocket.R
import com.example.freepowersocket.presentation.BleUiState
import com.example.freepowersocket.presentation.shared.ActivityCard
import com.example.freepowersocket.presentation.shared.Appbar
import com.example.freepowersocket.presentation.shared.DeviceListItem
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DeviceScanScreen(
    navController: NavController,
    onStartScan: suspend () -> Unit,
    onStopScan: () -> Unit,
    state: BleUiState,
    modifier: Modifier = Modifier
) {
    var currentProgress by remember { mutableFloatStateOf(0f) }
    var isScanning by remember { mutableStateOf(true) }
    val scanDuration = 5000L
    val progressInterval = 100L
    val totalSteps = scanDuration / progressInterval


    LaunchedEffect(Unit) {
        val scanJob = CoroutineScope(Dispatchers.IO).launch {
            onStartScan()
        }

        for (i in 1..totalSteps) {
            delay(progressInterval)
            currentProgress = i / totalSteps.toFloat()
        }
        isScanning = false
        onStopScan()
        scanJob.cancel()
    }

    Scaffold(containerColor = Color.White, bottomBar = {
        if (!isScanning) {
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
            Appbar(label = stringResource(id = R.string.connect_new_devices), widget = {
                ActivityCard(
                    leadingLottieAsset = if (isScanning) R.raw.radar else null,
                    leadingSvgAsset = if (!isScanning) R.drawable.ic_scan_complete else null,
                    cardTitle = if (isScanning) stringResource(id = R.string.scanning_for_new_devices) else stringResource(
                        id = R.string.scan_completed
                    ),
                    cardSubtitle = "${state.scannedDevices.size + 1} Found", widget = {
                        LinearProgressIndicator(
                            progress = { currentProgress },
                            color = Color.White,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .padding(top = 2.dp)
                        )
                    })
            })
        }


    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {
            itemsIndexed(state.scannedDevices) { index, device ->
                DeviceListItem(
                    label = device.name ?: "Unknown",
                    subTitle = device.address,
                    hadTopDivider = index == 0
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeviceScreenPreview() {
    FreePowerSocketTheme {
        DeviceScanScreen(navController = rememberNavController(),
            state = BleUiState(),
            onStartScan = suspend {},
            onStopScan = {})
    }
}