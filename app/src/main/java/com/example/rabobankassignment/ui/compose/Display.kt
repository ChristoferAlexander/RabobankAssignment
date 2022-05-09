@file:OptIn(ExperimentalFoundationApi::class)

package com.example.rabobankassignment.ui.compose

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rabobankassignment.R
import com.example.rabobankassignment.model.CsvResult
import com.example.rabobankassignment.model.CsvResultNavType
import com.example.rabobankassignment.parser.CsvRecord
import com.example.rabobankassignment.parser.CsvRecordValue
import com.example.rabobankassignment.parser.RecordValueType
import com.example.rabobankassignment.parser.Value.*
import com.example.rabobankassignment.ui.nav.NavRoute
import com.example.rabobankassignment.ui.nav.getOrThrow
import com.example.rabobankassignment.ui.theme.*
import com.example.rabobankassignment.viewModel.DisplayViewModel
import com.google.gson.Gson


const val KEY_DISPLAY_DATA = "DISPLAY_DATA"

object DisplayRoute : NavRoute<DisplayViewModel> {

    override val route = "display/{$KEY_DISPLAY_DATA}"

    /**
     * Call this to get navigation path to [DisplayRoute] with args
     *
     * TODO improve to more generalized
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
        listOf(navArgument(KEY_DISPLAY_DATA) { type = CsvResultNavType() })

    @Composable
    override fun viewModel(): DisplayViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: DisplayViewModel) = LoadedScreen(viewModel.data)
}

@Composable
fun LoadedScreen(data: CsvResult) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(data.columns.size),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(data.columns) { header ->
            HeaderGridItem(header)
        }
        items(data.records.map { it.elements }.flatten()) { record ->
            RecordGridItem(record)
        }
    }
}

@Composable
fun HeaderGridItem(title: String) {
    Text(
        text = title,
        style = Typography.headerTextStyle
    )
}

@Composable
fun RecordGridItem(csvRecordValue: CsvRecordValue) {
    when (val value = csvRecordValue.getValue()) {
        is DateValue -> DateValueItem(value.dateValue)
        is IntValue -> IntValueItem(value.intValue)
        is StringValue -> StringValueItem(value.stringValue)
        is UrlValue -> AvatarValueItem(thumbnailUrl = value.urlValue)
    }
}

@Composable
fun StringValueItem(value: String) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = value,
        style = Typography.stringTextStyle
    )
}

@Composable
fun IntValueItem(value: Int) {
    Text(
        modifier = Companion.fillMaxSize(),
        text = value.toString(),
        style = Typography.intTextStyle
    )
}

@Composable
fun DateValueItem(date: String) {
    Text(
        modifier = Companion.fillMaxSize(),
        text = date,
        style = Typography.dateTextStyle
    )
}

@Composable
fun AvatarValueItem(thumbnailUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnailUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "Avatar item thumbnail picture",
        contentScale = ContentScale.Fit,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview
@Composable
fun LoadedScreenPreview() {
    val dummyData = CsvResult(
        columns = listOf("Column 1", "Long column 2", "Column 3", "Long column 4", "Long column 5"),
        records = listOf(
            CsvRecord(
                listOf(
                    CsvRecordValue(RecordValueType.STRING, "A string"),
                    CsvRecordValue(RecordValueType.STRING, "Another string"),
                    CsvRecordValue(RecordValueType.INT, "54"),
                    CsvRecordValue(RecordValueType.DATE, "18/10/1992"),
                    CsvRecordValue(RecordValueType.URL, "http//:....")
                )
            )
        )
    )
    LoadedScreen(dummyData)
}