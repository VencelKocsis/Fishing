package hu.bme.aut.android.fishing.data.auth.firebase

import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.fishing.domain.model.User
import java.util.Date

data class FirebaseUser(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val birthDate: Date? = null
)

fun FirebaseUser.asUser() = User(
    id = id,
    name = name,
    email = email,
    birthDate = birthDate
)

fun User.asFirebaseUser() = FirebaseUser(
    id = id,
    name = name,
    email = email,
    birthDate = birthDate
)