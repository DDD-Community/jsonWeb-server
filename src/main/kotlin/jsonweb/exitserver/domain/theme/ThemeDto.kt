package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.review.Review

data class RegisterThemeRequest(
    val cafeId: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val time: Int,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val ageLimit: String,
    val genreList: List<String>
)

data class UpdateThemeRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val time: Int,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val ageLimit: String,
    val genreList: List<String>
)

data class RegisterThemeGenreRequest(
    val themeId: Long,
    val genreList: List<String>
)

data class ThemeListResponse(
    val themeList: List<ThemeResponse>
)

data class ThemeResponse(
    val themeId: Long,
    val name: String,
    val imageUrl: String,
    val time: Int,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val ageLimit: String,
    val genreList: List<Genre>
) {
    constructor(theme: Theme): this(
        themeId = theme.themeId,
        name = theme.name,
        imageUrl = theme.imageUrl,
        time = theme.time,
        minPlayerCount = theme.minPlayerCount,
        maxPlayerCount = theme.maxPlayerCount,
        difficulty = theme.difficulty,
        ageLimit = theme.ageLimit,
        genreList = theme.genreList
    )
}

data class ThemeSpecResponse(
    val themeId: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val time: Int,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val ageLimit: String,
    val genreList: List<Genre>
) {
    constructor(theme: Theme): this(
        themeId = theme.themeId,
        name = theme.name,
        description = theme.description,
        imageUrl = theme.imageUrl,
        time = theme.time,
        minPlayerCount = theme.minPlayerCount,
        maxPlayerCount = theme.maxPlayerCount,
        difficulty = theme.difficulty,
        ageLimit = theme.ageLimit,
        genreList = theme.genreList
    )
}