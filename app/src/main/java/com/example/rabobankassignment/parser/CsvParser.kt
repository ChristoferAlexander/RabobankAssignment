package com.example.rabobankassignment.parser

import android.net.Uri
import android.os.Parcelable
import android.util.Patterns
import com.example.rabobankassignment.parser.RecordValueType.*
import com.example.rabobankassignment.parser.Value.*
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class CsvTable(val header: CsvHeader, val csvRecordResults: List<CsvRecordResult>)

class CsvHeader(val line: String, val separator: Char) {
    val columnNames: List<String> = line.replace("\"", "").split(separator)
    val numberOrColumns: Int = columnNames.size
}

@Parcelize
class CsvRecord(val elements: List<CsvRecordValue>) : Parcelable

@Parcelize
data class CsvRecordValue(val type: RecordValueType, val value: String) : Parcelable {
    fun getValue(): Value {
        return when (type) {
            INT -> IntValue(value.toInt())
            STRING -> StringValue(value)
            DATE -> DateValue(value)
            URL -> UrlValue(value)
        }
    }
}

enum class RecordValueType {
    INT, STRING, DATE, URL
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

sealed class CsvRecordResult {
    class Success(val record: CsvRecord) : CsvRecordResult()
    class Failure(val line: String, val msg: String) : CsvRecordResult()

    companion object {

        /**
         * @return [CsvRecordResult.Success] if parsing is successful containing the parsed [CsvRecord] from the [line]
         * @return [CsvRecordResult.Failure] if parsing fails
         */
        fun buildFrom(
            line: String,
            expectedNumberOfElements: Int,
            separator: Char
        ): CsvRecordResult {
            val elements: List<String> = line.replace("\"", "").split(separator)
            val actualSize = elements.size
            return if (expectedNumberOfElements != actualSize) {
                Failure(
                    line = line,
                    msg = "Line doesn't contain as many arguments as expected! expected: $expectedNumberOfElements, actual: $actualSize"
                )
            } else {
                val recordValues = elements.map { element ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                    val dateElement = try {
                        LocalDate.parse(element, formatter)
                    } catch (e: Exception) {
                        null
                    }
                    val uriElement = if (element.isValidUrl()) Uri.parse(element) else null
                    val intElement = element.toIntOrNull()
                    when {
                        uriElement != null -> CsvRecordValue(URL, element)
                        intElement != null -> CsvRecordValue(INT, element)
                        dateElement != null -> CsvRecordValue(DATE, element)
                        else -> CsvRecordValue(STRING, element)
                    }
                }
                Success(CsvRecord(recordValues))
            }
        }
    }
}

fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

