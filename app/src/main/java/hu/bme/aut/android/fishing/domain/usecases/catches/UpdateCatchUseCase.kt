package hu.bme.aut.android.fishing.domain.usecases.catches

import hu.bme.aut.android.fishing.data.catches.CatchService
import hu.bme.aut.android.fishing.domain.model.Catch

class UpdateCatchUseCase(private val repository: CatchService) {
    suspend operator fun invoke(catch: Catch) = repository.updateCatch(catch)
}