package com.classtrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.classtrack.feature.dashboard.ui.DashboardScreen
import com.classtrack.feature.history.ui.CalendarScreen
import com.classtrack.feature.subjects.ui.SubjectListScreen

/**
 * Hosts all top-level navigation destinations.
 * Each composable screen internally calls [hiltViewModel()] for its ViewModel.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Subjects.route,
        modifier = modifier,
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
        composable(Screen.Subjects.route) {
            SubjectListScreen()
        }
    }
}
