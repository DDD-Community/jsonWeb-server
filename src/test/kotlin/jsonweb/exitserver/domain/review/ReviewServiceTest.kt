package jsonweb.exitserver.domain.review

import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.mockk
import jsonweb.exitserver.domain.cafe.CafeRepository
import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserRepository
import jsonweb.exitserver.domain.user.UserService
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
class ReviewServiceTest(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val cafeRepository: CafeRepository,
    @Autowired
    private val themeRepository: ThemeRepository,
    @Autowired
    private val reviewRepository: ReviewRepository,
    @Autowired
    private val reviewRepositoryImpl: ReviewRepositoryImpl
) : AnnotationSpec() {
//    private val userRepository = mockk<UserRepository>()
    private val reviewLikeRepository = mockk<ReviewLikeRepository>()
    private val userService = mockk<UserService>()
    private val modelMapper = ModelMapper()
    private val reviewService = ReviewService(
        reviewRepository,
        reviewRepositoryImpl,
        reviewLikeRepository,
        themeRepository,
        userService,
        modelMapper
    )

    @Test
    fun `가장 높은 감정 리뷰 테스트`() {
//        val testUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
//        every { userRepository.save(any()) } returns testUser
        val user = userRepository.save(User(0L, "p", "g", "a", "n"))
        val cafe = cafeRepository.save(Cafe("카페카페", "주소주소주소주소", "042-8282-8282", "asdf.net", ""))
        val theme = themeRepository.save(Theme("name", "desc", "url", 60, 6, 3, 3.0, "age", cafe))
        reviewRepository.save(Review("content", 4.0, 4.0, "재미있어요", "", user, theme))
        reviewRepository.save(Review("content", 4.0, 4.0, "슬퍼요", "재미있어요", user, theme))
        reviewRepository.save(Review("content", 4.0, 4.0, "여운이남아요", "재미있어요", user, theme))
        reviewRepository.save(Review("content", 4.0, 4.0, "신선해요", "재미있어요", user, theme))
    }

}