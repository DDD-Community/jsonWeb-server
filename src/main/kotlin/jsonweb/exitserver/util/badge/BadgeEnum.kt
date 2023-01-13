package jsonweb.exitserver.util.badge

import jsonweb.exitserver.domain.user.User

enum class BadgeEnum(private val kor: String, private val requirement: String) {
    EIXTER(
        "엑시터", "첫 리뷰 작성"
    ),

    ADDICTED_ESCAPE(
        "탈출중독", "인증 10개 이상 작성"
    ),

    TREND_SETTER(
        "트렌드세터", "카페 등록 요청 5번 승낙"
    ),

    EXIT_SHERIFF(
        "엑시트 보안관", "리뷰 신고 10번 처리 됨"
    ),

    HIGHEST_REALM(
        "천상계", "난이도 5 탈출 인증"
    ),

    HORROR_MANIA(
        "공포매니아", "공포 장르 리뷰 3개 작성"
    ),

    MYSTERY_LOVER(
        "미스터리 러버", "미스터리 장르 리뷰 3개 작성"
    ),

    ROMANCE_HOLIC(
        "로맨스 홀릭", "로맨스 장르 리뷰 3개 작성"
    );
    fun toEntity(user: User) = Badge(user = user, badge = kor)
    fun kor() = kor
    fun requirement() = requirement
}