package jsonweb.exitserver.common

const val INQUIRY_CANCEL_ERROR = "only could cancel during wait status."
class InquiryException(message: String) : RuntimeException(message)