package com.example.rabobankassignment.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.rabobankassignment.ui.compose.DisplayRoute
import com.example.rabobankassignment.ui.compose.DownloadRoute

@Composable
fun NavigationComponent(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = DownloadRoute.route
    ) {
        DownloadRoute.composable(this, navHostController)
        DisplayRoute.composable(this, navHostController)
    }
}