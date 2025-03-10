package hu.bme.aut.android.fishing.domain.usecases.auth

import hu.bme.aut.android.fishing.data.auth.AuthService

class AllAuthenticationUseCases(
    val repository: AuthService,
    val currentUserId: CurrentUserIdUseCase,
    val currentUser: CurrentUserUseCase,
    val hasUser: HasUserUseCase,
    val signUp : SignUpUseCase,
    val signIn : SignInUseCase,
    val sendRecoveryEmail : SendRecoveryEmailUseCase,
    val deleteAccount : DeleteAccountUseCase,
    val signOut : SignOutUseCase,
    val isEmailValid: IsEmailValidUseCase,
    val passwordsMatch: PasswordsMatchUseCase,
    val getUserProfile: GetUserProfileUseCase,
    val updateUserProfile: UpdateUserProfileUseCase
)