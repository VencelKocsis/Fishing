package hu.bme.aut.android.fishing.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.domain.usecases.auth.AllAuthenticationUseCases
import hu.bme.aut.android.fishing.ui.model.UiText
import hu.bme.aut.android.fishing.ui.model.toUiText
import hu.bme.aut.android.fishing.util.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authentication: AllAuthenticationUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state = _state.asStateFlow()

    private val email get() = state.value.email

    private val password get() = state.value.password

    private val confirmPassword get() = state.value.confirmPassword

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // TODO after logging in navigate to the list of catches screen

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.EmailChanged -> {
                val newEmail = event.email.trim()
                _state.update { it.copy(email = newEmail) }
            }

            is AuthenticationEvent.PasswordChanged -> {
                val newPassword = event.password.trim()
                _state.update { it.copy(password = newPassword) }
            }

            is AuthenticationEvent.ConfirmPasswordChanged -> {
                val newConfirmPassword = event.password.trim()
                _state.update { it.copy(confirmPassword = newConfirmPassword) }
            }

            AuthenticationEvent.PasswordVisibilityChanged -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }

            AuthenticationEvent.ConfirmPasswordVisibilityChanged -> {
                _state.update { it.copy(confirmPasswordVisibility = !state.value.confirmPasswordVisibility) }
            }

            AuthenticationEvent.AuthenticationButtonClicked -> {
                if (_state.value.isInRegisterMode)
                    onSignUp()
                else
                    onSignIn()
            }

            AuthenticationEvent.LogoutButtonClicked -> {
                if (_state.value.isLoggedIn)
                    onLogOut()
            }

            AuthenticationEvent.ChangeMode -> {
                _state.update { it.copy(isInRegisterMode = !_state.value.isInRegisterMode) }
            }
            AuthenticationEvent.ForgotPasswordButtonClicked -> {
                sendPasswordRecoveryEmail()
            }
        }
    }

    private fun sendPasswordRecoveryEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(!authentication.isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(R.string.correct_email_required))
                    )
                } else {
                    authentication.sendRecoveryEmail(email)
                    _uiEvent.send(UiEvent.Success(UiText.StringResource(R.string.password_recovery_email_sent)))
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun onSignIn() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!authentication.isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                    )
                } else {
                    if (password.isBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                        )
                    } else {
                        authentication.signIn(email, password)
                        _uiEvent.send(UiEvent.Success(UiText.StringResource(R.string.successful_login)))
                        checkSignInState()
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun onSignUp() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!authentication.isEmailValid(email)) {
                    _uiEvent.send(
                        UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                    )
                } else {
                    if (!authentication.passwordsMatch(password, confirmPassword) && password.isNotBlank()) {
                        _uiEvent.send(
                            UiEvent.Failure(UiText.StringResource(R.string.some_error_message))
                        )
                    } else {
                        authentication.signUp(email, password)
                        _uiEvent.send(UiEvent.Success(UiText.StringResource(R.string.successful_registration)))
                        onSignIn()
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    private fun onLogOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authentication.signOut()
                _uiEvent.send(UiEvent.Success(UiText.StringResource(R.string.successful_signout)))
                checkSignInState()

            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    fun checkSignInState() = _state.update { it.copy(isLoggedIn = authentication.hasUser()) }
}

data class AuthenticationState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordVisibility: Boolean = false,
    val confirmPasswordVisibility: Boolean = false,
    val isInRegisterMode: Boolean = false,
    val isLoggedIn: Boolean = false
)

sealed class AuthenticationEvent {
    data class EmailChanged(val email: String) : AuthenticationEvent()
    data class PasswordChanged(val password: String) : AuthenticationEvent()
    data class ConfirmPasswordChanged(val password: String) : AuthenticationEvent()
    object PasswordVisibilityChanged : AuthenticationEvent()
    object ConfirmPasswordVisibilityChanged : AuthenticationEvent()
    object AuthenticationButtonClicked : AuthenticationEvent()
    object LogoutButtonClicked : AuthenticationEvent()
    object ChangeMode : AuthenticationEvent()
    object ForgotPasswordButtonClicked : AuthenticationEvent()
}