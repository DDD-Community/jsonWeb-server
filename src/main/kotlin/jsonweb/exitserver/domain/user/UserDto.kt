package jsonweb.exitserver.domain.user

data class JwtDto(val accessToken: String)

data class UpdateUserInfoRequest(
    val newNickname: String?,
    val newProfileImageUrl: String?
)

data class UserInfoResponse(
    val nickname: String,
    val profileImageUrl: String,
    val gender: String,
    val ageRange: String,
    val exp: Int,
    val userLevel: String,
    val badges: List<String>
) {
    constructor(user: User): this(
        nickname = user.nickname,
        profileImageUrl = user.profileImageUrl,
        gender = user.gender,
        ageRange = user.ageRange,
        exp = user.exp,
        userLevel = user.level,
        badges = user.badgeList.map { it.badge }
    )
}