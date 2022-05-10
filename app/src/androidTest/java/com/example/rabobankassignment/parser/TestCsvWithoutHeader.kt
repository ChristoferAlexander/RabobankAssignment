package com.example.rabobankassignment.parser

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rabobankassignment.parser.CsvRecordResult.Success
import com.example.rabobankassignment.parser.CsvRecordValue.Value.*
import com.example.rabobankassignment.toInputStream
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStream

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestCsvWithoutHeader {

    @Test
    fun test_simple_example_without_header() {
        val csvStream: InputStream = """"Theo","Jansen",5,"1978-01-02T00:00:00","https://api.multiavatar.com/2cdf5db9b4dee297b7.png"
"Fiona","de Vries",7,"1950-11-12T00:00:00","https://api.multiavatar.com/b9339cb9e7a833cd5e.png"
"Petra","Boersma",1,"2001-04-20T00:00:00","https://api.multiavatar.com/2672c49d6099f87274.png""""
            .toInputStream()

        val csvTable = parseCsv(CsvSourceConfig(csvStream, hasHeader = false))
        assertEquals(csvTable.header, null)
        assertEquals(csvTable.csvRecordResults.size, 3)

        csvTable.csvRecordResults.forEach { recordResult ->
            assertTrue(recordResult is Success)
        }
        val mappedResult = csvTable.csvRecordResults.map { it as Success }
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
    fun test_simple_example_without_header_with_uneven_columns_in_row() {
        val csvStream: InputStream = """"Theo","Jansen",5,"1978-01-02T00:00:00","https://api.multiavatar.com/2cdf5db9b4dee297b7.png"
"Fiona","de Vries",7,"1950-11-12T00:00:00","https://api.multiavatar.com/b9339cb9e7a833cd5e.png","extra line arg"
"Petra","Boersma",1,"2001-04-20T00:00:00","https://api.multiavatar.com/2672c49d6099f87274.png""""
            .toInputStream()

        val csvTable = parseCsv(CsvSourceConfig(csvStream, hasHeader = false))
        assertEquals(csvTable.header, null)
        assertEquals(csvTable.csvRecordResults.size, 3)

        csvTable.csvRecordResults.forEach { recordResult ->
            assertTrue(recordResult is Success)
        }
        val mappedResult = csvTable.csvRecordResults.map { it as Success }

        assertTrue(mappedResult[0].record.elements[0].getValue() is StringValue)
        assertTrue(mappedResult[0].record.elements[1].getValue() is StringValue)
        assertTrue(mappedResult[0].record.elements[2].getValue() is IntValue)
        assertTrue(mappedResult[0].record.elements[3].getValue() is DateValue)
        assertTrue(mappedResult[0].record.elements[4].getValue() is UrlValue)

        assertTrue(mappedResult[1].record.elements[0].getValue() is StringValue)
        assertTrue(mappedResult[1].record.elements[1].getValue() is StringValue)
        assertTrue(mappedResult[1].record.elements[2].getValue() is IntValue)
        assertTrue(mappedResult[1].record.elements[3].getValue() is DateValue)
        assertTrue(mappedResult[1].record.elements[4].getValue() is UrlValue)
        assertTrue(mappedResult[1].record.elements[5].getValue() is StringValue)

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

        // assert extra line arg
        assertEquals(
            ((mappedResult[1].record.elements[5].getValue() as StringValue).stringValue),
            "extra line arg"
        )
    }
}


