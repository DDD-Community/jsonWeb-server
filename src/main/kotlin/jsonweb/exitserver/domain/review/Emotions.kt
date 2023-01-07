package jsonweb.exitserver.domain.review

enum class Emotions(private val emotion: String, private val emoji: String) {
    FUN("재미있어요", "&#128521;"),
    SCARY("무서워요", "&#128521;"),
    FRESH("신선해요", "&#128521;"),
    SAD("슬퍼요", "&#128521;"),
    TOUCH("여운이남아요", "&#128521;");

    fun getEmotion() = emotion
    fun getEmoji() = emoji
}
