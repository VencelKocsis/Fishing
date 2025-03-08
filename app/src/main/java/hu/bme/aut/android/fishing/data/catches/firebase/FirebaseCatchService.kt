package hu.bme.aut.android.fishing.data.catches.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import hu.bme.aut.android.fishing.data.catches.CatchService
import hu.bme.aut.android.fishing.domain.model.Catch
import hu.bme.aut.android.fishing.domain.usecases.auth.CurrentUserIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDate
import java.util.Date
import javax.inject.Inject

class FirebaseCatchService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase
) : CatchService {
    override suspend fun catches(): Flow<List<Catch>> =
        firestore.collection(CATCHES_COLLECTION).dataObjects()

    override suspend fun userCatches(): Flow<List<Catch>> =
        firestore.collection(CATCHES_COLLECTION).whereEqualTo(USER_ID_FIELD, userIdUseCase())
            .dataObjects()

    override suspend fun getCatchesByName(name: String): Flow<List<Catch>> =
        firestore.collection(CATCHES_COLLECTION).whereEqualTo(CATCHES_NAME_FIELD, name)
            .dataObjects()

    override suspend fun getCatchById(id: String): Catch? =
        firestore.collection(CATCHES_COLLECTION).document(id).get().await().toObject<Catch>()

    override suspend fun addCatch(catch: Catch) {
        firestore.collection(CATCHES_COLLECTION).add(catch.asFirebaseCatch()).await()
    }

    override suspend fun updateCatch(catch: Catch) {
        firestore.collection(CATCHES_COLLECTION).document(catch.id).set(catch).await()
    }

    override suspend fun deleteCatch(id: String) {
        firestore.collection(CATCHES_COLLECTION).document(id).delete().await()
    }

    companion object {
        private const val CATCHES_COLLECTION = "catches"
        private const val CATCHES_NAME_FIELD = "name"
        private const val USER_ID_FIELD = "userId"
    }
}