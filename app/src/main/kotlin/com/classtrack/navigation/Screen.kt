package com.classtrack.navigation

/**
 * Type-safe navigation destinations for the three main tabs.
 * Using simple string routes for Jetpack Navigation Compose 2.7.x.
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Calendar : Screen("calendar")
    data object Subjects : Screen("subjects")
}
