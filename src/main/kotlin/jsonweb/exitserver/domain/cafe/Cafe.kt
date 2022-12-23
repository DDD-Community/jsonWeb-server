package jsonweb.exitserver.domain.cafe.entity

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.theme.Theme
import java.io.Serializable
import javax.persistence.*
import kotlin.math.round

@Entity
class Cafe(
    name: String,
    address: String,
    tel: String,
    homepage: String,
    imageUrl: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cafeId: Long = 0L

    var name: String = name
        protected set
    var address: String = address
        protected set
    var themeCount: Int = 0
        protected set
    var avgStar: Double = 0.0
        protected set
    var tel: String = tel
        protected set
    var homepage: String = homepage
        protected set
    var reviewCount: Int = 0
        protected set
    var imageUrl: String = imageUrl
        protected set

    @OneToMany(mappedBy = "cafe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var themeList: MutableList<Theme> = mutableListOf()
        protected set

    @OneToMany(mappedBy = "cafe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var openHourList: MutableList<OpenHour> = mutableListOf()
        protected set

    @OneToMany(mappedBy = "cafe", cascade = [CascadeType.ALL], orphanRemoval = true)
    var priceList: MutableList<Price> = mutableListOf()
        protected set

    /**
     * methods
     */
    fun addPrice(price: Price) = priceList.add(price)
    fun addOpenHour(openHour: OpenHour) = openHourList.add(openHour)
    fun addTheme(theme: Theme) {
        themeList.add(theme)
        themeCount = themeList.size
    }
    fun increaseReviewCount() {
        reviewCount++
    }
    fun decreaseReviewCount() {
        reviewCount--
    }
    fun addReview(star: Double) {
        val totalStar = avgStar * reviewCount + star
        reviewCount++
        avgStar = round((totalStar / reviewCount * 10) / 10)
    }

    fun deleteReview(star: Double) {
        if (reviewCount == 1) {
            reviewCount = 0
            avgStar = 0.0
        } else {
            val totalStar = avgStar * reviewCount - star
            reviewCount--
            avgStar = round((totalStar / reviewCount * 10) / 10)
        }
    }

    fun editStar(starDiff: Double) {
        val totalStar = avgStar * reviewCount + starDiff
        avgStar = round((totalStar / reviewCount * 10) / 10)
    }

}

@Entity
class OpenHour(
    day: String,
    openHour: String,
    closeHour: String,
    cafe: Cafe
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val openHourId: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    var cafe: Cafe = cafe
        protected set

    var day: String = day
        protected set
    var openHour: String = openHour
        protected set
    var closeHour: String = closeHour
        protected set

    /**
     * methods
     */
}

@Entity
class Price(
    headCount: String,
    day: String,
    price: Int,
    cafe: Cafe
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val priceId: Long = 0L
    var headCount: String = headCount
        protected set
    var day: String = day
        protected set
    var price: Int = price
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    var cafe: Cafe = cafe
        protected set

    /**
     * methods
     */
}

@Embeddable
data class UserAndCafe(
    private val userId: Long,
    private val cafeId: Long
): Serializable

@Entity
@IdClass(UserAndCafe::class)
class CafeLike(
    @Id
    val userId: Long,
    @Id
    val cafeId: Long
) : BaseTimeEntity()

@Entity
class CafeReport(
    private val cafeId: Long,
    private val reportContent: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val reportId: Long = 0L
}