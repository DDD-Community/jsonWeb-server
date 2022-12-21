package jsonweb.exitserver.domain.inquiry

import org.springframework.data.jpa.repository.JpaRepository

interface InquiryRepository : JpaRepository<Inquiry, Long> {
}