package com.example.freepowersocket.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.freepowersocket.R
import com.example.freepowersocket.presentation.BleUiState
import com.example.freepowersocket.presentation.shared.Appbar
import com.example.freepowersocket.presentation.shared.DeviceListItem
import com.example.freepowersocket.ui.theme.FreePowerSocketTheme

@Composable
fun DeviceScanScreen(
    navController: NavController,
    onStartScan: () -> Unit,
    state: BleUiState,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {
        onStartScan()
    }

    Scaffold(containerColor = Color.White, topBar = {
        Appbar(
            label = stringResource(id = R.string.connect_new_devices),
            cardTitle = stringResource(id = R.string.scanning_for_new_devices),
            cardSubtitle = "${state.scannedDevices.size + 1} Found"
        )
    }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 16.dp)
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
            onStartScan = {})
    }
}