package hu.bme.aut.android.fishing.data.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import hu.bme.aut.android.fishing.data.auth.AuthService
import hu.bme.aut.android.fishing.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    AuthService {
    override fun currentUserId(): String? = firebaseAuth.currentUser?.uid

    override fun currentUser(): User?  {
        return currentUserId()?.let { User(it) }
    }

    override fun hasUser() = firebaseAuth.currentUser != null

    override suspend fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(user?.email?.substringBefore('@'))
                    .build()
                user?.updateProfile(profileChangeRequest)
            }.await()
    }

    override suspend fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    override suspend fun deleteAccount() {
        firebaseAuth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getUserProfile(id: String): User? =
        firestore.collection(PROFILES_COLLECTION).document(id).get().await().toObject<FirebaseUser>()?.asUser()


    override suspend fun updateUserProfile(user: User) {
        firestore.collection(PROFILES_COLLECTION).document(user.id).set(user.asFirebaseUser()).await()
    }

    companion object {
        private const val PROFILES_COLLECTION = "users"
    }
}