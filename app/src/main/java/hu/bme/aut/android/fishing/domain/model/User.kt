package hu.bme.aut.android.fishing.domain.model

import java.util.Date

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: Date? = null
)