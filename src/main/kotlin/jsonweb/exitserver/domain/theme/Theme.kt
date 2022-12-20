package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.Cafe
import jsonweb.exitserver.domain.review.Review
import javax.persistence.*

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
    val themeId: Long = 0L
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
        reviewCount = reviewList.size
    }

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
    val themeGenreId: Long = 0L
}

@Entity
class Genre(
    val genreName: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val genreId: Long = 0L
}


