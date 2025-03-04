package hu.bme.aut.android.fishing.feature.catches.list_catches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.data.catches.model.Catch
import hu.bme.aut.android.fishing.domain.usecases.auth.AllAuthenticationUseCases
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.ui.model.UiText
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListCatchesViewModel @Inject constructor(
    private val authentication: AllAuthenticationUseCases,
    private val catchesUseCases: AllCatchesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ListCatchesState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListCatchesEvent) {
        when (event) {
            is ListCatchesEvent.GlobalModeChanged -> {
                _state.update { it.copy(isGlobalModeOn = event.switchState) }
                loadCatches()
            }

            is ListCatchesEvent.FloatingActionButtonClicked -> {
                if(!authentication.hasUser()) {
                    viewModelScope.launch {
                        _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.text_not_logged_in_add)))
                    }
                }
            }

            is ListCatchesEvent.SaveCatch -> {
                saveCatch()
            }

            is ListCatchesEvent.ChangeName -> {
                _state.update { it.copy(newCatchName = event.name) }
            }

            is ListCatchesEvent.ChangeWeight -> {
                _state.update { it.copy(newCatchWeight = event.weight) }
            }

            is ListCatchesEvent.ChangeLength -> {
                _state.update { it.copy(newCatchLength = event.length) }
            }
        }
    }

    fun loadCatches() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                checkSignInState()
                if(state.value.isGlobalModeOn) {
                    catchesUseCases.catches().collect {
                        val catches = it.sortedBy { it.time }
                        _state.update { it.copy(isLoading = false, catches = catches) }
                    }
                } else {
                    catchesUseCases.userCatches().collect {
                        val catches = it.sortedBy { it.time }
                        _state.update { it.copy(isLoading = false, catches = catches) }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e, isError = true) }
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    fun checkSignInState() {
        _state.update { it.copy(isLoggedIn = authentication.hasUser()) }
    }

    fun saveCatch() {
        viewModelScope.launch {
            try {
                catchesUseCases.addCatch(
                    Catch(
                        name = state.value.newCatchName,
                        weight = state.value.newCatchWeight,
                        length = state.value.newCatchLength,
                        time = Date(System.currentTimeMillis())
                    )
                )
            } catch (e: Exception) {
                _state.update { it.copy(error = e, isError = true) }
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
        }
    }
}

data class ListCatchesState( //  represents the UI state
    val isGlobalModeOn: Boolean = true,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val catches: List<Catch> = emptyList(),
    val newCatchName: String = "",
    val newCatchWeight: String = "",
    val newCatchLength: String = ""
)

sealed class ListCatchesEvent { // represents user actions (events)

    data class GlobalModeChanged(val switchState: Boolean) : ListCatchesEvent()
    data object FloatingActionButtonClicked : ListCatchesEvent()
    data object SaveCatch: ListCatchesEvent()
    data class ChangeName(val name: String) : ListCatchesEvent()
    data class ChangeWeight(val weight: String) : ListCatchesEvent()
    data class ChangeLength(val length: String) : ListCatchesEvent()
}
}