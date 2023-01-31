package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.Cafe

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

data class ThemeWithCafe(
    val name: String,
    val description: String,
    val imageUrl: String,
    val time: Int,
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val ageLimit: String,
    val cafe: Cafe
) {
    constructor(form: RegisterThemeRequest, cafe: Cafe): this(
        name = form.name,
        description = form.description,
        imageUrl = form.imageUrl,
        time = form.time,
        minPlayerCount = form.minPlayerCount,
        maxPlayerCount = form.maxPlayerCount,
        difficulty = form.difficulty,
        ageLimit = form.ageLimit,
        cafe = cafe
    )
}

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
    val minPlayerCount: Int,
    val maxPlayerCount: Int,
    val difficulty: Double,
    val genreList: List<Genre>
) {
    constructor(theme: Theme): this(
        themeId = theme.themeId,
        name = theme.name,
        imageUrl = theme.imageUrl,
        minPlayerCount = theme.minPlayerCount,
        maxPlayerCount = theme.maxPlayerCount,
        difficulty = theme.difficulty,
        genreList = theme.themeGenreList.map { it.genre }
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
    val avgStar: Double,
    val reviewCount: Int,
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
        avgStar = theme.avgStar,
        reviewCount = theme.reviewCount,
        genreList = theme.themeGenreList.map { it.genre }
    )
}

