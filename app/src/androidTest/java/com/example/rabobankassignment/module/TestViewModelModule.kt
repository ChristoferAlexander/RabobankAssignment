package com.example.rabobankassignment.module

import com.example.rabobankassignment.di.ViewModelModule
import com.example.rabobankassignment.ui.nav.RouteNavigator
import com.example.rabobankassignment.ui.nav.RouteNavigatorIml
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [ViewModelModule::class]
)
class TestViewModelModule {

    @Provides
    @ViewModelScoped
    fun bindTestRouteNavigator(): RouteNavigator = RouteNavigatorIml()
}

