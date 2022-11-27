package jsonweb.exitserver.domain.user

import jsonweb.exitserver.domain.review.Review
import javax.persistence.*

@Entity
class User(
    val gender: String,
    val age: Int,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long = 0L

    var nickname: String = ""
    var profileImageUrl: String = ""
    var exp: Int = 0

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviewList: MutableList<Review> = mutableListOf()
}