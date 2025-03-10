package hu.bme.aut.android.fishing.domain.model

enum class Species {
    NONE,
    CRUCIAN,
    CARP,
    PIKE,
    CATFIS;

    companion object {
        val species = listOf(NONE, CRUCIAN, CARP, PIKE, CATFIS)
    }
}