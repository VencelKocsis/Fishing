package hu.bme.aut.android.fishing.domain.usecases.auth

class IsEmailValidUseCase {
    operator fun invoke(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}