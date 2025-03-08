package hu.bme.aut.android.fishing.ui.model

import hu.bme.aut.android.fishing.domain.model.Catch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.LocalDateTime

data class CatchUi(
    val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    val dueDate: String = LocalDate(
        LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth
    ).toString(),
    // TODO species: Species = Species.NONE
    // TODO val imageURL: String = "",
    val userId: String = ""
)

fun Catch.asCatchUi(): CatchUi = CatchUi(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = dueDate.toString(),
    // TODO species = species,
    // TODO imageURL = imageURL,
    userId = userId
)

fun CatchUi.asCatch(): Catch = Catch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = dueDate.toLocalDate(),
    // TODO species = species,
    // TODO imageURL = imageURL,
    userId = userId
)