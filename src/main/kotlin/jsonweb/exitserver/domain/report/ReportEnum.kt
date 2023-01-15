package jsonweb.exitserver.domain.report

enum class ReportEnum(private val kor: String) {
    IRRELEVANT("관련 없는 내용이에요"),
    AD_PROMO("광고 / 홍보성 게시글이에요"),
    SEXUAL_VIOLENCE_AVERSION("선정적이거나 폭력, 혐오적인 내용이에요"),
    COPYRIGHT_INFRINGEMENT("무단 도용, 저작권 침해가 의심되어요"),
    PERSONAL_INFORMATION("개인정보 노출이 우려되어요");

    fun kor() = kor
}

enum class ReportStatus {
    WAITING, RESOLVED
}