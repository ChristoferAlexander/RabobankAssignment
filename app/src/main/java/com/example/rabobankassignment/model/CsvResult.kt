package com.example.rabobankassignment.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.rabobankassignment.parser.CsvRecord
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class CsvResult(val columns: List<String>, val records: List<CsvRecord>) : Parcelable

class CsvResultParamType : NavType<CsvResult>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): CsvResult? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): CsvResult {
        val a = Gson().fromJson(value, CsvResult::class.java)
        return a
    }

    override fun put(bundle: Bundle, key: String, value: CsvResult) {
        bundle.putParcelable(key, value)
    }
}