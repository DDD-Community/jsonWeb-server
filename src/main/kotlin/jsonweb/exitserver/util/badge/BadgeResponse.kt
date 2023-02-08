package jsonweb.exitserver.util.badge

data class BadgeResponse(
    var badge: String = "",
    val requirement: String,
    var isObtained: Boolean = false,
)