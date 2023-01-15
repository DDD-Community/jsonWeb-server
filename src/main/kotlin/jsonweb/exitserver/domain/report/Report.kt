package jsonweb.exitserver.domain.report

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.boast.Boast
import jsonweb.exitserver.domain.review.Review
import jsonweb.exitserver.domain.user.User
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
class Report(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val reportId: Long = 0L,
    val content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
) : BaseTimeEntity() {

    var status: ReportStatus = ReportStatus.WAITING
        protected set

    fun resolve() {
        status = ReportStatus.RESOLVED
    }
}

@Entity
class BoastReport(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boast_id")
    val boast: Boast,
    content: String,
    user: User,
) : Report(user = user, content = content)

@Entity
class ReviewReport(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    val review: Review,
    content: String,
    user: User
) : Report(user = user, content = content)