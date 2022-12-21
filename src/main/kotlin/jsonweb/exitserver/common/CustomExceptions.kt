package jsonweb.exitserver.common

const val USER_NOT_FOUND = "user not found."
class UserException(message: String) : RuntimeException(message)

const val INQUIRY_CANCEL_ERROR = "only could cancel during wait status."
class InquiryException(message: String) : RuntimeException(message)