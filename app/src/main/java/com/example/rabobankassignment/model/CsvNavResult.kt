package com.example.rabobankassignment.model

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.example.rabobankassignment.parser.CsvRecord
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class CsvNavResult(val columns: List<String>?, val records: List<CsvRecord>) : Parcelable

class CsvResultNavType : NavType<CsvNavResult>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): CsvNavResult? =
        bundle.getParcelable(key)

    override fun parseValue(value: String): CsvNavResult =
        Gson().fromJson(value, CsvNavResult::class.java)


    override fun put(bundle: Bundle, key: String, value: CsvNavResult) {
        bundle.putParcelable(key, value)
    }
}