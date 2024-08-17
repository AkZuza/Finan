package com.akzuza.finan.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.akzuza.finan.R

object FinanRoute {
    val OVERVIEW = "Overview"
    val HOME = "Home"
    val SETTINGS = "Settings"
}

data class FinanDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId:Int
)

val TOP_LEVEL_FINAN_DESTINATIONS = listOf(
    FinanDestination(
        route = FinanRoute.OVERVIEW,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        iconTextId = R.string.tab_overview
    ),

    FinanDestination(
        route = FinanRoute.HOME,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.tab_home
    ),

    FinanDestination(
        route = FinanRoute.SETTINGS,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        iconTextId = R.string.tab_settings
    )
)