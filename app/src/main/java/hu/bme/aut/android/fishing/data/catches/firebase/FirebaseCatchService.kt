package hu.bme.aut.android.fishing.data.catches.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import hu.bme.aut.android.fishing.data.catches.CatchService
import hu.bme.aut.android.fishing.domain.model.Catch
import hu.bme.aut.android.fishing.domain.usecases.auth.CurrentUserIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCatchService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val userIdUseCase: CurrentUserIdUseCase,
) : CatchService {

    override suspend fun catches(): Flow<List<Catch>> =
        firestore.collection(CATCHES_COLLECTION).dataObjects<FirebaseCatch>()
            .map { list -> list.map { it.asCatch() } }

    override suspend fun userCatches(): Flow<List<Catch>> =
        firestore.collection(CATCHES_COLLECTION).whereEqualTo(USER_ID_FIELD, userIdUseCase())
            .dataObjects<FirebaseCatch>()
            .map { list -> list.map { it.asCatch() } }

    override suspend fun getCatchesByName(name: String): Flow<List<Catch>> =
        firestore.collection(CATCHES_COLLECTION).whereEqualTo(CATCHES_NAME_FIELD, name)
            .dataObjects()

    override suspend fun getCatchById(id: String): Catch? =
        firestore.collection(CATCHES_COLLECTION).document(id).get().await().toObject<FirebaseCatch>()?.asCatch()

    override suspend fun addCatch(catch: Catch, imageUri: Uri?) {
        val imageUrl = imageUri?.let { uploadImage(it) } ?: ""
        val firebaseCatch = catch.copy(imageURL = imageUrl).asFirebaseCatch()
        firestore.collection(CATCHES_COLLECTION).add(firebaseCatch).await()
    }

    override suspend fun updateCatch(catch: Catch, imageUri: Uri?) {
        val imageUrl = imageUri?.let { uploadImage(it) } ?: catch.imageURL
        val updatedCatch = catch.copy(imageURL = imageUrl).asFirebaseCatch()
        firestore.collection(CATCHES_COLLECTION).document(catch.id).set(updatedCatch).await()
    }

    override suspend fun deleteCatch(id: String) {
        firestore.collection(CATCHES_COLLECTION).document(id).delete().await()
    }

    override suspend fun uploadImage(imageUri: Uri): String? {
        return try {
            val storageRef = storage.reference.child("catch_images/${imageUri.lastPathSegment}")
            storageRef.putFile(imageUri).await()
            storageRef.downloadUrl.await().toString()
        } catch (e : Exception) {
            Log.e("FirebaseCatchService", "Image upload failed", e)
            null
        }
    }

    companion object {
        private const val CATCHES_COLLECTION = "catches"
        private const val CATCHES_NAME_FIELD = "name"
        private const val USER_ID_FIELD = "userId"
    }
}