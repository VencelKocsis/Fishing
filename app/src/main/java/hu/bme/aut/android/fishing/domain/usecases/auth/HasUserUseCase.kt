package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService

class HasUserUseCase(private val repository: AuthService) {
    operator fun invoke() =
        repository.hasUser()
}