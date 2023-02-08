package jsonweb.exitserver.util.badge

data class BadgeResponse(
    var badge: String = "",
    val requirement: String,
    val order: Int,
    var isObtained: Boolean = false,
)