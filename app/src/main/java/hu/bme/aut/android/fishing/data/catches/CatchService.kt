package hu.bme.aut.android.fishing.data.catches

import hu.bme.aut.android.fishing.data.catches.model.Catch
import kotlinx.coroutines.flow.Flow

interface CatchService {
    suspend fun catches(): Flow<List<Catch>>
    suspend fun userCatches(): Flow<List<Catch>>
    suspend fun getCatchesByName(name: String): Flow<List<Catch>>
    suspend fun getCatchById(id: String): Catch?
    suspend fun addCatch(catch: Catch)
    suspend fun updateCatch(catch: Catch)
    suspend fun deleteCatch(id: String)
}