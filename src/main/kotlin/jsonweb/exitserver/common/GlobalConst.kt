package jsonweb.exitserver.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

const val TEST_ADMIN_KAKAO_ID = -1L
const val TEST_USER_KAKAO_ID = -2L
