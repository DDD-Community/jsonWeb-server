package jsonweb.exitserver.domain.review

enum class Emotions(private val emotion: String, private val emoji: String, private val pastTense: String) {
    FUN("재미있어요", "\uD83D\uDE09", "재미있어했어요"),
    SCARY("무서워요", "\uD83D\uDE09", "무서워했어요"),
    FRESH("신선해요", "\uD83D\uDE09", "신선했어요"),
    SAD("슬퍼요", "\uD83D\uDE09", "슬퍼했어요"),
    TOUCH("여운이남아요", "\uD83D\uDE09", "여운이남았어요");

    fun getEmotion() = emotion
    fun getEmoji() = emoji
    fun getPastTense() = pastTense
}
