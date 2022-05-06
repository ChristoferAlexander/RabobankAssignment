package com.example.rabobankassignment.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.example.rabobankassignment.ui.nav.CustomRouteNavigator
import com.example.rabobankassignment.ui.nav.RouteNavigator

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun bindRouteNavigator(): RouteNavigator = CustomRouteNavigator()
}