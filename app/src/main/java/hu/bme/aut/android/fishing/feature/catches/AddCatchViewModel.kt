package hu.bme.aut.android.fishing.feature.catches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.data.catches.model.Catch
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
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
class AddCatchViewModel @Inject constructor(
    private val catchesUseCases: AllCatchesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddCatchState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddCatchEvent) {
        when (event) {
            is AddCatchEvent.SaveFloatingActionButtonClicked -> {
                saveCatch()
            }

            is AddCatchEvent.ChangeName -> {
                _state.update { it.copy(newCatchName = event.name) }
            }

            is AddCatchEvent.ChangeWeight -> {
                _state.update { it.copy(newCatchWeight = event.weight) }
            }

            is AddCatchEvent.ChangeLength -> {
                _state.update { it.copy(newCatchLength = event.length) }
            }
        }
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
}

data class AddCatchState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val newCatchName: String = "",
    val newCatchWeight: String = "",
    val newCatchLength: String = ""
)

sealed class AddCatchEvent {

    data object SaveFloatingActionButtonClicked : AddCatchEvent()
    data class ChangeName(val name: String) : AddCatchEvent()
    data class ChangeWeight(val weight: String) : AddCatchEvent()
    data class ChangeLength(val length: String) : AddCatchEvent()
}