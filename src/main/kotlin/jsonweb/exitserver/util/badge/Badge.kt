package jsonweb.exitserver.util.badge

import jsonweb.exitserver.common.BaseTimeEntity
import jsonweb.exitserver.domain.user.User
import javax.persistence.*

@Entity
class Badge(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val badgeId: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    val badge: String,
) : BaseTimeEntity()

