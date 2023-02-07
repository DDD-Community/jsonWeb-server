package jsonweb.exitserver.util

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse



@Component
class IpLogInterceptor : HandlerInterceptor {
    val log = logger()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("${request.getIp()} : ${request.method} ${request.requestURI}")
        return true
    }
}