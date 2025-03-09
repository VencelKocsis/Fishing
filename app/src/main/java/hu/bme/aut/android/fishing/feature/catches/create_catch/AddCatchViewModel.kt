package hu.bme.aut.android.fishing.feature.catches.create_catch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.domain.usecases.auth.AllAuthenticationUseCases
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.ui.model.CatchUi
import hu.bme.aut.android.fishing.ui.model.asCatch
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCatchViewModel @Inject constructor(
    private val authentication: AllAuthenticationUseCases,
    private val catchesUseCases: AllCatchesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(AddCatchState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddCatchEvent) {
        when (event) {
            is AddCatchEvent.ChangeName -> {
                val newValue = event.name
                _state.update { it.copy(
                    catch = it.catch.copy(name = newValue)
                ) }
            }

            is AddCatchEvent.ChangeWeight -> {
                val newValue = event.weight
                _state.update { it.copy(
                    catch = it.catch.copy(weight = newValue)
                ) }
            }

            is AddCatchEvent.ChangeLength -> {
                val newValue = event.length
                _state.update { it.copy(
                    catch = it.catch.copy(length = newValue)
                ) }
            }

            AddCatchEvent.SaveCatch -> {
                val userId = authentication.currentUserId()
                _state.update { it.copy(
                    catch = it.catch.copy(userId = userId ?: "UNKNOWN_USER")
                ) }
                saveCatch()
            }
        }
    }

    fun saveCatch() {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    catchesUseCases.addCatch(state.value.catch.asCatch())
                }
                _uiEvent.send(UiEvent.Success())
            } catch (e: Exception) {
                _state.update { it.copy(error = e, isError = true) }
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}

data class AddCatchState(
    val error: Throwable? = null,
    val isError: Boolean = error != null,
    val catch: CatchUi = CatchUi()
)

sealed class AddCatchEvent {

    object SaveCatch : AddCatchEvent()
    data class ChangeName(val name: String) : AddCatchEvent()
    data class ChangeWeight(val weight: String) : AddCatchEvent()
    data class ChangeLength(val length: String) : AddCatchEvent()
}