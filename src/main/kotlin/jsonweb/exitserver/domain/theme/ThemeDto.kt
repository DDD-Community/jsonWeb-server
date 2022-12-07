package jsonweb.exitserver.domain.theme

data class ThemeResponse(
    val name: String,
    val imageUrl: String,
    val description: String,
    val time: Int,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val ageLimit: String
) {
    constructor(theme: Theme): this(
        name = theme.name,
        imageUrl = theme.imageUrl,
        description = theme.description,
        time = theme.time,
        minPlayerCount = theme.minPlayerCount,
        maxPlayerCount = theme.maxPlayerCount,
        difficulty = theme.difficulty,
        ageLimit = theme.ageLimit
    )
}