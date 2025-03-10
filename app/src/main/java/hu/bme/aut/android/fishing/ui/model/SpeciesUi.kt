package hu.bme.aut.android.fishing.ui.model

import androidx.compose.ui.graphics.Color
import hu.bme.aut.android.fishing.R
import hu.bme.aut.android.fishing.domain.model.Species

sealed class SpeciesUi(
    val title: Int,
    val color: Color
) {
    object None: SpeciesUi(
        title = R.string.species_title_none,
        color = Color.Transparent
    )
    object Carp: SpeciesUi(
        title = R.string.species_title_carp,
        color = Color(0xFF2196F3)
    )
    object Crucian: SpeciesUi(
        title = R.string.species_title_crucian,
        color = Color(0xFFFFC107)
    )
    object Pike: SpeciesUi(
        title = R.string.species_title_pike,
        color = Color(0xFF4CAF50)
    )
    object Catfish: SpeciesUi(
        title = R.string.species_title_catfish,
        color = Color(0xFF795548)
    )
}

fun SpeciesUi.asSpecies(): Species {
    return when(this) {
        SpeciesUi.None -> Species.NONE
        SpeciesUi.Carp -> Species.CARP
        SpeciesUi.Crucian -> Species.CRUCIAN
        SpeciesUi.Pike -> Species.PIKE
        SpeciesUi.Catfish -> Species.CATFISH
    }
}

fun Species.asSpeciesUi(): SpeciesUi {
    return when(this) {
        Species.NONE -> SpeciesUi.None
        Species.CARP -> SpeciesUi.Carp
        Species.CRUCIAN -> SpeciesUi.Crucian
        Species.PIKE -> SpeciesUi.Pike
        Species.CATFISH -> SpeciesUi.Catfish
    }
}