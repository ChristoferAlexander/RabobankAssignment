package com.example.rabobankassignment.repository

import com.example.rabobankassignment.api.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

interface CsvRepo{
    suspend fun invoke(): ApiResult<ResponseBody>
}

@Singleton
class CsvRepoImp @Inject constructor(private val csvApi: CsvApi) : CsvRepo {
    override suspend fun invoke(): ApiResult<ResponseBody> = withContext(Dispatchers.IO) { csvApi.getCsvFile() }
}