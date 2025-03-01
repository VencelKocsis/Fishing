package hu.bme.aut.android.fishing.util

import hu.bme.aut.android.fishing.ui.model.UiText

sealed class UiEvent {
    data class Success(val message: UiText? = null): UiEvent()

    data class Failure(val message: UiText): UiEvent()
}
