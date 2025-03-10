package hu.bme.aut.android.fishing.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cabin
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.ui.model.SpeciesUi

@ExperimentalMaterial3Api
@Composable
fun SpeciesDropdown(
    species: List<SpeciesUi>,
    selectedSpecies: SpeciesUi,
    onSpeciesSelected: (SpeciesUi) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
    )

    val shape = RoundedCornerShape(5.dp)

    Surface(
        modifier = modifier
            .clip(shape = shape)
            .width(TextFieldDefaults.MinWidth)
            .background(MaterialTheme.colorScheme.background)
            .height(TextFieldDefaults.MinHeight)
            .clickable(enabled = enabled) { expanded = true },
        shape = shape
    ) {
        Row(
            modifier = modifier
                .width(TextFieldDefaults.MinWidth)
                .height(TextFieldDefaults.MinHeight)
                .clip(shape = shape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                //painter = painterResource(id = R.drawable.fish_on_hook), // Use drawable, not mipmap
                imageVector = Icons.Default.Cabin,
                contentDescription = null,
                tint = selectedSpecies.color,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier
                    .weight(weight = 8f),
                text = stringResource(id = selectedSpecies.title),
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(
                modifier = Modifier
                    .rotate(degrees = angle)
                    .weight(weight = 1.5f),
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .width(TextFieldDefaults.MinWidth),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                species.forEach { species ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(id = species.title),
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            expanded = false
                            onSpeciesSelected(species)
                        },
                        leadingIcon = {
                            Icon(
                                //painter = painterResource(id = R.drawable.fish_on_hook),
                                imageVector = Icons.Default.Cabin,
                                contentDescription = null,
                                tint = species.color,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun PriorityDropdown_Preview() {
    val species = listOf(SpeciesUi.None, SpeciesUi.Carp, SpeciesUi.Pike, SpeciesUi.Catfish, SpeciesUi.Crucian)
    var selectedSpecies by remember { mutableStateOf(species[0]) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpeciesDropdown(
            species = species,
            selectedSpecies = selectedSpecies,
            onSpeciesSelected = {
                selectedSpecies = it
            }
        )

    }
}





























