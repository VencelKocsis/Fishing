package hu.bme.aut.android.fishing.domain.usecases.catches

import android.net.Uri
import hu.bme.aut.android.fishing.data.catches.CatchService
import hu.bme.aut.android.fishing.domain.model.Catch

class AddCatchUseCase(private val repository: CatchService) {
    suspend operator fun invoke(catch: Catch, imageUri: String) = repository.addCatch(catch, imageUri)
}