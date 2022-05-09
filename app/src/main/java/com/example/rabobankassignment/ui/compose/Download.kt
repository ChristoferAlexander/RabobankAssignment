package com.example.rabobankassignment.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rabobankassignment.R.string
import com.example.rabobankassignment.ui.nav.NavRoute
import com.example.rabobankassignment.viewModel.DownloadViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DownloadRoute : NavRoute<DownloadViewModel> {

    override val route = "download/"

    @Composable
    override fun viewModel(): DownloadViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: DownloadViewModel) =
        DownloadScreen(
            startDownload = viewModel::startDownload,
            state = viewModel.state,
            effectFlow = viewModel.effectsFlow,
        )
}

@Composable
fun DownloadScreen(
    startDownload: () -> Unit,
    state: DownloadViewModel.State,
    effectFlow: Flow<DownloadViewModel.Effect>,
) {
    ErrorDialogLayout(effectFlow)
    Box(Modifier.fillMaxSize()) {
        when (state) {
            DownloadViewModel.State.Downloading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            DownloadViewModel.State.Empty -> TextButton(
                modifier = Modifier.align(Alignment.Center),
                onClick = startDownload
            ) {
                Text(stringResource(string.btn_download))
            }
        }
    }
}

@Composable
fun ErrorDialogLayout(effectFlow: Flow<DownloadViewModel.Effect>) {
    val openDialog = remember { mutableStateOf(false) }
    val initialErrorState = stringResource(string.error_unknown)
    val dialogError = remember { mutableStateOf(initialErrorState) }

    // Listen for side effects from the VM
    LaunchedEffect(effectFlow) {
        effectFlow.collect { effect ->
            when (effect) {
                is DownloadViewModel.Effect.Error -> {
                    dialogError.value = effect.message.toString()
                    openDialog.value = true
                }
            }
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = stringResource(string.dialog_title_error))
            },
            text = {
                Text(dialogError.value)
            },
            confirmButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text(stringResource(string.btn_ok))
                }
            }
        )
    }
}

@Preview
@Composable
fun DownloadScreenEmptyPreview() {
    DownloadScreen({}, DownloadViewModel.State.Empty, flowOf())
}

@Preview
@Composable
fun DownloadScreenDownloadingPreview() {
    DownloadScreen({}, DownloadViewModel.State.Downloading, flowOf())
}

@Preview
@Composable
fun DownloadScreenErrorPreview() {
    DownloadScreen({}, DownloadViewModel.State.Empty, flowOf(DownloadViewModel.Effect.Error("Some error")))
}