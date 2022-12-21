package jsonweb.exitserver.domain.cafe

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import jsonweb.exitserver.config.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@Import(TestConfig::class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = ["spring.config.location = classpath:application-test.yml"])
class CafeLikeRepositoryTest(
    @Autowired
    private val cafeLikeRepository: CafeLikeRepository
) : AnnotationSpec() {
    @Test
    fun `좋아요 등록 테스트`() {
        cafeLikeRepository.save(CafeLike(1L, 2L))
        cafeLikeRepository.existsById(UserAndCafe(1L, 2L)) shouldBe true
    }

    @Test
    fun `좋아요 삭제 테스트`() {
        cafeLikeRepository.save(CafeLike(1L, 2L))
        cafeLikeRepository.deleteById(UserAndCafe(1L, 2L))
        cafeLikeRepository.existsById(UserAndCafe(1L, 2L)) shouldBe false
    }

    @Test
    fun `좋아요 없는지 테스트`() {
        cafeLikeRepository.existsById(UserAndCafe(3L, 2L)) shouldBe false
    }

    @Test
    fun `좋아요 리스트 테스트`() {
        cafeLikeRepository.save(CafeLike(1L, 2L))
        cafeLikeRepository.save(CafeLike(1L, 3L))
        val list = cafeLikeRepository.findAllByUserId(1L)
        list.size shouldBe 2
        list[0].cafeId shouldBe 2L
        list[1].cafeId shouldBe 3L
    }
}