package jsonweb.exitserver.domain.boast

import org.springframework.data.domain.Sort
import javax.validation.constraints.Size

enum class BoastSort(private val sort: String, private val direction: Sort.Direction) {
    LIKE("likeCount", Sort.Direction.ASC),
    DATE("modifiedAt", Sort.Direction.ASC);

    fun getSortBy() = sort
    fun getDirection() = direction
}

data class BoastRequest(
    val themeId: Long,

    @field:Size(max = 3)
    val imageUrls: List<String>,

    @field:Size(max = 3)
    val hashtags: List<String>
)

data class BoastResponse(
    val boastId: Long,
    val userNickname: String,
    val userProfileImage: String,
    val userLevel: String = "1", // TODO
    val userBadge: String = "초보", // TODO
    val genre: List<String>,
    val modifiedAt: String,
    var isLiked: Boolean = false,
    val likeCount: Int,
    val boastImages: List<String>,
    val hashtags: List<String>
) {
    constructor(boast: Boast) : this(
        boastId = boast.id,
        userNickname = boast.user.nickname,
        userProfileImage = boast.user.profileImageUrl,
//        userLevel = boast.user.level.toString(), TODO
//        userBadge = boast.user.badge.toString(), TODO
        genre = boast.theme.themeGenreList.map { it.genre.genreName },
        modifiedAt = boast.modifiedAt,
        likeCount = boast.likeCount,
        boastImages = boast.boastImageList.map { it.imageUrl },
        hashtags = boast.hashtagList.map { it.hashtag }
    )
}

data class BoastListResponse(
    val boastList: List<BoastResponse>,
    val totalNumber: Long,
    val isLast: Boolean
)