package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.Cafe
import jsonweb.exitserver.domain.review.Review
import javax.persistence.*
import kotlin.math.round

@Entity
class Theme(
    var name: String,
    var description: String,
    var imageUrl: String,
    var time: Int,
    var minPlayerCount: Int,
    var maxPlayerCount: Int,
    var difficulty: Double,
    var ageLimit: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    var cafe: Cafe
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    val id: Long = 0L
    var reviewCount: Int = 0
        protected set
    var avgStar: Double = 0.0
        protected set

    @OneToMany(mappedBy = "theme", cascade = [CascadeType.ALL], orphanRemoval = true)
    var themeGenreList: MutableList<ThemeGenre> = mutableListOf()

    @OneToMany(mappedBy = "theme", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviewList: MutableList<Review> = mutableListOf()

    /**
     * methods
     */
    fun addThemeGenre(themeGenre: ThemeGenre) = themeGenreList.add(themeGenre)

    fun addReview(review: Review) {
        reviewList.add(review)
        val totalStar = avgStar * reviewCount + review.star
        reviewCount = reviewList.size
        avgStar = round((totalStar / reviewCount * 10) / 10)
    }


    fun deleteReview(review: Review) {
        if (reviewCount == 1) {
            reviewList.remove(review)
            reviewCount = 0
            avgStar = 0.0
        } else {
            val totalStar = avgStar * reviewCount - review.star
            reviewList.remove(review)
            reviewCount = reviewList.size
            avgStar = round((totalStar / reviewCount * 10) / 10)
        }
    }

    // for test
    constructor(): this("", "", "", 0, 0, 0, 0.0, "", Cafe())
}

@Entity
class ThemeGenre(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    val theme: Theme,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    val genre: Genre,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_genre_id")
    val id: Long = 0L
}

@Entity
class Genre(
    val genreName: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    val id: Long = 0L
}


