package jsonweb.exitserver.domain.review

enum class EmotionEnum(
    private val kor: String,
    private val emoji: String,
    private val pastTense: String
) {
    FUN("재미있어요", "\uD83D\uDE09", "재미있어했어요"),
    SCARY("무서워요", "\uD83D\uDE28", "무서워했어요"),
    FRESH("신선해요", "\uD83E\uDD29", "신선했어요"),
    SAD("슬퍼요", "\uD83E\uDD72", "슬퍼했어요"),
    TOUCH("여운이남아요", "\uD83D\uDE27", "여운이남았어요"),
    BLANK("", "", "");

    companion object {
        private val emotionEnumMap: Map<String, EmotionEnum> = values().associateBy { it.kor }
        fun findByEmotion(emotion: String) = emotionEnumMap[emotion]
    }

    fun kor() = kor
    fun emoji() = emoji
    fun pastTense() = pastTense
}
