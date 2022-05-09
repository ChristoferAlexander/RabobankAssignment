package com.example.rabobankassignment.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.rabobankassignment.model.CsvNavResult
import com.example.rabobankassignment.ui.compose.DisplayRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.rabobankassignment.ui.nav.RouteNavigator
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel constructor(
    val data: CsvNavResult,
    private val routeNavigator: RouteNavigator
) : ViewModel(), RouteNavigator by routeNavigator {

    @Inject constructor(savedStateHandle: SavedStateHandle, routeNavigator: RouteNavigator) : this(DisplayRoute.getData(savedStateHandle), routeNavigator)

}