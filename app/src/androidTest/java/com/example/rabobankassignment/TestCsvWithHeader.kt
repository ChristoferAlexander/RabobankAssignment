package com.example.rabobankassignment

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rabobankassignment.parser.CsvRecordResult
import com.example.rabobankassignment.parser.CsvRecordValue.Value.*
import com.example.rabobankassignment.parser.CsvSourceConfig
import com.example.rabobankassignment.parser.parseCsv
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStream

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestCsvWithHeader {

    @Test
    fun test_simple_example() {
        val csvStream: InputStream = """"First name","Sur name","Issue count","Date of birth","avatar"
"Theo","Jansen",5,"1978-01-02T00:00:00","https://api.multiavatar.com/2cdf5db9b4dee297b7.png"
"Fiona","de Vries",7,"1950-11-12T00:00:00","https://api.multiavatar.com/b9339cb9e7a833cd5e.png"
"Petra","Boersma",1,"2001-04-20T00:00:00","https://api.multiavatar.com/2672c49d6099f87274.png""""
            .toInputStream()

        val csvTable = parseCsv(CsvSourceConfig(csvStream))
        assertEquals(csvTable.header!!.columnNames.size, 5)
        assertEquals(csvTable.csvRecordResults.size, 3)
        assertEquals(csvTable.header!!.columnNames[0], "First name")
        assertEquals(csvTable.header!!.columnNames[1], "Sur name")
        assertEquals(csvTable.header!!.columnNames[2], "Issue count")
        assertEquals(csvTable.header!!.columnNames[3], "Date of birth")
        assertEquals(csvTable.header!!.columnNames[4], "avatar")
        csvTable.csvRecordResults.forEach { recordResult ->
            assertTrue(recordResult is CsvRecordResult.Success)
        }
        val mappedResult = csvTable.csvRecordResults.map { it as CsvRecordResult.Success }
        mappedResult.forEach { record ->
            assertTrue(record.record.elements[0].getValue() is StringValue)
            assertTrue(record.record.elements[1].getValue() is StringValue)
            assertTrue(record.record.elements[2].getValue() is IntValue)
            assertTrue(record.record.elements[3].getValue() is DateValue)
            assertTrue(record.record.elements[4].getValue() is UrlValue)
        }
        assertEquals(((mappedResult[0].record.elements[0].getValue() as StringValue).stringValue), "Theo")
        assertEquals(((mappedResult[1].record.elements[0].getValue() as StringValue).stringValue), "Fiona")
        assertEquals(((mappedResult[2].record.elements[0].getValue() as StringValue).stringValue), "Petra")

        assertEquals(((mappedResult[0].record.elements[1].getValue() as StringValue).stringValue), "Jansen")
        assertEquals(((mappedResult[1].record.elements[1].getValue() as StringValue).stringValue), "de Vries")
        assertEquals(((mappedResult[2].record.elements[1].getValue() as StringValue).stringValue), "Boersma")

        assertEquals(((mappedResult[0].record.elements[2].getValue() as IntValue).intValue), 5)
        assertEquals(((mappedResult[1].record.elements[2].getValue() as IntValue).intValue), 7)
        assertEquals(((mappedResult[2].record.elements[2].getValue() as IntValue).intValue), 1)

        assertEquals(((mappedResult[0].record.elements[3].getValue() as DateValue).dateValue), "02-01-1978")
        assertEquals(((mappedResult[1].record.elements[3].getValue() as DateValue).dateValue), "12-11-1950")
        assertEquals(((mappedResult[2].record.elements[3].getValue() as DateValue).dateValue), "20-04-2001")

        assertEquals(
            ((mappedResult[0].record.elements[4].getValue() as UrlValue).urlValue),
            "https://api.multiavatar.com/2cdf5db9b4dee297b7.png"
        )
        assertEquals(
            ((mappedResult[1].record.elements[4].getValue() as UrlValue).urlValue),
            "https://api.multiavatar.com/b9339cb9e7a833cd5e.png"
        )
        assertEquals(
            ((mappedResult[2].record.elements[4].getValue() as UrlValue).urlValue),
            "https://api.multiavatar.com/2672c49d6099f87274.png"
        )
    }

    @Test
    fun test_record_element_with_comma() {
        val csvStream: InputStream = """"First name","Sur name","Issue count","Date of birth","avatar"
"Petra","Boersma, the great",1,"2001-04-20T00:00:00","https://api.multiavatar.com/2672c49d6099f87274.png""""
            .toInputStream()
        val csvTable = parseCsv(CsvSourceConfig(csvStream))
        val mappedResult = csvTable.csvRecordResults.map { it as CsvRecordResult.Success }
        assertEquals(((mappedResult[0].record.elements[1].getValue() as StringValue).stringValue), "Boersma, the great")
    }

    @Test
    fun test_record_element_with_quotes() {
        val csvStream: InputStream = """"First name","Sur name","Issue count","Date of birth","avatar"
"Petra","Boersma ""the"" great",1,"2001-04-20T00:00:00","https://api.multiavatar.com/2672c49d6099f87274.png""""
            .toInputStream()
        val csvTable = parseCsv(CsvSourceConfig(csvStream))
        val mappedResult = csvTable.csvRecordResults.map { it as CsvRecordResult.Success }
        assertEquals(((mappedResult[0].record.elements[1].getValue() as StringValue).stringValue), "Boersma \"the\" great")
    }

    @Test
    fun test_invalid_num_of_elements_in_record() {
        val csvStream: InputStream = """"First name","Sur name","Issue count","Date of birth","avatar"
"Petra","Boersma",1,"2001-04-20T00:00:00","https://api.multiavatar.com/2672c49d6099f87274.png","extra element""""
            .toInputStream()
        val csvTable = parseCsv(CsvSourceConfig(csvStream))
        val mappedResult = csvTable.csvRecordResults.map { it as CsvRecordResult.Failure }
        assertEquals(mappedResult[0].msg, "Line doesn't contain as many arguments as expected! expected: 5, actual: 6")
    }

    @Test
    fun test_empty_csv() {
        val csvStream: InputStream = "".toInputStream()
        val csvTable = parseCsv(CsvSourceConfig(csvStream))
        val mappedResult = csvTable.csvRecordResults.map { it as CsvRecordResult.Success }
        assertEquals(mappedResult.size, 0)
        assertEquals(csvTable.header!!.columnNames.size, 0)
    }
}


