package jsonweb.exitserver.domain.user

import jsonweb.exitserver.domain.boast.Boast
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long = 0L

    var profileImageUrl: String = ""
        protected set
    var exp: Int = 0
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

    @Enumerated(EnumType.STRING)
    var role: Role = Role.ROLE_USER
        protected set


    /**
     * methods
     */
    fun updateUserInfo(newNickname: String? = null, newProfileImageUrl: String? = null) {
        newNickname?.let { nickname = newNickname }
        newProfileImageUrl?.let { profileImageUrl = newProfileImageUrl }
    }

    fun addInquiry(inquiry: Inquiry) {
       inquiryMutableList.add(inquiry)
    }

    fun deleteInquiry(inquiry: Inquiry) {
        inquiryMutableList.remove(inquiry)
    }

    fun clearInquiry() {
        inquiryMutableList.clear()
    }

    fun setAdmin() {
        role = Role.ROLE_ADMIN
    }

    fun addMyBoast(boast: Boast) {
        myBoastMutableList.add(boast)
    }

    fun clearMyBoast() {
        myBoastMutableList.clear()
    }
}
