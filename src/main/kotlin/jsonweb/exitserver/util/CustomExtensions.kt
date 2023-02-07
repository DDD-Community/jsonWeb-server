package jsonweb.exitserver.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletRequest

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

fun HttpServletRequest.getIp(): String = this.getHeader("X-Forwarded-For")
        ?: this.getHeader("Proxy-Client-IP")
        ?: this.getHeader("WL-Proxy-Client-IP")
        ?: this.getHeader("HTTP_CLIENT_IP")
        ?: this.getHeader("HTTP_X_FORWARDED_FOR")
        ?: this.remoteAddr

fun String.toDotFormat() = this.split(" ")[0].replace("-", ".")

