package jsonweb.exitserver.domain.cafe.entity

import jsonweb.exitserver.domain.theme.Theme
import javax.persistence.*

@Entity
class Cafe(
    name: String,
    address: String,
    tel: String,
    homepage: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    val id: Long = -1

    var name: String = name
        protected set
    var address: String = address
        protected set
    var themeCount: Int = 0
        protected set
    var themeAvgStar: Double = 0.0
        protected set
    var tel: String = tel
        protected set
    var homepage: String = homepage
        protected set
    var totalReviewCount: Int = 0
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
}

@Entity
class OpenHour(
    cafe: Cafe,
    day: String,
    openHour: Int,
    closeHour: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "open_hour_id")
    val id: Long = -1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    var cafe: Cafe = cafe
        protected set

    var day: String = day
        protected set
    var openHour: Int = openHour
        protected set
    var closeHour: Int = closeHour
        protected set

    /**
     * methods
     */
}

@Entity
class Price(
    target: String,
    price: Int,
    cafe: Cafe
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    val id: Long = -1
    var target: String = target
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