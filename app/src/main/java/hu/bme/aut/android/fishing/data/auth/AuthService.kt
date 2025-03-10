package hu.bme.aut.android.fishing.data.auth

import hu.bme.aut.android.fishing.domain.model.User

interface AuthService {
    fun currentUserId(): String?
    fun currentUser(): User?
    fun hasUser(): Boolean
    suspend fun signUp(
        email: String, password: String,
    )
    suspend fun signIn(
        email: String,
        password: String
    )
    suspend fun sendRecoveryEmail(email: String)
    suspend fun deleteAccount()
    suspend fun signOut()

    suspend fun getUserProfile(id: String): User?
    suspend fun updateUserProfile(user: User)
}