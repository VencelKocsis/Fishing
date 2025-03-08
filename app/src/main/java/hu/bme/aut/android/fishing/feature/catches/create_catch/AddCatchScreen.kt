package hu.bme.aut.android.fishing.feature.catches.create_catch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.common.CatchAppBar
import hu.bme.aut.android.fishing.ui.common.CatchEditor
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
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CatchEditor(
                    nameValue = state.catch.name,
                    nameValueChange = { viewModel.onEvent(AddCatchEvent.ChangeName(it)) },
                    weightValue = state.catch.weight,
                    weightValueChange = { viewModel.onEvent(AddCatchEvent.ChangeWeight(it)) },
                    lengthValue = state.catch.length,
                    lengthValueChange = { viewModel.onEvent(AddCatchEvent.ChangeLength(it)) },
                    modifier = Modifier
                )
            }
        }
    }
}














