package hu.bme.aut.android.fishing.feature.catches.check_catch

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.domain.usecases.catches.AllCatchesUseCases
import hu.bme.aut.android.fishing.ui.model.CatchUi
import hu.bme.aut.android.fishing.ui.model.SpeciesUi
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

            is CheckCatchEvent.SelectSpecies -> {
                val newValue = event.species
                _state.update { it.copy(
                    catch = it.catch?.copy(species = newValue)
                ) }
            }

            is CheckCatchEvent.DeleteImage -> {
                deleteImage(imageUri = event.imageUri)
                _state.update { it.copy(
                    catch = it.catch?.copy(imageUri = ""),
                    imageUri = null,
                    isUploadedImage = false
                ) }
            }

            is CheckCatchEvent.SelectNewImage -> {
                _state.update { it.copy(
                    imageUri = event.imageUri,
                    isNewSelectedImage = true
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

    private fun deleteImage(imageUri: String) {
        viewModelScope.launch {
            try {
                catchesUseCases.deleteImage(imageUri)
                _state.update {
                    it.copy(
                        catch = it.catch?.copy(imageUri = ""),
                        imageUri = null,
                        isUploadedImage = false,
                        isNewSelectedImage = false
                    )
                }
            } catch (e : Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun load() {
        val catchId = checkNotNull<String>(savedState["id"])
        viewModelScope.launch {
            _state.update { it.copy(isLoadingCatch = true) }
            try {
                val catch = catchesUseCases.getCatchById(catchId)!!.asCatchUi()
                _state.update { it.copy(catch = catch) }

                if (catch.imageUri.isNotEmpty()) {
                    launch(Dispatchers.IO) {
                        catchesUseCases.downloadImage(catch.imageUri) { progress ->
                            _state.update { currentState ->
                                currentState.copy(uploadProgress = progress)
                            }
                        }.let { imageUri ->
                            _state.update {
                                it.copy(
                                    isLoadingCatch = false,
                                    imageUri = imageUri,
                                    isUploadedImage = imageUri != null,
                                    isNewSelectedImage = false
                                )
                            }
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoadingCatch = false,
                            isUploadedImage = false,
                            imageUri = null
                        )
                    }
                }

            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun updateCatch(isNavigateBack: Boolean = true, isImageUpload: Boolean = true) {
        viewModelScope.launch {
            try {
                val imageUri = state.value.imageUri
                var imageUrl: String? = null

                if(isImageUpload && state.value.isNewSelectedImage) {
                    imageUrl = imageUri?.let {
                        catchesUseCases.uploadImage(it) { progress ->
                            _state.update { currentState ->
                                currentState.copy(uploadProgress = progress,
                                    isUploadedImage = true
                                )
                            }
                        }
                    }
                } else {
                    imageUrl = state.value.catch?.imageUri
                }

                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    catchesUseCases.updateCatch(
                        state.value.catch!!.copy(imageUri = imageUrl.toString()).asCatch(),
                        imageUrl.toString()
                    )
                }

                _state.update { it.copy(isNewSelectedImage = false) }

                if(isNavigateBack) {
                    _uiEvent.send(UiEvent.Success())
                }
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
                _state.update {
                    it.copy(
                        isUploadedImage = false,
                        isNewSelectedImage = false,
                        imageUri = null
                    )
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
    val isUploadedImage: Boolean = false,
    val isNewSelectedImage: Boolean = false,
    val error: Throwable? = null,
    val imageUri: Uri? = null,
    val uploadProgress: Float = 0f
)

sealed class CheckCatchEvent {
    object EditingCatch: CheckCatchEvent()
    object StopEditingCatch: CheckCatchEvent()
    data class ChangeName(val name: String): CheckCatchEvent()
    data class ChangeWeight(val weight: String): CheckCatchEvent()
    data class ChangeLength(val length: String): CheckCatchEvent()
    data class SelectSpecies(val species: SpeciesUi): CheckCatchEvent()
    object UpdateCatch: CheckCatchEvent()
    object DeleteCatch: CheckCatchEvent()
    data class DeleteImage(val imageUri: String): CheckCatchEvent()
    data class SelectNewImage(val imageUri: Uri?): CheckCatchEvent()
}