package com.attendease.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.attendease.feature.calendar.CalendarScreen
import com.attendease.feature.dashboard.DashboardScreen
import com.attendease.feature.subjects.SubjectsScreen
import kotlinx.serialization.Serializable

@Serializable
object DashboardRoute

@Serializable
object CalendarRoute

@Serializable
object SubjectsRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val items = listOf(
                    Triple(DashboardRoute, "Dashboard", Icons.Filled.Home),
                    Triple(CalendarRoute, "Calendar", Icons.Filled.DateRange),
                    Triple(SubjectsRoute, "Subjects", Icons.Filled.List)
                )

                items.forEach { (route, label, icon) ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route?.contains(route::class.qualifiedName ?: "") == true } == true
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<DashboardRoute> { DashboardScreen() }
            composable<CalendarRoute> { CalendarScreen() }
            composable<SubjectsRoute> { SubjectsScreen() }
        }
    }
}
