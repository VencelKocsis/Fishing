package hu.bme.aut.android.fishing.feature.catches.create_catch

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.common.CatchAppBar
import hu.bme.aut.android.fishing.ui.common.CatchEditor
import hu.bme.aut.android.fishing.ui.common.CatchImagePicker
import hu.bme.aut.android.fishing.ui.model.toImageStateUi
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddCatchScreen(
    viewModel: AddCatchViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showGallery by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(state.imageUri) }

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        onNavigateBack()
                    }
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CatchAppBar(
                title = stringResource(id = R.string.text_add_catch),
                onNavigateBack = onNavigateBack,
                actions = {}
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.onEvent(AddCatchEvent.SaveCatch) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CatchEditor(
                nameValue = state.catch.name,
                nameValueChange = { viewModel.onEvent(AddCatchEvent.ChangeName(it)) },
                weightValue = state.catch.weight,
                weightValueChange = { viewModel.onEvent(AddCatchEvent.ChangeWeight(it)) },
                lengthValue = state.catch.length,
                lengthValueChange = { viewModel.onEvent(AddCatchEvent.ChangeLength(it)) },
                selectedSpecies = state.catch.species,
                onSpeciesSelected = { viewModel.onEvent(AddCatchEvent.SelectSpecies(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )

            CatchImagePicker(
                imageState = state.toImageStateUi(),
                isEditing = true,
                onImageSelected = { uri ->
                    uri?.let { viewModel.onEvent(AddCatchEvent.CaptureImage(it.toString())) }
                },
                onImageDeleted = {
                    viewModel.onEvent(AddCatchEvent.DeleteImage(""))
                }
            )

            if (state.uploadProgress > 0f && state.uploadProgress < 1f) {
                LinearProgressIndicator(
                    progress = state.uploadProgress,
                    modifier = Modifier
                        .fillMaxWidth().padding(16.dp)
                )
            }
        }
    }
}