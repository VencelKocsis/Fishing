package hu.bme.aut.android.fishing.domain.usecases.catches

import hu.bme.aut.android.fishing.data.catches.CatchService

class DeleteCatchUseCase(private val repository: CatchService) {
    suspend operator fun invoke(id: String) = repository.deleteCatch(id)
}