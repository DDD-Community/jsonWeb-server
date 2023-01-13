package jsonweb.exitserver.domain

import jsonweb.exitserver.domain.review.Review
import jsonweb.exitserver.domain.theme.Genre
import jsonweb.exitserver.domain.theme.GenreEnum
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.theme.ThemeGenre
import jsonweb.exitserver.domain.user.User

val mockUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
val mockTheme = Theme()
val mockReview = Review("", 3.0, 3.0, "", "", mockUser, mockTheme)




