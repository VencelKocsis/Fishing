package hu.bme.aut.android.fishing.data.catches.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

class Catch(
    @DocumentId val id: String = "",
    val name: String = "",
    val time: Date = Date(),
    val weight: String = "",
    val length: String = "",
    val userId: String = ""
    // TODO add: Species, imageUrl
)