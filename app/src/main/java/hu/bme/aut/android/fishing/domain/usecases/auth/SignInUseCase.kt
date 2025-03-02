package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService

class SignInUseCase(private val repository: AuthService) {
    suspend operator fun invoke(email: String, password: String) =
        repository.signIn(email, password)
}