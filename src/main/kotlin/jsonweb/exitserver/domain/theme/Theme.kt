package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.entity.Cafe
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

    // 장르에서는 테마를 참조할 경우가 없을 거 같아서 단방향으로 해도 될 듯 싶습니다
//    @OneToMany(mappedBy = "genre", cascade = [CascadeType.ALL], orphanRemoval = true)
//    var themeGenreList: MutableList<ThemeGenre> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val genreId: Long = 0L
}


