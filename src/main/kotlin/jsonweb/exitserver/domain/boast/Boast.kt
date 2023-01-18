package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
import org.hibernate.annotations.BatchSize
import java.io.Serializable
import javax.persistence.*

@Entity
class Boast(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boast_id")
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    theme: Theme,
    imageUrl: String
) : BaseTimeEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    var theme: Theme = theme
        protected set

    var likeCount: Int = 0
        protected set

    var imageUrl: String = imageUrl
        protected set

    var visibility: Boolean = true
        protected set

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "boast", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val hashtagMutableList: MutableList<BoastHashtag> = mutableListOf()
    val hashtagList: List<BoastHashtag> get() = hashtagMutableList.toList()

    /**
     * methods
     */
    fun addImage(imageUrl: String) {
        this.imageUrl = imageUrl
    }

    fun addHashtag(hashtag: BoastHashtag) {
        hashtagMutableList.add(hashtag)
    }

    fun update(form: BoastRequest, theme: Theme) {
        hashtagMutableList.clear()
        this.theme = theme
        this.imageUrl = form.imageUrl
        form.hashtags.forEach {
            addHashtag(BoastHashtag(hashtag = "#$it", boast = this))
        }
    }

    fun plusLike() {
        likeCount++
    }

    fun minusLike() {
        likeCount--
    }

    fun setInvisible() {
        visibility = false
    }

    fun setVisible() {
        visibility = true
    }
}

@Entity
class BoastHashtag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boast_hashtag_id")
    val id: Long = 0L,

    val hashtag: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boast_id")
    val boast: Boast,
)

@Embeddable
data class BoastLikeId(
    private val userId: Long,
    private val boastId: Long
) : Serializable

@Entity
@IdClass(BoastLikeId::class)
class BoastLike(
    @Id
    val userId: Long,

    @Id
    val boastId: Long,
)

