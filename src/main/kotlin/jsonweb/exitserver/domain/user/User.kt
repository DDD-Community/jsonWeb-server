package jsonweb.exitserver.domain.user

import jsonweb.exitserver.domain.boast.Boast
import jsonweb.exitserver.domain.inquiry.Inquiry
import jsonweb.exitserver.domain.report.Report
import jsonweb.exitserver.domain.review.Review
import jsonweb.exitserver.domain.theme.GenreEnum
import jsonweb.exitserver.util.badge.Badge
import jsonweb.exitserver.util.badge.BadgeEnum
import javax.persistence.*

enum class Role { ROLE_USER, ROLE_ADMIN }


@Entity
class User(
    val kakaoId: Long,
    val password: String,
    val gender: String,
    val ageRange: String,
    var nickname: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L

    var profileImageUrl: String = ""
        protected set
    var exp: Int = 0
        protected set
    var level: String = UserLevelEnum.LEVEL_1.getLevelName()
        protected set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val boastMutableList: MutableList<Boast> = mutableListOf()
    val boastList: List<Boast> get() = boastMutableList.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val reviewMutableList: MutableList<Review> = mutableListOf()
    val reviewList: List<Review> get() = reviewMutableList.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val inquiryMutableList: MutableList<Inquiry> = mutableListOf()
    val inquiryList: List<Inquiry> get() = inquiryMutableList.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val badgeMutableSet: MutableSet<Badge> = mutableSetOf()
    val badgeList: List<Badge> get() = badgeMutableSet.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val reportMutableList: MutableList<Report> = mutableListOf()
    val reportList: List<Report> get() = reportMutableList.toList()


    @Enumerated(EnumType.STRING)
    var role: Role = Role.ROLE_USER
        protected set


    /**
     * methods
     */
    // user
    @PostUpdate
    fun checkUserLevel() {
        level = UserLevelEnum.getLevelName(exp)
    }

    fun addExp(amount: Int) {
        exp += amount
    }

    fun addBadge(badgeEnum: BadgeEnum) {
        badgeMutableSet.add(badgeEnum.toEntity(this))
    }

    fun getReviewGenreCount(genreEnum: GenreEnum) = reviewList
        .filter {
            it.theme.themeGenreList
                .any { theme -> theme.genre.genreName == genreEnum.kor() }
        }.size

    fun isNotGotten(badgeEnum: BadgeEnum): Boolean = badgeMutableSet
        .none { it.badge == badgeEnum.kor() }

    fun clearBadge() {
        badgeMutableSet.clear()
    }


    fun updateUserInfo(newNickname: String? = null, newProfileImageUrl: String? = null) {
        newNickname?.let { nickname = newNickname }
        newProfileImageUrl?.let { profileImageUrl = newProfileImageUrl }
    }

    fun setAdmin() {
        role = Role.ROLE_ADMIN
    }

    // inquiry
    fun addInquiry(inquiry: Inquiry) {
        inquiryMutableList.add(inquiry)
    }

    fun deleteInquiry(inquiry: Inquiry) {
        inquiryMutableList.remove(inquiry)
    }

    fun clearInquiry() {
        inquiryMutableList.clear()
    }

    // boast
    fun addMyBoast(boast: Boast) {
        boastMutableList.add(boast)
    }

    fun clearMyBoast() {
        boastMutableList.clear()
    }

    // review
    fun addReview(review: Review) {
        reviewMutableList.add(review)
    }

    // report
    fun addReport(report: Report) {
        reportMutableList.add(report)
    }

}
