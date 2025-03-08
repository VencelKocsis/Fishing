package hu.bme.aut.android.fishing.data.catches.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.fishing.domain.model.Catch
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaInstant
import java.util.Date

data class FirebaseCatch(
    @DocumentId val id: String = "",
    val name: String = "",
    val weight: String = "",
    val length: String = "",
    val dueDate: Timestamp = Timestamp.now(),
    // TODO species: Species = Species.NONE
    // TODO val imageURL: String = "",
    val userId: String = ""
    )

fun FirebaseCatch.asCatch() = Catch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = LocalDateTime
        .ofInstant(Instant.ofEpochSecond(dueDate.seconds), ZoneId.systemDefault())
        .toKotlinLocalDateTime()
        .date,
    // TODO species = species,
    // TODO imageURL = imageURL,
    userId = userId
)

fun Catch.asFirebaseCatch() = FirebaseCatch(
    id = id,
    name = name,
    weight = weight,
    length = length,
    dueDate = Timestamp(Date.from(dueDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toJavaInstant())),
    // TODO species = species,
    // TODO imageURL = imageURL,
    userId = userId
)