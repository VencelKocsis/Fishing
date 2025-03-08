package hu.bme.aut.android.fishing.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

data class Catch(
    val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    val dueDate: LocalDate = LocalDateTime.now().toKotlinLocalDateTime().date,
    // TODO species: Species = Species.NONE,
    // TODO val imageURL: String = "",
    val userId: String = ""
)