package jsonweb.exitserver.domain

import jsonweb.exitserver.domain.review.Review
import jsonweb.exitserver.domain.theme.Genre
import jsonweb.exitserver.domain.theme.GenreEnum
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.theme.ThemeGenre
import jsonweb.exitserver.domain.user.User


fun getMockUser() = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
fun getMockReview() = Review("", 3.0, 3.0, "", "", getMockUser(), Theme())

fun getMockReview(genreEnum: GenreEnum): Review {
    val theme = Theme()
    theme.addThemeGenre(ThemeGenre(theme, Genre(genreEnum.kor())))
    return Review("", 3.0, 3.0, "", "", getMockUser(), theme)
}
