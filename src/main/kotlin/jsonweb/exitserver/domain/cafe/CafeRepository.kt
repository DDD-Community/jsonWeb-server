package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.Cafe
import org.springframework.data.jpa.repository.JpaRepository

interface CafeRepository: JpaRepository<Cafe, Long> {
}