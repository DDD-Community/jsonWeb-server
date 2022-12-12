package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.CafeReport
import org.springframework.data.jpa.repository.JpaRepository

interface CafeReportRepository: JpaRepository<CafeReport, Long> {
}