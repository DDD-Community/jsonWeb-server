package jsonweb.exitserver.common

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass
import javax.persistence.PrePersist
import javax.persistence.PreUpdate


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {

    var createdAt: String = ""
    var modifiedAt: String = ""

    private fun dateFormat(): String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    @PrePersist
    fun prePersist() {
        createdAt = dateFormat()
        modifiedAt = dateFormat()
    }

    @PreUpdate
    fun preUpdate() {
        modifiedAt = dateFormat()
    }
}
