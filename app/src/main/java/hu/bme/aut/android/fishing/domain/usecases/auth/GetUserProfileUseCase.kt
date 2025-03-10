package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService

class GetUserProfileUseCase(private val repository: AuthService) {
    suspend operator fun invoke(id: String) = repository.getUserProfile(id)
}