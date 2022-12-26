package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.user.User
import javax.persistence.*

enum class InquiryStatus(private val type: String) {
    WAITING("waiting"),
    RESOLVED("resolved");

    fun type() = type
}

val INQUIRY_CATEGORIES = mutableListOf(
    "카페 등록 요청",
    "정보 수정 요청",
    "서비스 관련",
    "기타 문의 사항"
)

@Entity
class Inquiry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val inquiryId: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    category: String,
    title: String,
    content: String
) : BaseTimeEntity() {
    var category: String = category
        protected set
    var title: String = title
        protected set
    var content: String = content
        protected set

    @Enumerated(value = EnumType.STRING)
    var status: InquiryStatus = InquiryStatus.WAITING
        protected set
    var answer: String = ""
        protected set

    /**
     * methods
     */
    fun resolve() {
        this.status = InquiryStatus.RESOLVED
    }

    fun update(newCategory: String, newTitle: String, newContent: String) {
        this.category = newCategory
        this.title = newTitle
        this.content = newContent
    }

    fun addAnswer(answer: String) {
        this.answer = answer
    }
}