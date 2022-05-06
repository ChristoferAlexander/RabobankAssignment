package com.example.rabobankassignment.parser

fun parseCsv(sourceConfig: CsvSourceConfig): CsvTable {
    sourceConfig.bufferedReader().useLines { lines: Sequence<String> ->
        val lineIterator = lines.iterator()
        val csvHeader = CsvHeader(
            line = lineIterator.next(),
            separator = sourceConfig.separatorChar
        )
        val csvRecordParsingResults = lineIterator.asSequence().mapNotNull { line ->
            return@mapNotNull CsvRecordResult.buildFrom(
                line = line,
                expectedNumberOfElements = csvHeader.numberOrColumns,
                separator = sourceConfig.separatorChar
            )
        }
        return CsvTable(
            csvHeader,
            csvRecordParsingResults.toList()
        )
    }
}

