package hu.bme.aut.android.fishing.feature.catches.check_catch

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.data.catches.model.Catch
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.ui.model.UiText
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckCatchViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val catchesUseCases: AllCatchesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(CheckCatchState())
    val state: StateFlow<CheckCatchState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CheckCatchEvent) {
        when (event) {
            CheckCatchEvent.EditingCatch -> {
                _state.update {
                    it.copy(
                        isEditingCatch = true
                    )
                }
                Log.d("CheckCatchViewModel", "Editing catch: ${state.value.catch}")
            }

            CheckCatchEvent.StopEditingCatch -> {
                _state.update {
                    it.copy(
                        isEditingCatch = false
                    )
                }
                Log.d("CheckCatchViewModel", "Stop editing catch: ${state.value.catch}")
            }

            is CheckCatchEvent.ChangeName -> {
                _state.update { it.copy(newCatchName = event.name) }
            }

            is CheckCatchEvent.ChangeWeight -> {
                _state.update { it.copy(newCatchWeight = event.weight) }
            }

            is CheckCatchEvent.ChangeLength -> {
                _state.update { it.copy(newCatchLength = event.length) }
            }

            CheckCatchEvent.UpdateCatch -> {
                updateCatch()
            }

            CheckCatchEvent.DeleteCatch -> {
                deleteCatch()
            }
        }
    }

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingCatch = true) }
            try {
                // TODO pass id from navigation throws error
                Log.d("CheckCatchViewModel", "Loading catch: ${savedState.get<String>("catchId")}")
                val catchId = savedState.get<String>("catchId") ?: throw Exception("No catch id")
                val catch = catchesUseCases.getCatchById(catchId)

                if (catch != null) {
                    _state.update {
                        it.copy(
                            catch = catch,
                            newCatchName = catch.name,
                            newCatchWeight = catch.weight,
                            newCatchLength = catch.length,
                            isLoadingCatch = false
                        )
                    }
                } else {
                    _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.catch_not_found)))
                    _state.update { it.copy(isLoadingCatch = false) }
                }

            } catch (e : Exception) {
                _state.update { it.copy(isLoadingCatch = false, error = e) }
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun updateCatch() {
        viewModelScope.launch {
            try {
                Log.d("CheckCatchViewModel", "Updating catch: ${state.value.catch}")
                val currentCatch = _state.value.catch ?: throw Exception("No catch")

                val updatedCatch = Catch(
                    id = currentCatch.id, // Ensure the ID remains the same
                    name = state.value.newCatchName,
                    weight = state.value.newCatchWeight,
                    length = state.value.newCatchLength,
                    time = currentCatch.time, // Preserve the original time
                    userId = currentCatch.userId // Keep the same user ID
                )

                catchesUseCases.updateCatch(updatedCatch)

                _state.update { it.copy(catch = updatedCatch, isEditingCatch = false) }
                _uiEvent.send(UiEvent.Success())

            } catch (e : Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun deleteCatch() {
        viewModelScope.launch {
            try {
                val catchId = state.value.catch?.id ?: throw Exception("No catch id")
                catchesUseCases.deleteCatch(catchId)
                _uiEvent.send(UiEvent.Success())
            } catch (e : Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}

data class CheckCatchState(
    val newCatchName: String = "",
    val newCatchWeight: String = "",
    val newCatchLength: String = "",
    val catch: Catch? = null,
    val isLoadingCatch: Boolean = false,
    val isEditingCatch: Boolean = false,
    val error: Throwable? = null
)

sealed class CheckCatchEvent {
    object EditingCatch: CheckCatchEvent()
    object StopEditingCatch: CheckCatchEvent()
    data class ChangeName(val name: String): CheckCatchEvent()
    data class ChangeWeight(val weight: String): CheckCatchEvent()
    data class ChangeLength(val length: String): CheckCatchEvent()
    object UpdateCatch: CheckCatchEvent()
    object DeleteCatch: CheckCatchEvent()
}