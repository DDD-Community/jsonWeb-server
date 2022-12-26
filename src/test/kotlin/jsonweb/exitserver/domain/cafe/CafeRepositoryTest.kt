package jsonweb.exitserver.domain.cafe

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import jsonweb.exitserver.config.TestConfig
import jsonweb.exitserver.domain.review.ReviewRepository
import jsonweb.exitserver.domain.theme.GenreRepository
import jsonweb.exitserver.domain.theme.ThemeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.TestPropertySource

@Import(TestConfig::class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = ["spring.config.location = classpath:application-test.yml"])
class CafeRepositoryTest(
    @Autowired
    private val cafeRepository: CafeRepository,
    @Autowired
    private val themeRepository: ThemeRepository,
    @Autowired
    private val reviewRepository: ReviewRepository,
    @Autowired
    private val genreRepository: GenreRepository
) : AnnotationSpec() {
    @BeforeAll
    fun `초기 설정`() {
        for (i: Int in 1..5) {
            val tempCafe = Cafe("a", "a", "a", "a", "a")
            repeat(i) {
                tempCafe.increaseReviewCount()
                /*
                1, 2, 3, 4, 5
                */
            }
            cafeRepository.save(tempCafe)
        }
    }

    @Test
    fun `리뷰수로 정렬되서 리스트 나오는지 테스트`() {
        val pageable = PageRequest.of(
            0,
            5,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val result = cafeRepository.findAll(pageable).toList()
        val answer = listOf(5L, 4L, 3L, 2L, 1L)
        for (i in 0..4) {
            result[i].cafeId shouldBe answer[i]
        }
    }

    @Test
    fun `리뷰수로 페이징 처리도 잘 되는지 테스트 1`() {
        val pageable = PageRequest.of(
            0,
            3,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val result = cafeRepository.findAll(pageable)

        result.isLast shouldBe false // check isLast
        result.totalElements shouldBe 5

        val answer = listOf(5L, 4L, 3L)
        for (i in 0..2) {
            result.toList()[i].cafeId shouldBe answer[i]
        }
    }

    @Test
    fun `리뷰수로 페이징 처리도 잘 되는지 테스트 2`() {
        val pageable = PageRequest.of(
            1,
            3,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val result = cafeRepository.findAll(pageable)

        result.isLast shouldBe true // check isLast
        result.totalElements shouldBe 5

        val answer = listOf(2L, 1L)
        for (i in 0..1) {
            result.toList()[i].cafeId shouldBe answer[i]
        }
    }

}