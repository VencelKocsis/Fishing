package hu.bme.aut.android.fishing.ui.model

import hu.bme.aut.android.fishing.domain.model.User
import java.text.SimpleDateFormat
import java.util.Locale

data class UserUi(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: String = ""
)

fun User.asUserUi(): UserUi = UserUi(
    id = id,
    name = name,
    email = email,
    birthDate = birthDate?.let {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
    } ?: ""
)

fun UserUi.asUser(): User = User(
    id = id,
    name = name,
    email = email,
    birthDate = birthDate.toDateOrNull()
)