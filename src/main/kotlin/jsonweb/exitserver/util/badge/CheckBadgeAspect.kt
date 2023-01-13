package jsonweb.exitserver.util.badge

import jsonweb.exitserver.common.logger
import jsonweb.exitserver.domain.inquiry.InquiryCategoryEnum
import jsonweb.exitserver.domain.theme.GenreEnum
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

enum class BadgeDomain {
    BOAST, INQUIRY, REVIEW
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckBadge(
    val type: BadgeDomain
)

@Aspect
@Component
class CheckBadgeAspect(private val userService: UserService) {
    val log = logger()

    @AfterReturning("@annotation(jsonweb.exitserver.util.badge.CheckBadge)")
    fun checkBadge(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        val user = userService.getCurrentLoginUser()
        when (signature.method.getAnnotation(CheckBadge::class.java).type) {
            BadgeDomain.BOAST -> checkBoastBadge(user)
            BadgeDomain.REVIEW -> checkReviewBadge(user)
            BadgeDomain.INQUIRY -> checkInquiryBadge(user)
        }
    }

    private fun checkBoastBadge(user: User) {
        // 인증 글 10개 작성, 탈출중독
        if (user.isNotGotten(BadgeEnum.ADDICTED_ESCAPE) &&
            user.myBoastList.size == 10
        ) {
            log.info("${user.nickname}님이 ${BadgeEnum.ADDICTED_ESCAPE.kor()} 뱃지 획득!")
            user.addBadge(BadgeEnum.ADDICTED_ESCAPE)
        }

        // 첫 난이도 5 탈출 인증 작성, 천상계
        if (user.isNotGotten(BadgeEnum.HIGHEST_REALM) &&
            user.myBoastList.any { it.theme.difficulty == 5.0 }
        ) {
            log.info("${user.nickname}님이 ${BadgeEnum.HIGHEST_REALM.kor()} 뱃지 획득!")
            user.addBadge(BadgeEnum.HIGHEST_REALM)
        }
    }

    private fun setGenreBadge(genre: GenreEnum, badge: BadgeEnum) {
        val user = userService.getCurrentLoginUser()
        val genreCount = user.reviewList
            .filter { it.theme.themeGenreList
                .any { theme -> theme.genre.genreName == genre.kor() }
            }.size
        if (user.isNotGotten(badge) &&
            genreCount == 3
        ) {
            log.info("${user.nickname}님이 ${badge.kor()} 뱃지 획득!")
            user.addBadge(badge)
        }
    }

    private fun checkReviewBadge(user: User) {
        // 첫 리뷰, 엑시터
        if (user.isNotGotten(BadgeEnum.EIXTER) &&
            user.reviewList.size == 1
        ) {
            log.info("${user.nickname}님이 ${BadgeEnum.EIXTER.kor()} 뱃지 획득!")
            user.addBadge(BadgeEnum.EIXTER)
        }

        // 공포 장르 리뷰 3개 작성, 공포매니아
        setGenreBadge(GenreEnum.HORROR, BadgeEnum.HORROR_MANIA)

        // 미스터리 장르 리뷰 3개 작성, 미스터리 러버
        setGenreBadge(GenreEnum.MYSTERY, BadgeEnum.MYSTERY_LOVER)

        // 로맨스 장르 리뷰 3개 작성, 로맨스 홀릭
        setGenreBadge(GenreEnum.ROMANCE, BadgeEnum.ROMANCE_HOLIC)

        // TODO : 리뷰 신고 10개 처리, 엑시트 보안관

    }

    private fun checkInquiryBadge(user: User) {
        // 카페 등록 요청 5번 등록 요청, 트렌드세터
        val condition = user.inquiryList
            .filter { it.category == InquiryCategoryEnum.CAFE.kor() }
            .size

        if (user.isNotGotten(BadgeEnum.TREND_SETTER) &&
            condition == 5
        ) {
            log.info("${user.nickname}님이 ${BadgeEnum.TREND_SETTER.kor()} 뱃지 획득!")
            user.addBadge(BadgeEnum.TREND_SETTER)
        }
    }

}