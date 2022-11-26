package jsonweb.exitserver.common

data class CommonResponse<T>(
    var message: String = "ok",
    var data: T,
)

