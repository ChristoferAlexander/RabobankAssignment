package com.example.rabobankassignment.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rabobankassignment.api.ApiError
import com.example.rabobankassignment.api.ApiException
import com.example.rabobankassignment.api.ApiSuccess
import com.example.rabobankassignment.model.CsvResult
import com.example.rabobankassignment.parser.CsvRecordResult
import com.example.rabobankassignment.parser.CsvSourceConfig
import com.example.rabobankassignment.parser.parseCsv
import com.example.rabobankassignment.repository.CsvRepo
import com.example.rabobankassignment.ui.compose.DisplayRoute
import com.example.rabobankassignment.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: CsvRepo
) : ViewModel(), RouteNavigator by routeNavigator {

    sealed class State {
        object Empty : State()
        object Downloading : State()
    }

    sealed class Effect {
        data class Error(val message: String?) : Effect()
    }

    var state by mutableStateOf<State>(State.Empty)
        private set

    var effects = Channel<Effect>(Channel.UNLIMITED)
        private set

    fun startDownload() {
        viewModelScope.launch {
            state = State.Downloading
            when (val result = repository.invoke()) {
                is ApiError -> {
                    state = State.Empty
                    effects.send(Effect.Error(result.message))
                }
                is ApiException -> {
                    state = State.Empty
                    effects.send(Effect.Error(result.error.localizedMessage))
                }
                is ApiSuccess -> {
                    val scvTable = parseCsv(CsvSourceConfig(result.data.byteStream()))
                    // TODO handle failed records, maybe show the failed record in an error line
                    navigateToRoute(
                        DisplayRoute.getWithArgs(
                            CsvResult(
                                columns = scvTable.header.columnNames,
                                records = scvTable.csvRecordResults.filterIsInstance<CsvRecordResult.Success>()
                                    .map { it.record }
                                /*.map {
                                        it.record.elements.map {
                                            when (it) {
                                                is IntValue -> it.value.toString()
                                                is StringValue -> it.value
                                                is DateValue -> it.value.toString()
                                                is UrlValue -> it.value.toString()
                                            }
                                        }
                                    }*/)
                        )
                    )
                }
            }
        }
    }
}

