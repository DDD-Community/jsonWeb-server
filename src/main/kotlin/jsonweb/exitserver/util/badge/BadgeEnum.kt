package jsonweb.exitserver.util.badge

import jsonweb.exitserver.domain.user.User

const val GENRE_REVIEW_REQUIREMENT = 3

enum class BadgeEnum(
    private val kor: String,
    private val requirement: String,
    private val order: Int
) {
    ROMANCE_HOLIC(
        "로맨스홀릭", "로맨스 장르 테마 리뷰를 3개이상 작성할시 얻을 수 있는 칭호", 8
    ),

    EIXTER(
        "엑시터", "첫 리뷰를 작성하면 얻을 수 있는 칭호", 1
    ),

    TREND_SETTER(
        "트렌드세터", "방탈출 카페 등록 요청이 5개 이상 승낙될시 얻을 수 있는 칭호", 3
    ),

    EXIT_SHERIFF(
        "엑시트보안관", "잘못된 리뷰 신고가 10건이상 처리 되었을 때 얻을 수 있는 칭호", 4
    ),

    ADDICTED_ESCAPE(
        "탈출중독", "탈출 인증 글을 10개 이상 작성할시 얻을 수 있는 칭호", 2
    ),

    HORROR_MANIA(
        "공포매니아", "공포 장르 테마 리뷰를 3개이상 작성할시 얻을 수 있는 칭호", 6
    ),

    HIGHEST_REALM(
        "천상계", "난이도가 5인 테마를 탈출하고 인증할시 얻을 수 있는 칭호", 5
    ),
;
    fun toEntity(user: User) = Badge(user = user, badge = kor)
    fun kor() = kor
    fun requirement() = requirement
    fun order() = order
}