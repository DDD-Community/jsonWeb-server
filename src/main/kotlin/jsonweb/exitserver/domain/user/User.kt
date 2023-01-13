package jsonweb.exitserver.domain.user

import jsonweb.exitserver.domain.boast.Boast
import jsonweb.exitserver.domain.inquiry.Inquiry
import jsonweb.exitserver.domain.review.Review
import jsonweb.exitserver.util.badge.Badge
import jsonweb.exitserver.util.badge.BadgeEnum
import javax.persistence.*

enum class Role { ROLE_USER, ROLE_ADMIN }
enum class UserLevel(private val levelName: String, private val needExp: Int) {
    LEVEL_1("초보", 100),
    LEVEL_2("중수", 200),
    LEVEL_3("고수", 300),
    LEVEL_4("초고수", 400);
    fun getLevelName() = levelName
    fun getNeedExp() = needExp
}

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
    var level: String = UserLevel.LEVEL_1.getLevelName()
        protected set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val myBoastMutableList: MutableList<Boast> = mutableListOf()
    val myBoastList: List<Boast> get() = myBoastMutableList.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val reviewMutableList: MutableList<Review> = mutableListOf()
    val reviewList: List<Review> get() = reviewMutableList.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val inquiryMutableList: MutableList<Inquiry> = mutableListOf()
    val inquiryList: List<Inquiry> get() = inquiryMutableList.toList()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val badgeMutableSet: MutableSet<Badge> = mutableSetOf()
    val badgeList: List<Badge> get() = badgeMutableSet.toList()


    @Enumerated(EnumType.STRING)
    var role: Role = Role.ROLE_USER
        protected set


    /**
     * methods
     */
    // user
    @PostUpdate
    fun checkUserLevel() {
        level = if (exp >= UserLevel.LEVEL_4.getNeedExp()) {
            UserLevel.LEVEL_4.getLevelName()
        } else if (exp >= UserLevel.LEVEL_3.getNeedExp()) {
            UserLevel.LEVEL_3.getLevelName()
        } else if (exp >= UserLevel.LEVEL_2.getNeedExp()) {
            UserLevel.LEVEL_2.getLevelName()
        } else {
            UserLevel.LEVEL_1.getLevelName()
        }
    }

    fun addExp(amount: Int) {
        exp += amount
    }

    fun addBadge(badgeEnum: BadgeEnum) {
        badgeMutableSet.add(badgeEnum.toEntity(this))
    }
    
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
        myBoastMutableList.add(boast)
    }

    fun clearMyBoast() {
        myBoastMutableList.clear()
    }

    // review
    fun addReview(review: Review) {
        reviewMutableList.add(review)
    }

}
