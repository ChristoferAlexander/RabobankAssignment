package com.example.rabobankassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rabobankassignment.ui.compose.DownloadScreen
import com.example.rabobankassignment.ui.compose.LoadedScreen
import com.example.rabobankassignment.ui.nav.NavigationComponent
import com.example.rabobankassignment.ui.theme.RabobankAssignmentTheme
import com.example.rabobankassignment.viewModel.DownloadViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RabobankAssignmentTheme {
                MainLayout()
            }
        }
    }
}

@Composable
private fun MainLayout() {
    val navController = rememberNavController()
    NavigationComponent(navController)
}