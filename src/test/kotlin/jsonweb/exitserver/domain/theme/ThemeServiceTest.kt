package jsonweb.exitserver.domain.theme

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.mpp.log
import jsonweb.exitserver.domain.cafe.CafeRepository
import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.review.Emotions
import jsonweb.exitserver.util.ModelMapperConfig
import jsonweb.exitserver.util.TestConfig

import org.junit.jupiter.api.Assertions.*
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@Import(TestConfig::class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = ["spring.config.location = classpath:application-test.yml"])
class ThemeServiceTest(
    @Autowired
    private val cafeRepository: CafeRepository,
    @Autowired
    private val themeRepository: ThemeRepository,
    @Autowired
    private val genreRepository: GenreRepository,
    @Autowired
    private val themeGenreRepository: ThemeGenreRepository,
//    @Autowired
//    private val modelMapper: ModelMapper
) : AnnotationSpec() {

    @Test
    fun `modelMapper 테스트`() {
//        val themeService = ThemeService(cafeRepository, themeRepository, genreRepository, themeGenreRepository, modelMapper)
        val modelMapper = ModelMapper()
        val testCafe = cafeRepository.save(Cafe(
            "nameC",
            "addressC",
            "telC",
            "homeC",
            "urlC"
        ))
        println("cafeId: " + testCafe.cafeId)
        val testRequest = RegisterThemeRequest(
            testCafe.cafeId,
            "name",
            "desc",
            "url",
            60,
            7,
            10,
            5.0,
            "age",
            listOf("Genre1", "Genre2")
        )

        val testDTO = ThemeWithCafe(testRequest, testCafe)

        testDTO.cafe.cafeId shouldBe 1L

        val theme = modelMapper.map(testDTO, Theme::class.java)
        theme.name shouldBe "name"

        theme.cafe.cafeId shouldBe 1L

        val testTheme = themeRepository.save(theme)

        testTheme.cafe.cafeId shouldBe testCafe.cafeId
    }
}