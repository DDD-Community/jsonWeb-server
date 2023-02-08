package jsonweb.exitserver.util.badge

import jsonweb.exitserver.domain.user.User

const val GENRE_REVIEW_REQUIREMENT = 3

enum class BadgeEnum(
    private val kor: String,
    private val requirement: String,
    private val order: Int
) {
    EIXTER(
        "엑시터", "첫 리뷰 작성", 1
    ),

    ADDICTED_ESCAPE(
        "탈출중독", "인증 10개 이상 작성", 2
    ),

    TREND_SETTER(
        "트렌드세터", "카페 등록 요청 5번 승낙", 3
    ),

    EXIT_SHERIFF(
        "엑시트 보안관", "리뷰 신고 10번 처리 됨", 4
    ),

    HIGHEST_REALM(
        "천상계", "난이도 5 탈출 인증", 5
    ),

    HORROR_MANIA(
        "공포매니아", "공포 장르 리뷰 3개 작성", 6
    ),

    MYSTERY_LOVER(
        "미스터리 러버", "미스터리 장르 리뷰 3개 작성", 7
    ),

    ROMANCE_HOLIC(
        "로맨스 홀릭", "로맨스 장르 리뷰 3개 작성", 8
    );
    fun toEntity(user: User) = Badge(user = user, badge = kor)
    fun kor() = kor
    fun requirement() = requirement
    fun order() = order
}