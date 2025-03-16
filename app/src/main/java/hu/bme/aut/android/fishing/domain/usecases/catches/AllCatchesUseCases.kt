package hu.bme.aut.android.fishing.domain.usecases.catches

import hu.bme.aut.android.fishing.data.catches.CatchService

class AllCatchesUseCases(
    val repository: CatchService,
    val catches: CatchesUseCase,
    val userCatches: UserCatchesUseCase,
    val getCatchesByName: GetCatchesByNameUseCase,
    val getCatchById: GetCatchByIdUseCase,
    val addCatch: AddCatchUseCase,
    val updateCatch: UpdateCatchUseCase,
    val deleteCatch: DeleteCatchUseCase,
    val uploadImage: UploadImageUseCase,
    val downloadImage: DownloadImageUseCase,
    val deleteImage: DeleteImageUseCase
)