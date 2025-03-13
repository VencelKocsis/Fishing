package hu.bme.aut.android.fishing.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.domain.model.Species
import hu.bme.aut.android.fishing.ui.model.SpeciesUi
import hu.bme.aut.android.fishing.ui.model.asSpeciesUi

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun CatchEditor(
    modifier : Modifier = Modifier,
    nameValue: String,
    nameValueChange: (String) -> Unit,
    weightValue: String,
    weightValueChange: (String) -> Unit,
    lengthValue: String,
    lengthValueChange: (String) -> Unit,
    species: List<SpeciesUi> = Species.species.map { it.asSpeciesUi() },
    selectedSpecies: SpeciesUi,
    onSpeciesSelected: (SpeciesUi) -> Unit,
    enabled: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (enabled) {
            // Name input
            NormalTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = nameValue,
                label = stringResource(id = R.string.catch_name),
                onValueChange = nameValueChange,
                enabled = true,
                readOnly = false,
                isError = false,
                onDone = {},
                leadingIcon = { /*TODO*/ },
                trailingIcon = { /*TODO*/ },
                imeAction = ImeAction.Next,
                singleLine = true,
                keyboardType = KeyboardType.Text,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        // Species dropdown
        SpeciesDropdown(
            species = species,
            selectedSpecies = selectedSpecies,
            onSpeciesSelected = onSpeciesSelected,
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled
        )

        // Weight input
        NormalTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = weightValue,
            label = stringResource(id = R.string.catch_weight),
            onValueChange = weightValueChange,
            enabled = true,
            readOnly = false,
            isError = false,
            onDone = {},
            leadingIcon = { /*TODO*/ },
            trailingIcon = { /*TODO*/ },
            imeAction = ImeAction.Next,
            singleLine = true,
            keyboardType = KeyboardType.Number,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            )
        )

        // Length input
        NormalTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = lengthValue,
            label = stringResource(id = R.string.catch_length),
            onValueChange = lengthValueChange,
            enabled = true,
            readOnly = false,
            isError = false,
            leadingIcon = { /*TODO*/ },
            trailingIcon = { /*TODO*/ },
            imeAction = ImeAction.Done,
            singleLine = true,
            keyboardType = KeyboardType.Number,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            onDone = {
                keyboardController?.hide()
            }
        )
    }
}






















