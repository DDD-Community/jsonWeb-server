package jsonweb.exitserver.domain.review

enum class Emotions(private val emotion: String) {
    FUN("재미있어요"),
    SCARY("무서워요"),
    FRESH("신선해요"),
    SAD("슬퍼요"),
    TOUCH("여운이남아요");
    fun getEmotion() = emotion
}
