package hu.bme.aut.android.fishing.domain.model

import java.util.Date

data class Catch(
    val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    val dueDate: Date? = null,
    val species: Species = Species.NONE,
    val imageUri: String = "",
    val userId: String = ""
)