package hu.bme.aut.android.fishing.feature.catches.list_catches

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.icons.Fish
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCatchesScreen(
    onFabClick: () -> Unit,
    viewModel: ListCatchesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.loadCatches()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message?.asString(context) ?: ""
                        )
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
            TopAppBar(title = { stringResource(id = R.string.text_catches) })
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                //onClick = { viewModel.onEvent(ListCatchesEvent.FloatingActionButtonClicked) },
                onClick = onFabClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SingleChoiceSegmentedButtonRow{
                SegmentedButton(
                    selected = !state.isGlobalModeOn,
                    onClick = { viewModel.onEvent(ListCatchesEvent.GlobalModeChanged(false)) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = {}
                ) {
                    Text(text = stringResource(id = R.string.text_own))
                }
                SegmentedButton(
                    selected = state.isGlobalModeOn,
                    onClick = { viewModel.onEvent(ListCatchesEvent.GlobalModeChanged(true)) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    icon = {}
                ) {
                    Text(text = stringResource(id = R.string.text_global))
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if(!state.isGlobalModeOn && !state.isLoggedIn) {
                    Text(text = stringResource(id = R.string.text_not_logged_in_see))
                } else {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )
                    } else if (state.isError) {
                        Text(
                            text = state.error?.toUiText()?.asString(context)
                                ?: stringResource(id = R.string.some_error_message)
                        )
                    } else {
                        if (state.catches.isEmpty()) {
                            Text(text = stringResource(id = R.string.text_no_catches))
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(5.dp))
                            ) {
                                items(state.catches.size) { i ->
                                    ListItem(
                                        leadingContent = {
                                            Icon(
                                                imageVector = Fish,
                                                contentDescription = null
                                            )
                                        },
                                        headlineContent = {
                                            Text(text = state.catches[i].name)
                                        },
                                        supportingContent = {
                                            val dateFormat: DateFormat =
                                                SimpleDateFormat("yyyy.MM.dd hh:mm:ss")
                                            val strDate: String =
                                                dateFormat.format(state.catches[i].time)
                                            Text(
                                                text = strDate
                                            )
                                        }
                                    )
                                    if (i < state.catches.size - 1) {
                                        HorizontalDivider(
                                            thickness = 2.dp,
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}























