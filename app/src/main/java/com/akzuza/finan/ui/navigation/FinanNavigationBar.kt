package com.akzuza.finan.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun FinanNavigationBar(
    navigationCallback: (String) -> Unit,
    selected: String
) {
    NavigationBar (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        TOP_LEVEL_FINAN_DESTINATIONS.mapIndexed() { _, dest ->
            NavigationBarItem(
                selected = selected == dest.route,
                onClick = { navigationCallback(dest.route) },
                icon = { Icon(
                    imageVector = if(selected == dest.route) dest.selectedIcon else dest.unselectedIcon,
                    contentDescription = null
                ) },
                label = { Text(text = dest.route) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFinanNavigationBar() {
    val navController = rememberNavController()
    var selectedDestination by remember {
        mutableStateOf(FinanRoute.HOME)
    }

    FinanNavigationBar(
        navigationCallback = { dest -> navController.navigate(dest); selectedDestination = dest},
        selected = selectedDestination
    )
}
