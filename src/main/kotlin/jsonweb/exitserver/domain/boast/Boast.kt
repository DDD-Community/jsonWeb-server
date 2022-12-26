package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.User
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
) : BaseTimeEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    var theme: Theme = theme
        protected set

    var likeCount: Int = 0
        protected set

    var visibility: Boolean = true
        protected set

    @OneToMany(mappedBy = "boast", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val boastImageMutableList: MutableList<BoastImage> = mutableListOf()
    val boastImageList: List<BoastImage> get() = boastImageMutableList.toList()

    @OneToMany(mappedBy = "boast", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val hashtagMutableList: MutableList<BoastHashtag> = mutableListOf()
    val hashtagList: List<BoastHashtag> get() = hashtagMutableList.toList()

    /**
     * methods
     */
    fun addImage(image: BoastImage) {
        boastImageMutableList.add(image)
    }

    fun addHashtag(hashtag: BoastHashtag) {
        hashtagMutableList.add(hashtag)
    }

    fun update(form: BoastRequest, theme: Theme) {
        boastImageMutableList.clear()
        hashtagMutableList.clear()
        this.theme = theme
        form.imageUrls.forEach {
            addImage(BoastImage(imageUrl = it, boast = this))
        }
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
class BoastImage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boast_image_id")
    val id: Long = 0L,

    val imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boast_id")
    val boast: Boast,
)

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

@Entity
class BoastReport(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boast_report_id")
    val id: Long = 0L,

    val reportContent: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boast_id")
    val boast: Boast
)

