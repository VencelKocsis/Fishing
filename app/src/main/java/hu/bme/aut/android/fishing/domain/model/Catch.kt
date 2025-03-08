package hu.bme.aut.android.fishing.domain.model

import java.util.Date

data class Catch(
    val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    val dueDate: Date? = null,
    // TODO species: Species = Species.NONE,
    // TODO val imageURL: String = "",
    val userId: String = ""
) {
    constructor() : this("", "", "", "", Date(), "")
}