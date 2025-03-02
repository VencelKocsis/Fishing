package hu.bme.aut.android.fishing.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.fishing.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
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
}