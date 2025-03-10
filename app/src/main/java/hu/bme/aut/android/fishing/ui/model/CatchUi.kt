package hu.bme.aut.android.fishing.ui.model

import hu.bme.aut.android.fishing.domain.model.Catch
import hu.bme.aut.android.fishing.domain.model.Species
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CatchUi(
    val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    //val dueDate: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val species: SpeciesUi = SpeciesUi.None,
    // TODO val imageURL: String = "",
    val userId: String = ""
)

fun Catch.asCatchUi(): CatchUi = CatchUi(
    id = id,
    name = name,
    weight = weight,
    length = length,
    //dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dueDate),
    species = species.asSpeciesUi(),
    // TODO imageURL = imageURL,
    userId = userId
)

fun CatchUi.asCatch(): Catch = Catch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    //dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dueDate) ?: Date(),
    species = species.asSpecies(),
    // TODO imageURL = imageURL,
    userId = userId
)