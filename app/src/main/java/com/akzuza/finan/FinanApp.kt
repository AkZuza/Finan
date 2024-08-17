package com.akzuza.finan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akzuza.finan.data.FinanTransactionEvent
import com.akzuza.finan.data.sms.CheckAndRequestForSMSPermission
import com.akzuza.finan.ui.home.FinanHomeScreen
import com.akzuza.finan.ui.home.FinanHomeState
import com.akzuza.finan.ui.home.NewTransactionDialog
import com.akzuza.finan.ui.navigation.FinanNavigationBar
import com.akzuza.finan.ui.navigation.FinanRoute
import com.akzuza.finan.ui.overview.FinanOverviewScreen
import com.akzuza.finan.ui.settings.FinanSettingsScreen


@Composable
fun FinanApp(homeState: FinanHomeState, onEvent: (FinanTransactionEvent) -> Unit) {

    val navController = rememberNavController()
    var selectedDestination by remember {
        mutableStateOf(FinanRoute.HOME)
    }


    CheckAndRequestForSMSPermission()

    Scaffold (
        bottomBar = {
            FinanNavigationBar(
                navigationCallback = { dest ->
                    navController.navigate(dest)
                    selectedDestination = dest },
                selected = selectedDestination
            )
        },
        floatingActionButton = {
            if(selectedDestination == FinanRoute.HOME)
                FloatingActionButton(onClick = { onEvent(FinanTransactionEvent.ShowDialog) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                }
        },
    ) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = FinanRoute.HOME
        ) {
            composable(FinanRoute.OVERVIEW) {
                FinanOverviewScreen()
            }

            composable(FinanRoute.HOME) {
                FinanHomeScreen(
                    homeState = homeState,
                    onEvent = onEvent
                )
            }

            composable(FinanRoute.SETTINGS) {
                FinanSettingsScreen()
            }
        }
    }

    if(homeState.showTransactionDialog)
        NewTransactionDialog(state = homeState, onEvent = onEvent)
}



@Preview
@Composable
fun PreviewFinanApp() {
    FinanApp(
        homeState = FinanHomeState(),
        { event -> }
    )
}