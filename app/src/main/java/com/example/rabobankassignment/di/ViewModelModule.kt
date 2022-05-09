package com.example.rabobankassignment.di

import com.example.rabobankassignment.ui.nav.RouteNavigator
import com.example.rabobankassignment.ui.nav.RouteNavigatorIml
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
open class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun bindRouteNavigator(): RouteNavigator = RouteNavigatorIml()
}