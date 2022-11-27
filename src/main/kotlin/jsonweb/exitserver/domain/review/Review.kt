package jsonweb.exitserver.domain.review

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import javax.persistence.*

@Entity
class Review(
    content: String,
    star: Double,
    difficulty: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    val theme: Theme

): BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    val id: Long = -1

    var content: String = content
        protected set

    var star: Double = star
        protected set

    var difficulty: Double = difficulty
        protected set

    var likeCount: Int = 0
        protected set

    /**
     * methods
     */
    fun plusLike() {
        likeCount++
    }

}