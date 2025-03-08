package hu.bme.aut.android.fishing.feature.catches.check_catch

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.ui.model.CatchUi
import hu.bme.aut.android.fishing.ui.model.UiText
import hu.bme.aut.android.fishing.ui.model.asCatch
import hu.bme.aut.android.fishing.ui.model.asCatchUi
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            }

            CheckCatchEvent.StopEditingCatch -> {
                _state.update {
                    it.copy(
                        isEditingCatch = false
                    )
                }
            }

            is CheckCatchEvent.ChangeName -> {
                val newValue = event.name
                _state.update { it.copy(
                    catch = it.catch?.copy(name = newValue)
                ) }
            }

            is CheckCatchEvent.ChangeWeight -> {
                val newValue = event.weight
                _state.update { it.copy(
                    catch = it.catch?.copy(weight = newValue)
                ) }
            }

            is CheckCatchEvent.ChangeLength -> {
                val newValue = event.length
                _state.update { it.copy(
                    catch = it.catch?.copy(length = newValue)
                ) }
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
        val catchId = checkNotNull<String>(savedState["id"])
        viewModelScope.launch {
            _state.update { it.copy(isLoadingCatch = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val catch = catchesUseCases.getCatchById(catchId)!!.asCatchUi()
                    _state.update { it.copy(isLoadingCatch = false, catch = catch) }
                }
            } catch (e : Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun updateCatch() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    catchesUseCases.updateCatch(state.value.catch!!.asCatch())
                }
                _uiEvent.send(UiEvent.Success())
            } catch (e : Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun deleteCatch() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    catchesUseCases.deleteCatch(state.value.catch!!.id)
                }
                _uiEvent.send(UiEvent.Success())
            } catch (e : Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}

data class CheckCatchState(
    val catch: CatchUi? = null,
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