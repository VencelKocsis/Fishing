package hu.bme.aut.android.fishing.domain.usecases.catches

import hu.bme.aut.android.fishing.data.catches.CatchService

class GetCatchByIdUseCase(private val repository: CatchService) {
    suspend operator fun invoke(id: String) = repository.getCatchById(id)
}