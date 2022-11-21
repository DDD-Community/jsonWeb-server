package jsonweb.exitserver

import org.springframework.data.jpa.repository.JpaRepository

interface TestRepository: JpaRepository<Test, Long> {
}