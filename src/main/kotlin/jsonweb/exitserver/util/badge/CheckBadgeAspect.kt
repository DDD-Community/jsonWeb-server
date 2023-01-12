package jsonweb.exitserver.util.badge

import jsonweb.exitserver.common.logger
import jsonweb.exitserver.domain.user.UserService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

enum class BadgeType {
    BOAST, INQUIRY, REVIEW
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckBadge(
    val type: BadgeType
)

@Aspect
@Component
class CheckBadgeAspect(private val userService: UserService) {
    val log = logger()

    @AfterReturning("@annotation(jsonweb.exitserver.util.badge.CheckBadge)")
    fun checkBadge(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        when (signature.method.getAnnotation(CheckBadge::class.java).type) {
            BadgeType.BOAST -> checkBoastBadge()
            else -> {}
        }

    }

    @Transactional
    fun checkBoastBadge() {
        val user = userService.getCurrentLoginUser()

        // 인증 글 10개 작성 시 탈출중독 뱃지 획득
        if (user.isNotGotten(BadgeEnum.ADDICTED_ESCAPE) &&
            user.myBoastList.size == 10
        ) {
            log.info("${user.nickname}님이 탈출중독 뱃지 획득.")
            user.addBadge(BadgeEnum.ADDICTED_ESCAPE)
        }

        // 난이도 5 탈출 인증 시 천상계 뱃지 획득
        if (user.isNotGotten(BadgeEnum.HIGHEST_REALM) &&
            user.myBoastList.any { it.theme.difficulty == 5.0 }
        ) {
            log.info("${user.nickname}님이 천상계 뱃지 획득.")
            user.addBadge(BadgeEnum.HIGHEST_REALM)
        }
    }

}