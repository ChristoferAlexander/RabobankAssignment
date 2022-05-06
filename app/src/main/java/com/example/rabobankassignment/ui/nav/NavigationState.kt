package com.example.rabobankassignment.ui.nav

import java.util.UUID

/**
 * State that can be used to trigger navigation.
 */
sealed class NavigationState {

    object Idle : NavigationState()

    data class NavigateToRoute(val route: String, val id: String = UUID.randomUUID().toString()) : NavigationState()

    /**
     * @param staticRoute is the static route to pop to, without parameter replacements.
     */
    data class PopToRoute(val staticRoute: String, val id: String = UUID.randomUUID().toString()) : NavigationState()

    data class NavigateUp(val id: String = UUID.randomUUID().toString()) : NavigationState()
}