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
import java.io.File
import javax.inject.Inject

class FirebaseCatchService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val userIdUseCase: CurrentUserIdUseCase
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

    // Updated addCatch method
    override suspend fun addCatch(catch: Catch, imageUri: String) {
        val firebaseCatch = catch.copy(imageUri = imageUri).asFirebaseCatch()
        firestore.collection(CATCHES_COLLECTION).add(firebaseCatch).await()
    }

    // Updated updateCatch method
    override suspend fun updateCatch(catch: Catch, imageUri: String) {
        val updatedCatch = catch.copy(imageUri = imageUri).asFirebaseCatch()
        firestore.collection(CATCHES_COLLECTION).document(catch.id).set(updatedCatch).await()
    }

    override suspend fun deleteCatch(id: String) {
        firestore.collection(CATCHES_COLLECTION).document(id).delete().await()
    }

    override suspend fun uploadImage(imageUri: Uri, onProgress: (Float) -> Unit): String {
        try {
            // Create a storage reference from our app
            val storageRef = storage.reference

            // Create a reference with an initial file path and name
            val imageRef =
                storageRef.child("catch_images/${System.currentTimeMillis()}_${imageUri.lastPathSegment}")

            // Upload the file
            val uploadTask = imageRef.putFile(imageUri)

            // Listen for state changes, errors, and completion of the upload
            uploadTask.addOnProgressListener { snapshot ->
                val progress =
                    (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat() / 100
                onProgress(progress)
            }

            // Wait for the upload to complete
            val taskSnapshot = uploadTask.await()

            // Get the download URL
            val downloadUrl = taskSnapshot.storage.downloadUrl.await()
            Log.d("UploadImage", "Image uploaded and URL: $downloadUrl")

            // Example of creating a reference from an HTTPS URL
            val httpsReference = storage.getReferenceFromUrl(downloadUrl.toString())
            Log.d("UploadImage", "HTTPS Reference: $httpsReference")

            // Example of creating a reference from a Google Cloud Storage URI
            val gsReference =
                storage.getReferenceFromUrl("gs://${storage.app.options.storageBucket}/${imageRef.path}")
            Log.d("UploadImage", "GS Reference: $gsReference")

            return downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("UploadImage", "Error during image upload", e)
            throw e
        }
    }
    // TODO needs to add: delete image from catch

    override suspend fun downloadImage(imageUrl: String, onProgress: (Float) -> Unit): Uri? {
        try {
            if(imageUrl == "null" || imageUrl.isEmpty()) {
                return null
            }

            Log.d("DownloadImage", "Starting download for: $imageUrl")

            if (!imageUrl.startsWith("https://") && !imageUrl.startsWith("gs://")) {
                throw IllegalArgumentException("Invalid Firebase Storage URL")
            }

            val storageReference = storage.getReferenceFromUrl(imageUrl)
            val tempFile = File.createTempFile("download", "jpg")
            val downloadTask = storageReference.getFile(tempFile)

            downloadTask.addOnProgressListener { snapshot ->
                val progress = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toFloat() / 100
                Log.d("DownloadImage", "Progress: ${progress * 100}%")
                onProgress(progress)
            }

            downloadTask.await()
            Log.d("DownloadImage", "Download complete")

            return Uri.fromFile(tempFile)
        } catch (e: Exception) {
            Log.e("DownloadImage", "Error during image download", e)
            throw e
        }
    }

    override suspend fun deleteImage(imageUrl: String) {
        try {
            Log.d("DeleteImage", "Starting deletion for: $imageUrl")

            if (!imageUrl.startsWith("https://") && !imageUrl.startsWith("gs://")) {
                throw IllegalArgumentException("Invalid Firebase Storage URL")
            }

            val storageReference = storage.getReferenceFromUrl(imageUrl)
            storageReference.delete().await()
            Log.d("DeleteImage", "Image deleted successfully")

        } catch (e: Exception) {
            Log.e("DeleteImage", "Error during image deletion", e)
            throw e
        }
    }

    companion object {
        private const val CATCHES_COLLECTION = "catches"
        private const val CATCHES_NAME_FIELD = "name"
        private const val USER_ID_FIELD = "userId"
    }
}