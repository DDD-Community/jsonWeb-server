package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.util.toDotFormat
import javax.validation.constraints.Size

data class BoastRequest(
    val themeId: Long,
    val imageUrl: String,
    @field:Size(max = 3)
    val hashtags: List<String>
)

data class BoastResponse(
    val boastId: Long,
    val cafeName: String,
    val themeName: String,
    val writerNickname: String,
    val writerProfileImage: String,
    val writerLevel: String,
    val genre: List<String>,
    val modifiedAt: String,
    var isLiked: Boolean = false,
    val likeCount: Int,
    val boastImage: String,
    val hashtags: List<String>
) {
    constructor(boast: Boast) : this(
        boastId = boast.id,
        cafeName = boast.theme.cafe.name,
        themeName = boast.theme.name,
        writerNickname = boast.user.nickname,
        writerProfileImage = boast.user.profileImageUrl,
        writerLevel = boast.user.level,
        genre = boast.theme.themeGenreList.map { it.genre.genreName },
        modifiedAt = boast.modifiedAt.toDotFormat(),
        likeCount = boast.likeCount,
        boastImage = boast.imageUrl,
        hashtags = boast.hashtagList.map { it.hashtag }
    )
}

data class BoastListResponse(
    val boastList: List<BoastResponse>,
    val totalNumber: Long,
    val isLast: Boolean
)