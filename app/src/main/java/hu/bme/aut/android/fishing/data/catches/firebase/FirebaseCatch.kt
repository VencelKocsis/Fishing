package hu.bme.aut.android.fishing.data.catches.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.fishing.domain.model.Catch
import hu.bme.aut.android.fishing.domain.model.Species

data class FirebaseCatch(
    @DocumentId val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    val dueDate: Timestamp? = null,
    val species: Species = Species.NONE,
    val imageURL: String = "",
    val userId: String = ""
    )

fun FirebaseCatch.asCatch() = Catch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = dueDate?.toDate(),
    species = species,
    imageUri = imageURL,
    userId = userId
)

fun Catch.asFirebaseCatch() = FirebaseCatch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = dueDate?.let { Timestamp(it) } ?: Timestamp.now(),
    species = species,
    imageURL = imageUri,
    userId = userId
)