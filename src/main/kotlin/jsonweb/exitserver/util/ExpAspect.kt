package jsonweb.exitserver.util

import jsonweb.exitserver.domain.user.UserService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Exp (
    val amount: Int = 0,
    val reason: String = ""
)

@Aspect
@Component
class ExpAspect(private val userService: UserService) {
    @AfterReturning("@annotation(jsonweb.exitserver.util.Exp)")
    fun addExp(joinPoint: JoinPoint) {
        val signature = joinPoint.signature as MethodSignature
        userService.getCurrentLoginUser().addExp(
            signature.method.getAnnotation(Exp::class.java).amount,
            signature.method.getAnnotation(Exp::class.java).reason
        )
    }
}