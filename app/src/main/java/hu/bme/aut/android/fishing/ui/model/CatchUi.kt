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
    val dueDate: String = "",
    val species: SpeciesUi = SpeciesUi.None,
    val imageURL: String = "",
    val userId: String = ""
)

fun String?.toDateOrNull(): Date? {
    return if (this.isNullOrEmpty()) {
        null
    } else {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormat.parse(this)
        } catch (e: Exception) {
            null
        }
    }
}

fun Catch.asCatchUi(): CatchUi = CatchUi(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = dueDate?.let {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
    } ?: "",
    species = species.asSpeciesUi(),
    imageURL = imageURL,
    userId = userId
)

fun CatchUi.asCatch(): Catch = Catch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = dueDate.toDateOrNull(),
    species = species.asSpecies(),
    imageURL = imageURL,
    userId = userId
)