package hu.bme.aut.android.fishing.domain.usecases.auth

class PasswordsMatchUseCase {
    operator fun invoke(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}