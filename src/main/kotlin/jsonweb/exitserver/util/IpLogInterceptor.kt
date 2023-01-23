package jsonweb.exitserver.util

import jsonweb.exitserver.common.logger
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse



@Component
class IpLogInterceptor : HandlerInterceptor {
    val log = logger()
    private fun HttpServletRequest.getIp(): String =
        this.getHeader("X-Forwarded-For")
            ?: this.getHeader("Proxy-Client-IP")
            ?: this.getHeader("WL-Proxy-Client-IP")
            ?: this.getHeader("HTTP_CLIENT_IP")
            ?: this.getHeader("HTTP_X_FORWARDED_FOR")
            ?: this.remoteAddr

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("${request.getIp()} : ${request.method} ${request.requestURI}")
        return true
    }
}