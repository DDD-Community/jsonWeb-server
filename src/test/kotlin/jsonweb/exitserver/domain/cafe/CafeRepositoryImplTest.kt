package jsonweb.exitserver.domain.cafe

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.config.TestConfig
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.TestPropertySource

private val logger = KotlinLogging.logger {}

@Import(TestConfig::class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = ["spring.config.location = classpath:application-test.yml"])
class CafeRepositoryImplTest(
    @Autowired
    private val cafeRepositoryImpl: CafeRepositoryImpl,
    @Autowired
    private val cafeRepository: CafeRepository,
    @Autowired
    private val themeRepository: ThemeRepository
) : AnnotationSpec() {

    @BeforeAll
    fun `초기 설정`() {
        for (i in 4..6) {
            val tempCafe = Cafe("카페$i", "주소$i", "0", "asdf.net", "")
            for (j in 1..5) {
                tempCafe.addTheme(
                    Theme(
                        "테마$j",
                        "설명",
                        "",
                        100,
                        2,
                        5,
                        0.0,
                        "",
                        tempCafe
                    )
                )
            }
            cafeRepository.save(tempCafe)
        }
    }

    @Test
    fun `querydsl keyword 검색 잘 하는지 테스트 1`() {
        val pageable = PageRequest.of(
            0,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepositoryImpl.getList("카페", pageable)
        cafeList.count() shouldBe 2
        cafeList.isLast shouldBe false
        cafeList.totalElements shouldBe 3L
    }

    @Test
    fun `querydsl keyword 검색 잘 하는지 테스트 2`() {
        val pageable = PageRequest.of(
            1,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepositoryImpl.getList("카페", pageable)
        cafeList.count() shouldBe 1
        cafeList.isLast shouldBe true
        cafeList.totalElements shouldBe 3L
    }

    @Test
    fun `querydsl keyword 검색 잘 하는지 테스트 3`() {
        val pageable = PageRequest.of(
            0,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepositoryImpl.getList("6", pageable)
        cafeList.count() shouldBe 1
        cafeList.isLast shouldBe true
    }

    @Test
    fun `querydsl keyword 검색 잘 하는지 테스트 4`() {
        val pageable = PageRequest.of(
            0,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepositoryImpl.getList("주소5", pageable)
        cafeList.count() shouldBe 1
        cafeList.isLast shouldBe true
    }

    @Test
    fun `querydsl keyword 검색 잘 하는지 테스트 5`() {
        val pageable = PageRequest.of(
            0,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepositoryImpl.getList("테마3", pageable)
        cafeList.count() shouldBe 2
        cafeList.isLast shouldBe false
        cafeList.totalElements shouldBe 3L
    }
}
