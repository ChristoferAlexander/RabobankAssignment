package com.example.rabobankassignment.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.rabobankassignment.parser.CsvRecord
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class CsvResult(val columns: List<String>, val records: List<CsvRecord>) : Parcelable

class CsvResultNavType : NavType<CsvResult>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): CsvResult? =
        bundle.getParcelable(key)

    override fun parseValue(value: String): CsvResult =
        Gson().fromJson(value, CsvResult::class.java)


    override fun put(bundle: Bundle, key: String, value: CsvResult) {
        bundle.putParcelable(key, value)
    }
}