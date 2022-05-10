package com.example.rabobankassignment.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rabobankassignment.EntryPointActivity
import com.example.rabobankassignment.MainLayout
import com.example.rabobankassignment.module.MockServer
import com.example.rabobankassignment.ui.theme.RabobankAssignmentTheme
import com.example.rabobankassignment.viewModel.DownloadViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/*
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
// TODO fix injection error of Navigator for ViewModel
class DownloadScreenTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<EntryPointActivity>()

    @Inject
    lateinit var loginViewModel: DownloadViewModel

    @Before
    fun setUp() {
        MockServer.server.dispatcher = successDispatcher()
        hiltTestRule.inject()
    }

    @After
    fun tearDown() {
        MockServer.server.shutdown()
    }

    @Test
    fun app_displays_list_of_items() {
        composeTestRule.setContent {
            RabobankAssignmentTheme {
                MainLayout()
            }
        }
        listOf("Download").forEach { item ->
            composeTestRule.onNodeWithText(item).assertExists()
        }
    }

    private fun successDispatcher(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when(request.path) {
                    else -> MockResponse().setResponseCode(200).setBody("TODO")
                }
            }
        }
    }
}*/
