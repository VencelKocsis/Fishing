package hu.bme.aut.android.fishing.feature.catches.create_catch

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.domain.usecases.auth.AllAuthenticationUseCases
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.ui.model.CatchUi
import hu.bme.aut.android.fishing.ui.model.SpeciesUi
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

            is AddCatchEvent.SelectSpecies -> {
                val newValue = event.species
                _state.update { it.copy(
                    catch = it.catch.copy(species = newValue)
                ) }
            }

            is AddCatchEvent.CaptureImage -> {
                val uri = Uri.parse(event.uri)
                _state.update { it.copy(
                    catch = it.catch.copy(imageURL = uri.toString()),  // Ensure URI is stored inside `catch`
                    imageUri = uri  // Also update `imageUri` in state
                ) }
                Log.d("AddCatchViewModel", "Image URI updated: $uri")
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
                val imageUri = state.value.imageUri
                val catchUi = state.value.catch.asCatch()  // Convert to Catch domain model
                Log.d("AddCatchViewModel", "Saving catch with image URI: $imageUri")
                Log.d("AddCatchViewModel", "Catch to save: $catchUi")

                // Upload the image if it exists
                val imageUrl = imageUri?.let { catchesUseCases.uploadImage(it) } ?: ""
                val updatedCatch = catchUi.copy(imageURL = imageUrl)
                Log.d("AddCatchViewModel", "Updated catch with image URL: $imageUrl")

                catchesUseCases.addCatch(updatedCatch, imageUri)  // Pass the updated Catch domain model
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
    val catch: CatchUi = CatchUi(),
    val imageUri: Uri? = null
)

sealed class AddCatchEvent {

    object SaveCatch : AddCatchEvent()
    data class ChangeName(val name: String) : AddCatchEvent()
    data class ChangeWeight(val weight: String) : AddCatchEvent()
    data class ChangeLength(val length: String) : AddCatchEvent()
    data class SelectSpecies(val species: SpeciesUi) : AddCatchEvent()
    data class CaptureImage(val uri: String) : AddCatchEvent()
}