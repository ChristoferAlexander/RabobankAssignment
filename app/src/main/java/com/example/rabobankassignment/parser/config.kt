package com.example.rabobankassignment.parser

import java.io.InputStream
import java.nio.charset.Charset

data class CsvSourceConfig (
    val stream: InputStream,
    val charset: Charset = Charsets.UTF_8,
    val separatorChar: Char = ',',
    val quoteChar: Char = '"'
)

fun CsvSourceConfig.bufferedReader() = stream.bufferedReader(charset)
