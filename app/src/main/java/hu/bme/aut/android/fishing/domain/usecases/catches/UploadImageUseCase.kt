package hu.bme.aut.android.fishing.domain.usecases.catches

import android.net.Uri
import hu.bme.aut.android.fishing.data.catches.CatchService

class UploadImageUseCase(private val repository: CatchService) {
    operator suspend fun invoke(imageUri: Uri) = repository.uploadImage(imageUri)
}