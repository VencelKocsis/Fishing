package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService

class SignUpUseCase(private val repository: AuthService) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signUp(email, password)
}