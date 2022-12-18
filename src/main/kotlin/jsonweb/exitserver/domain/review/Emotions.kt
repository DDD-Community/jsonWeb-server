package jsonweb.exitserver.domain.review

data class Emotions(
    val list: List<String>
) {
    constructor() : this(
        list = mutableListOf(
            "재미있어요",
            "무서워요",
            "신선해요",
            "슬퍼요",
            "여운이남아요"
        )
    )
}
