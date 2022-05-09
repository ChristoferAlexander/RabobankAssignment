package com.example.rabobankassignment.parser

import java.io.InputStream
import java.nio.charset.Charset

/**
 * Contains CVS as [InputStream] and CSV parsing options
 */
data class CsvSourceConfig (
    val stream: InputStream,
    val charset: Charset = Charsets.UTF_8,
    val separatorChar: Char = ',',
    val hasHeader:Boolean = true,
    val quoteChar: Char = '"',
    val dateFormatter: CsvDateFormatter = CsvDateFormatter()
)

data class CsvDateFormatter(val inputFormat: String = INPUT_DATE_FORMAT, val outputFormat: String = OUTPUT_DATE_FORMAT)

fun CsvSourceConfig.bufferedReader() = stream.bufferedReader(charset)
