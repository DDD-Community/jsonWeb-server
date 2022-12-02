package jsonweb.exitserver.domain.theme

import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.review.Review
import javax.persistence.*

@Entity
class Theme(
    name: String,
    description: String,
    imageUrl: String,
    time: Int,
    minPlayerCount: Int,
    maxPlayerCount: Int,
    difficulty: Double,
    ageLimit: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    val cafe: Cafe,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val themeId: Long = 0L

    var name: String = name
        protected set
    var description: String = description
        protected set
    var imageUrl: String = imageUrl
        protected set
    var time: Int = time
        protected set
    var minPlayerCount: Int = minPlayerCount
        protected set
    var maxPlayerCount: Int = maxPlayerCount
        protected set
    var difficulty: Double = difficulty
        protected set
    var ageLimit: String = ageLimit
        protected set
    var reviewCount: Int = 0
        protected set
    var isReported: Boolean = false
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

    // 장르에서는 테마를 참조할 경우가 없을 거 같아서 단방향으로 해도 될 듯 싶습니다
//    @OneToMany(mappedBy = "genre", cascade = [CascadeType.ALL], orphanRemoval = true)
//    var themeGenreList: MutableList<ThemeGenre> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val genreId: Long = 0L
}


