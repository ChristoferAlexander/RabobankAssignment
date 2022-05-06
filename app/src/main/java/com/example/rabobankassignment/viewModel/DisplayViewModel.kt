package com.example.rabobankassignment.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.rabobankassignment.ui.compose.DisplayRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.rabobankassignment.ui.nav.RouteNavigator
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator
) : ViewModel(), RouteNavigator by routeNavigator {

     val data = DisplayRoute.getData(savedStateHandle)

}