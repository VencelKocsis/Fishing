package hu.bme.aut.android.fishing.ui.model

import android.net.Uri
import hu.bme.aut.android.fishing.feature.catches.check_catch.CheckCatchState
import hu.bme.aut.android.fishing.feature.catches.create_catch.AddCatchState

data class CatchImageStateUi(
    val imageUri: Uri?,
    val uploadedImageUri: String?,
    val isNewSelectedImage: Boolean,
    val isUploadedImage: Boolean
)

fun AddCatchState.toImageStateUi(): CatchImageStateUi {
    return CatchImageStateUi(
        imageUri = this.imageUri,
        uploadedImageUri = this.catch.imageUri,
        isNewSelectedImage = this.imageUri != null,
        isUploadedImage = !this.catch.imageUri.isNullOrEmpty()
    )
}

fun CheckCatchState.CatchImageStateUi(): CatchImageStateUi {
    return CatchImageStateUi(
        imageUri = this.imageUri,
        uploadedImageUri = this.catch?.imageUri,
        isNewSelectedImage = this.isNewSelectedImage,
        isUploadedImage = this.isUploadedImage
    )
}