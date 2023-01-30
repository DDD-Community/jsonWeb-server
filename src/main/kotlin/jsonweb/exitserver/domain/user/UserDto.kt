package jsonweb.exitserver.domain.user

import jsonweb.exitserver.util.toDotFormat

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

data class CheckNicknameResponse(val isDuplicated: Boolean)

data class ExpLogResponse(
    val createdAt: String,
    val amount: Int,
    val reason: String
) {
    constructor(expLog: ExpLog): this(
        createdAt = expLog.createdAt.toDotFormat(),
        amount = expLog.amount,
        reason = expLog.reason
    )
}