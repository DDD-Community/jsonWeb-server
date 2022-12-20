package jsonweb.exitserver.domain.inquiry

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.user.User
import javax.persistence.*

enum class InquiryStatus(private val type: String) {
    WAITING("waiting"),
    PROCEEDING("proceeding"),
    DONE("done"),
    CANCEL("cancel");
    fun type() = type
}

@Entity
class Inquiry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val inquiryId: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val cafeName: String,
    val address: String,
    val description: String
): BaseTimeEntity() {
    @Enumerated(value = EnumType.STRING)
    var status: InquiryStatus = InquiryStatus.WAITING
        protected set

    /**
     * methods
     */
    fun proceeding() {
        this.status = InquiryStatus.PROCEEDING
    }

    fun done() {
        this.status = InquiryStatus.DONE
    }

    fun cancel() {
        this.status = InquiryStatus.CANCEL
    }
}