package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService

class DeleteAccountUseCase(private val repository: AuthService) {
    suspend operator fun invoke() =
        repository.deleteAccount()
}