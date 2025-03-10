package hu.bme.aut.android.fishing.domain.model

enum class Species {
    NONE,
    CRUCIAN,
    CARP,
    PIKE,
    CATFISH;

    companion object {
        val species = listOf(NONE, CRUCIAN, CARP, PIKE, CATFISH)
    }
}