package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService
import hu.bme.aut.android.fishing.domain.model.User

class UpdateUserProfileUseCase(private val repository: AuthService) {
    suspend operator fun invoke(user: User) = repository.updateUserProfile(user)
}