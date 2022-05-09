package com.example.rabobankassignment.ui.compose

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rabobankassignment.R
import com.example.rabobankassignment.model.CsvNavResult
import com.example.rabobankassignment.model.CsvResultNavType
import com.example.rabobankassignment.parser.CsvRecord
import com.example.rabobankassignment.parser.CsvRecordValue
import com.example.rabobankassignment.parser.CsvRecordValue.Value.*
import com.example.rabobankassignment.parser.RecordValueType
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
     * TODO improve to more generalized approach
     */
    fun getWithArgs(data: CsvNavResult): String {
        return route.replace(
            "{$KEY_DISPLAY_DATA}",
            Uri.encode(Gson().toJson(data))
        )
    }

    @Throws(IllegalArgumentException::class)
    fun getData(savedStateHandle: SavedStateHandle) =
        savedStateHandle.getOrThrow<CsvNavResult>(KEY_DISPLAY_DATA)

    override fun getArguments(): List<NamedNavArgument> =
        listOf(navArgument(KEY_DISPLAY_DATA) { type = CsvResultNavType() })

    @Composable
    override fun viewModel(): DisplayViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: DisplayViewModel) = LoadedScreen(viewModel)
}

@Composable
fun LoadedScreen(viewModel: DisplayViewModel) {
    BackHandler {
       viewModel.navigateUp()
    }
    LoadedLayout(viewModel.data)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoadedLayout(data: CsvNavResult) {
    // Not the best choice as it can not scroll horizontally
    // lots of limitations on compose side for grid layouts
    // needs a custom implementation on LazyRow and LazyColumn so it can show items without header
    // right now it will do a best effort attempt
    LazyVerticalGrid(
        cells = data.columns?.let { GridCells.Fixed(it.size) } ?: GridCells.Adaptive(dimensionResource(R.dimen.min_record_height)),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_tiny)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_tiny)),
        modifier = Modifier.background(Color.Black)
    ) {
        data.columns?.let {
            items(it) { header ->
                Box(modifier = Modifier.heightIn(dimensionResource(R.dimen.min_header_height)).background(Color.White)) {
                    HeaderGridItem(header)
                }
            }
        }
        items(data.records.map { it.elements }.flatten()) { record ->
            Box(modifier = Modifier.heightIn(dimensionResource(R.dimen.min_record_height)).background(Color.White)) {
                RecordGridItem(record)
            }
        }
    }
}

@Composable
fun BoxScope.HeaderGridItem(title: String) {
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = title,
        style = Typography.headerTextStyle
    )
}

@Composable
fun BoxScope.RecordGridItem(csvRecordValue: CsvRecordValue, modifier: Modifier = Modifier) {
    when (val value = csvRecordValue.getValue()) {
        is DateValue -> DateValueItem(value.dateValue, modifier)
        is IntValue -> IntValueItem(value.intValue, modifier)
        is StringValue -> StringValueItem(value.stringValue, modifier)
        is UrlValue -> AvatarValueItem(thumbnailUrl = value.urlValue, modifier)
    }
}

@Composable
fun BoxScope.StringValueItem(value: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.align(Alignment.Center),
        text = value,
        style = Typography.stringTextStyle
    )
}

@Composable
fun BoxScope.IntValueItem(value: Int, modifier: Modifier) {
    Text(
        modifier = modifier.align(Alignment.Center),
        text = value.toString(),
        style = Typography.intTextStyle
    )
}

@Composable
fun BoxScope.DateValueItem(date: String, modifier: Modifier) {
    Text(
        modifier = modifier.align(Alignment.Center),
        text = date,
        style = Typography.dateTextStyle
    )
}

@Composable
fun BoxScope.AvatarValueItem(thumbnailUrl: String, modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnailUrl)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "Avatar item thumbnail picture",
        contentScale = ContentScale.Fit,
        modifier = modifier.size(dimensionResource(R.dimen.min_record_height)).padding(dimensionResource(R.dimen.padding_tiny)).align(Alignment.Center)
    )
}

@Preview
@Composable
fun LoadedScreenPreview() {
    val dummyData = CsvNavResult(
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
    LoadedLayout(dummyData)
}