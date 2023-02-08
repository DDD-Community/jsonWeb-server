package jsonweb.exitserver.util.badge

data class BadgeResponse(
    val badge: String = "",
    val requirement: String,
    val order: Int,
    var isObtained: Boolean = false,
)