package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.Cafe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface CafeRepository: JpaRepository<Cafe, Long> {
}