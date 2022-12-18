package jsonweb.exitserver.domain.user

import jsonweb.exitserver.domain.inquiry.Inquiry
import jsonweb.exitserver.domain.review.Review
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L

    var profileImageUrl: String = ""
    var exp: Int = 0

    @Enumerated(EnumType.STRING)
    var role: Role = Role.ROLE_USER

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviewList: MutableList<Review> = mutableListOf()

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var inquiryList: MutableList<Inquiry> = mutableListOf()

    /**
     * methods
     */
    fun updateUserInfo(newNickname: String? = null, newProfileImageUrl: String? = null) {
        newNickname?.let { nickname = newNickname }
        newProfileImageUrl?.let { profileImageUrl = newProfileImageUrl }
    }

    fun addInquiry(inquiry: Inquiry) {
        this.inquiryList.add(inquiry)
    }
}
