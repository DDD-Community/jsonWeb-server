package jsonweb.exitserver.domain.cafe

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import jsonweb.exitserver.config.TestConfig
import jsonweb.exitserver.domain.theme.*
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
//    @Autowired
//    private val cafeRepositoryCustomImpl: CafeRepositoryCustomImpl,
    @Autowired
    private val cafeRepository: CafeRepository,
    @Autowired
    private val themeRepository: ThemeRepository,
    @Autowired
    private val genreRepository: GenreRepository,
    @Autowired
    private val themeGenreRepository: ThemeGenreRepository
) : AnnotationSpec() {

    @BeforeAll
    fun `초기 설정`() {

//        val genres: Array<String> = arrayOf("장르1", "장르2", "장르3", "장르4", "장르5", "장르6", "장르7", "장르8")
//        val genreEntities = arrayOfNulls<Genre>(8)
//        for (i: Int in 0..7) {
//            genreEntities[i] = genreRepository.save(Genre(genres[i]))
//        }
        val genre1 = genreRepository.save(Genre("장르1"))
        val genre2 = genreRepository.save(Genre("장르2"))

        for (i in 4..7) {
            val tempCafe = Cafe("카페$i", "주소$i", "0", "home.net", "")
            for (j in 1..5) {
                val theme = Theme(
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

                if (i <= 6) {
                    theme.addThemeGenre(ThemeGenre(theme, genre1))
                } else {
                    theme.addThemeGenre(ThemeGenre(theme, genre2))
                }
                tempCafe.addTheme(theme)
            }
            cafeRepository.save(tempCafe)
        }
    }

    @Test
    fun `querydsl 장르 검색 잘하는지`() {
        val pageable = PageRequest.of(
            0,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepository.getListWithGenreName("장르1", pageable)
        cafeList.count() shouldBe 2
        cafeList.totalElements shouldBe 3L
        cafeList.isLast shouldBe false
    }

    @Test
    fun `querydsl keyword 검색 잘 하는지 테스트 1`() {
        val pageable = PageRequest.of(
            0,
            2,
            Sort.by(CafeSort.valueOf("REVIEW").getDirection(), CafeSort.valueOf("REVIEW").getSortBy())
                .and(Sort.by("cafeId"))
        )
        val cafeList = cafeRepository.getList("카페", pageable)
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
        val cafeList = cafeRepository.getList("카페", pageable)
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
        val cafeList = cafeRepository.getList("6", pageable)
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
        val cafeList = cafeRepository.getList("주소5", pageable)
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
        val cafeList = cafeRepository.getList("테마3", pageable)
        cafeList.count() shouldBe 2
        cafeList.isLast shouldBe false
        cafeList.totalElements shouldBe 3L
    }
}
