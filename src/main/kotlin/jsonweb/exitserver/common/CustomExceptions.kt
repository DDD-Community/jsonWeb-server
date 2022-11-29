package jsonweb.exitserver.common

const val USER_NOT_FOUND = "user not found."
class UserException(message: String) : RuntimeException(message)