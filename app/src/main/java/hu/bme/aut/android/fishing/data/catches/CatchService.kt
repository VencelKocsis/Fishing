package hu.bme.aut.android.fishing.data.catches

import android.net.Uri
import hu.bme.aut.android.fishing.domain.model.Catch
import kotlinx.coroutines.flow.Flow

interface CatchService {
    suspend fun catches(): Flow<List<Catch>>
    suspend fun userCatches(): Flow<List<Catch>>
    suspend fun getCatchesByName(name: String): Flow<List<Catch>>
    suspend fun getCatchById(id: String): Catch?
    suspend fun addCatch(catch: Catch, imageUri: String)
    suspend fun updateCatch(catch: Catch, imageUri: String)
    suspend fun deleteCatch(id: String)
    suspend fun uploadImage(imageUri: Uri, onProgress: (Float) -> Unit): String
    suspend fun downloadImage(imageUrl: String, onProgress: (Float) -> Unit): Uri?
}