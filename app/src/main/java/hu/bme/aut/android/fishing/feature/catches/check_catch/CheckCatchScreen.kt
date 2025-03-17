package hu.bme.aut.android.fishing.feature.catches.check_catch

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.common.CatchAppBar
import hu.bme.aut.android.fishing.ui.common.CatchEditor
import hu.bme.aut.android.fishing.ui.common.CatchImagePicker
import hu.bme.aut.android.fishing.ui.model.CatchImageStateUi
import hu.bme.aut.android.fishing.ui.model.CatchUi
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun CheckCatchScreen(
    onNavigateBack: () -> Unit,
    viewModel: CheckCatchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> {
                    onNavigateBack()
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        hostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState) },
        topBar = {
            if (!state.isLoadingCatch) {
                CatchAppBar(
                    title = if (state.isEditingCatch) {
                        stringResource(id = R.string.editing_catch)
                    } else state.catch?.name ?: "Fogás",
                    onNavigateBack = onNavigateBack,
                    actions = {
                        IconButton(
                            onClick = {
                                if (state.isEditingCatch) {
                                    viewModel.onEvent(CheckCatchEvent.StopEditingCatch)
                                } else {
                                    viewModel.onEvent(CheckCatchEvent.EditingCatch)
                                }
                            }
                        )  {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                        IconButton(
                            onClick = {
                                viewModel.onEvent(CheckCatchEvent.DeleteCatch)
                            }
                        )  {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (state.isEditingCatch) {
                LargeFloatingActionButton(
                    onClick = {
                        viewModel.onEvent(CheckCatchEvent.UpdateCatch)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoadingCatch) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            } else {
                val catch = state.catch ?: CatchUi()
                CatchEditor(
                    nameValue = catch.name,
                    nameValueChange = { viewModel.onEvent(CheckCatchEvent.ChangeName(it)) },
                    weightValue = catch.weight,
                    weightValueChange = { viewModel.onEvent(CheckCatchEvent.ChangeWeight(it)) },
                    lengthValue = catch.length,
                    lengthValueChange = { viewModel.onEvent(CheckCatchEvent.ChangeLength(it)) },
                    selectedSpecies = catch.species,
                    onSpeciesSelected = { viewModel.onEvent(CheckCatchEvent.SelectSpecies(it)) },
                    modifier = Modifier,
                    enabled = state.isEditingCatch
                )

                CatchImagePicker(
                    imageState = state.CatchImageStateUi(),
                    isEditing = state.isEditingCatch,
                    onImageSelected = { uri ->
                        viewModel.onEvent(CheckCatchEvent.SelectNewImage(uri))
                    },
                    onImageDeleted = {
                        viewModel.onEvent(CheckCatchEvent.DeleteImage(state.catch?.imageUri ?: ""))
                    }
                )
            }
        }
    }
}

















