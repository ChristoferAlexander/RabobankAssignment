package com.example.rabobankassignment.module

import com.example.rabobankassignment.di.ApiModule
import com.example.rabobankassignment.module.MockServer.Companion
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class]
)
class TestApiModule: ApiModule() {

    override fun baseUrl(): HttpUrl {
        return Companion.server.url("http://localhost/")
    }
}

class MockServer {
    companion object {
        val server = MockWebServer()
    }
}