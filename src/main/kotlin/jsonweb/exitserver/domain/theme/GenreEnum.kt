package jsonweb.exitserver.domain.theme

enum class GenreEnum(private val kor: String) {
    HORROR("공포"),
    MYSTERY("미스터리"),
    ROMANCE("로맨스");
    fun kor() = kor
}