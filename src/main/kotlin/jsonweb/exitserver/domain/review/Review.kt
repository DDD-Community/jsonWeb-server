package jsonweb.exitserver.domain.review

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import java.io.Serializable
import javax.persistence.*

@Entity
class Review(
    content: String,
    star: Double,
    difficulty: Double,
    emotionFirst: String,
    emotionSecond: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    var theme: Theme

) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    val id: Long = 0L

    var content: String = content
        protected set

    var star: Double = star
        protected set

    var difficulty: Double = difficulty
        protected set

    var emotionFirst = emotionFirst
        protected set

    var emotionSecond = emotionSecond
        protected set

    var likeCount: Int = 0
        protected set

    /**
     * methods
     */
    fun plusLike() {
        likeCount++
    }

    fun minusLike() {
        likeCount--
    }

    fun editReview(emotionFirst: String, emotionSecond: String, content: String, star: Double, difficulty: Double) {
        this.emotionFirst = emotionFirst
        this.emotionSecond = emotionSecond
        this.content = content
        this.star = star
        this.difficulty = difficulty
    }
}

@Embeddable
data class UserAndReview(
    private val userId: Long,
    private val reviewId: Long
) : Serializable

@Entity
@IdClass(UserAndReview::class)
class ReviewLike(
    @Id
    val userId: Long,
    @Id
    val reviewId: Long
)