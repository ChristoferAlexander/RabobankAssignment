package com.example.rabobankassignment.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rabobankassignment.api.ApiError
import com.example.rabobankassignment.api.ApiException
import com.example.rabobankassignment.api.ApiSuccess
import com.example.rabobankassignment.model.CsvNavResult
import com.example.rabobankassignment.parser.CsvRecordResult
import com.example.rabobankassignment.parser.CsvSourceConfig
import com.example.rabobankassignment.parser.parseCsv
import com.example.rabobankassignment.repository.CsvRepoImp
import com.example.rabobankassignment.ui.compose.DisplayRoute
import com.example.rabobankassignment.ui.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val repository: CsvRepoImp
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

    private val effects = Channel<Effect>(Channel.UNLIMITED)
    val effectsFlow = effects.receiveAsFlow()

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
                    // TODO handle failed records, maybe show the failed record in an error line
                    val csvTable = parseCsv(CsvSourceConfig(result.data.byteStream()))
                    navigateToRoute(
                        DisplayRoute.getWithArgs(
                            CsvNavResult(
                                columns = csvTable.header?.columnNames,
                                records = csvTable.csvRecordResults.filterIsInstance<CsvRecordResult.Success>().map { it.record }
                            )
                        )
                    )
                    state = State.Empty
                }
            }
        }
    }
}

