package hu.bme.aut.android.fishing.domain.usecases.catches

import android.net.Uri
import hu.bme.aut.android.fishing.data.catches.CatchService

class DownloadImageUseCase(private val repository: CatchService) {
    operator suspend fun invoke(imageUrl: String, onProgress: (Float) -> Unit): Uri? {
        return repository.downloadImage(imageUrl, onProgress)
    }
}