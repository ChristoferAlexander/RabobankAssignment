package com.example.rabobankassignment.parser

import android.os.Parcelable
import com.example.rabobankassignment.parser.CsvRecordValue.Value.*
import com.example.rabobankassignment.parser.RecordValueType.*
import kotlinx.parcelize.Parcelize

data class CsvTable(val header: CsvHeader?, val csvRecordResults: List<CsvRecordResult>)

data class CsvHeader(val columnNames: List<String>)

@Parcelize
data class CsvRecord(val elements: List<CsvRecordValue>) : Parcelable

@Parcelize
data class CsvRecordValue(private val type: RecordValueType, private val value: String) : Parcelable {

    fun getValue(): Value {
        return when (type) {
            INT -> IntValue(value.toInt())
            STRING -> StringValue(value)
            DATE -> DateValue(value)
            URL -> UrlValue(value)
        }
    }

    sealed class Value : Parcelable {

        @Parcelize
        class IntValue(val intValue: Int) : Value()

        @Parcelize
        class StringValue(val stringValue: String) : Value()

        @Parcelize
        class DateValue(val dateValue: String) : Value()

        @Parcelize
        class UrlValue(val urlValue: String) : Value()
    }
}

enum class RecordValueType {
    INT, STRING, DATE, URL
}

sealed class CsvRecordResult {
    class Success(val record: CsvRecord) : CsvRecordResult()
    class Failure(val line: String, val msg: String) : CsvRecordResult()
}



