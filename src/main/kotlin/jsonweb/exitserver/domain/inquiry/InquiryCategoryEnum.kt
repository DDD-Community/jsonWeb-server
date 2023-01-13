package jsonweb.exitserver.domain.inquiry

enum class InquiryCategoryEnum(private val kor: String) {
    CAFE("카페 등록 요청"),
    EDIT("정보 수정 요청"),
    SERVICE("서비스 관련"),
    ETC("기타 문의 사항");
    fun kor() = kor
}