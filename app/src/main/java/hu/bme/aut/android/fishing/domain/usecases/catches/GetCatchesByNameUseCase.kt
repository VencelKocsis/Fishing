package hu.bme.aut.android.fishing.domain.usecases.catches

import hu.bme.aut.android.fishing.data.catches.CatchService

class GetCatchesByNameUseCase(private val repository: CatchService) {
    suspend operator fun invoke(name: String) = repository.getCatchesByName(name)
}