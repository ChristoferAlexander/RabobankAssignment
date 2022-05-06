@file:OptIn(ExperimentalFoundationApi::class)

package com.example.rabobankassignment.ui.compose

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import com.example.rabobankassignment.model.CsvResult
import com.example.rabobankassignment.model.CsvResultParamType
import com.example.rabobankassignment.parser.CsvRecordValue
import com.example.rabobankassignment.parser.Value.*
import com.example.rabobankassignment.ui.nav.NavRoute
import com.example.rabobankassignment.ui.nav.getOrThrow
import com.example.rabobankassignment.viewModel.DisplayViewModel
import com.google.gson.Gson


const val KEY_DISPLAY_DATA = "DISPLAY_DATA"

object DisplayRoute : NavRoute<DisplayViewModel> {

    override val route = "display/{$KEY_DISPLAY_DATA}"

    /**
     * Call this to get navigation path to [DisplayRoute] with args
     *
     * NOTE: Not a very clean way to handle nav args TODO improve
     */
    fun getWithArgs(data: CsvResult): String {
        return route.replace(
            "{$KEY_DISPLAY_DATA}",
            Uri.encode(Gson().toJson(data))
        )
    }

    fun getData(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<CsvResult>(KEY_DISPLAY_DATA)

    override fun getArguments(): List<NamedNavArgument> =
        listOf(navArgument(KEY_DISPLAY_DATA) { type = CsvResultParamType() })

    @Composable
    override fun viewModel(): DisplayViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: DisplayViewModel) = LoadedScreen(viewModel.data)
}

@Composable
fun LoadedScreen(data: CsvResult) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(data.columns.size),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(data.columns) {
            HeaderGridItem(it)
        }
        items(data.records.map { it.elements }.flatten()) { record ->
            RecordGridItem(record)
        }
    }
}

@Composable
fun HeaderGridItem(title: String) {
    Text(
        modifier = Companion.fillMaxHeight(),
        text = title,
        fontWeight = FontWeight.Bold,
        style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
    )
}

@Composable
fun RecordGridItem(csvRecordValue: CsvRecordValue) {
    when (csvRecordValue.getValue()) {
        is DateValue -> Text(
            text = csvRecordValue.value
        )
        is IntValue -> Text(
            color = Color.Blue,
            text = csvRecordValue.value
        )
        is StringValue -> StringValueItem(csvRecordValue.value)
        is UrlValue -> Text(
            text = csvRecordValue.value
        )
    }
}

@Composable
fun StringValueItem(value: String) {
    Text(
        text = value,
        style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
    )
}

@Preview
@Composable
fun LoadedScreenPreview() {
    /*val dummyData = CsvResult(
        columns = listOf(
            ColumnEntry("First name", 1f),
            ColumnEntry("Surname", 1f),
            ColumnEntry("Issue count", 1f),
            ColumnEntry("Date of birth", 1f),
            ColumnEntry("Avatar", 1f)
        ),
        users = listOf(
            CsvRecord(
                name = "John",
                surName = "Doe",
                issueCount = 4,
                dateOfBirth = Date(),
                avatarUrl = "http//"
            ),
            CsvRecord(
                name = "Alex",
                surName = "Boutakidis",
                issueCount = 4,
                dateOfBirth = Date(),
                avatarUrl = "http//"
            )
        )
    )*/
    // LoadedScreen(dummyData)
}