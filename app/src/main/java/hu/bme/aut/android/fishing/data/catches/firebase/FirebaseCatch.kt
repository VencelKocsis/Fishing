package hu.bme.aut.android.fishing.data.catches.firebase

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import hu.bme.aut.android.fishing.domain.model.Catch
import hu.bme.aut.android.fishing.domain.model.Species
import java.util.Date

data class FirebaseCatch(
    @DocumentId val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    //@ServerTimestamp val dueDate: Date? = null,
    val species: Species = Species.NONE,
    // TODO val imageURL: String = "",
    val userId: String = ""
    )

fun FirebaseCatch.asCatch() = Catch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    //dueDate = dueDate ?: Date(),
    species = species,
    // TODO imageURL = imageURL,
    userId = userId
)

fun Catch.asFirebaseCatch() = FirebaseCatch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    //dueDate = dueDate,
    species = species,
    // TODO imageURL = imageURL,
    userId = userId
)