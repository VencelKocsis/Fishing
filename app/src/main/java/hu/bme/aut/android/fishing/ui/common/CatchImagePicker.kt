package hu.bme.aut.android.fishing.ui.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hu.bme.aut.android.fishing.ui.model.CatchImageStateUi
import hu.bme.aut.android.fishing.R

@Composable
fun CatchImagePicker(
    imageState: CatchImageStateUi,
    isEditing: Boolean,
    onImageSelected: (Uri?) -> Unit,
    onImageDeleted: () -> Unit
) {
    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            onImageSelected(uri)
        }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            // Show uploaded image when NOT editing
            imageState.isUploadedImage && !imageState.uploadedImageUri.isNullOrEmpty() && !isEditing -> {
                AsyncImage(
                    model = imageState.uploadedImageUri,
                    contentDescription = "Uploaded Catch Image",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Show selected image when editing
            imageState.imageUri != null && imageState.isNewSelectedImage -> {
                AsyncImage(
                    model = imageState.imageUri,
                    contentDescription = "Selected Catch Image",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Show "add image" icon if no image is present
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { imagePickerLauncher.launch("image/*") }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add Photo",
                        modifier = Modifier.size(48.dp)
                    )
                    Text(text = stringResource(R.string.add_photo))
                }
            }
        }

        // Show close button when editing and an image is present
        if (isEditing && (imageState.imageUri != null || imageState.isUploadedImage)) {
            IconButton(
                onClick = { onImageDeleted() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Image"
                )
            }
        }
    }
}
