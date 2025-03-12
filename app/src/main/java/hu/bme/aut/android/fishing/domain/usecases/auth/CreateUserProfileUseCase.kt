package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService
import hu.bme.aut.android.fishing.domain.model.User

class CreateUserProfileUseCase(private val authService: AuthService) {
    operator suspend fun invoke(user: User) {
        authService.createUserProfile(user)
    }
}