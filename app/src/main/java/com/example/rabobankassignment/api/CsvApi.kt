package com.example.rabobankassignment.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvApi @Inject constructor(private val service: Service) {

    suspend fun getCsvFile(): ApiResult<ResponseBody> = service.getCsvFile()

    interface Service {
        @GET("main/issues.csv")
        suspend fun getCsvFile(): ApiResult<ResponseBody>
    }

    companion object {
        const val API_URL = "https://raw.githubusercontent.com/RabobankDev/AssignmentCSV/"
    }
}
