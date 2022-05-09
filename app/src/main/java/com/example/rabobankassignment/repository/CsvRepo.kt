package com.example.rabobankassignment.repository

import com.example.rabobankassignment.api.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CsvRepo @Inject constructor(private val csvApi: CsvApi) {
    suspend fun invoke(): ApiResult<ResponseBody> = withContext(Dispatchers.IO) { csvApi.getCsvFile() }
}