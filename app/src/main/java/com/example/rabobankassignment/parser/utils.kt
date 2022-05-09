package com.example.rabobankassignment.parser

import android.net.Uri
import com.example.rabobankassignment.api.isValidUrl
import com.example.rabobankassignment.parser.CsvRecordResult.Failure
import com.example.rabobankassignment.parser.CsvRecordResult.Success
import com.example.rabobankassignment.parser.RecordValueType.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun parseCsv(sourceConfig: CsvSourceConfig): CsvTable {
    sourceConfig.bufferedReader().useLines { lines: Sequence<String> ->
        val lineIterator = lines.iterator()
        val splitByComma = createLineSplitter()
        val trimmedHeaderLine = removeBomChars(lineIterator.next())
        val csvHeader = CsvHeader(trimmedHeaderLine.splitByComma())
        // TODO recursive join lines that fail parsing in case the contain extra "\n" in a records
        val records: List<List<String>> = lineIterator.asSequence().mapNotNull { line ->
            val trimmedLine = removeBomChars(line)
            if (trimmedLine.isBlank()) return@mapNotNull null
            trimmedLine.splitByComma()
        }.toList()
        return CsvTable(
            csvHeader,
            parseRecords(records, csvHeader)
        )
    }
}

/**
 * @return [List<CsvRecordResult>]
 *
 * [CsvRecordResult.Success] if parsing is successful containing the parsed [CsvRecord] from the [line]
 * [CsvRecordResult.Failure] if parsing fails
 */
private fun parseRecords(
    line: List<List<String>>,
    csvHeader: CsvHeader
): List<CsvRecordResult> {
    val expectedNumberOfElements = csvHeader.columnNames.size
    return line.map { elements ->
        val actualSize = elements.size
        if (expectedNumberOfElements != actualSize) {
            Failure(
                line = elements.toString(),
                msg = "Line doesn't contain as many arguments as expected! expected: $expectedNumberOfElements, actual: $actualSize"
            )
        } else {
            val recordValues = elements.map { element ->
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val dateElement = try {
                    LocalDate.parse(element, formatter)
                } catch (e: Exception) {
                    println(e)
                    null
                }
                val uriElement = if (element.isValidUrl()) Uri.parse(element) else null
                val intElement = element.toIntOrNull()
                when {
                    uriElement != null -> CsvRecordValue(URL, element)
                    intElement != null -> CsvRecordValue(INT, element)
                    dateElement != null -> {
                        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy")
                        CsvRecordValue(DATE, dateElement.format(outputFormatter))
                    }
                    else -> CsvRecordValue(STRING, element)
                }
            }
            Success(CsvRecord(recordValues))
        }
    }
}

private fun removeBomChars(s: String) = s.trimStart('\uFEFF', '\u200B')

/**
 * Parses a csv line
 */
private fun createLineSplitter(
    separatorChar: Char = ',',
    quoteChar: Char = '"'
): String.() -> List<String> = {
    val line = this
    var numberOfDoubleQuotesEncountered = 0
    val separatorIndexes = mutableListOf(0)
    line.forEachIndexed { index, char ->
        when (char) {
            quoteChar -> {
                numberOfDoubleQuotesEncountered++
            }
            separatorChar -> {
                if ((numberOfDoubleQuotesEncountered % 2) == 0) {
                    separatorIndexes.add(index)
                }
            }
        }
    }
    // add en extra separator char at end of line
    separatorIndexes.add(line.length)
    mutableListOf<String>().apply {
        if (separatorIndexes.size == 2) {
            add(line)
        } else {
            val commaIndexIterator = separatorIndexes.iterator()
            var startIndex = commaIndexIterator.next()
            // will run at least once as we add an extra separator char at the end of the line
            while (commaIndexIterator.hasNext()) {
                val endIndex = commaIndexIterator.next()
                add(line.substring(startIndex, endIndex))
                startIndex = endIndex + 1
            }
        }
    }.map { it.trimThenTrimQuotesThenTrim(quoteChar) }.map { it.quotedQuotesToQuotes(quoteChar) }
}

/**
 * Trims leading spaces (inside and outside quotes) and quotes Ex: [ " ... " ] to [...]
 */
private fun String.trimThenTrimQuotesThenTrim(quoteChar: Char): String = this
    .trim()
    .removeSurrounding(quoteChar.toString())
    .trim()

/**
 * Replaces quotes quotes with quotes
 * Ex: [Some ""quoted"" text] to [Some "quoted" text]
 *
 * Note: each of the embedded double-quote characters must be represented by a pair of double-quote characters.
 */
private fun String.quotedQuotesToQuotes(quoteChar: Char): String =
    this.replace(quoteChar.toString() + quoteChar.toString(), quoteChar.toString())

