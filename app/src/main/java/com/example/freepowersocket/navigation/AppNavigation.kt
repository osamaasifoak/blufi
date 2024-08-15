package com.example.freepowersocket.navigation

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.freepowersocket.presentation.BleViewModel
import com.example.freepowersocket.presentation.components.DeviceScanScreen
import com.example.freepowersocket.presentation.components.DeviceScreen
import com.example.freepowersocket.presentation.components.WifiPasswordScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    activity: Activity,
    context: Context
) {
    NavHost(navController = navController, startDestination = "DeviceScanScreen") {

        composable("DeviceScanScreen") {
            val viewModel = hiltViewModel<BleViewModel>()
            val state by viewModel.state.collectAsState()
            DeviceScanScreen(
                navController = navController,
                onStartScan = viewModel::startScan,
                onStopScan = viewModel::stopScan,
                state = state,
            )
        }

        composable("WifiPasswordScreen") {
            WifiPasswordScreen(navController = navController)
        }

        composable("DeviceScreen/?password={password}",
            arguments = listOf(
                navArgument("password") {
                    nullable = true
                }
            )
        ) { backStackEntry ->

            val viewModel = hiltViewModel<BleViewModel>()
            val state by viewModel.state.collectAsState()
            DeviceScreen(
                navController = navController,
                context = context,
                activity = activity,
                wifiPassword = backStackEntry.arguments?.getString("password").toString(),
                state = state,
                onStartScan = viewModel::startScan,
                onStopScan = viewModel::stopScan,
                provisionDevice = viewModel::provisionDevice,
            )
        }

    }
}