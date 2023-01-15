package jsonweb.exitserver.domain.boast

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserService
import org.springframework.data.domain.*
import java.lang.Integer.min
import java.util.*

class BoastServiceTest : AnnotationSpec() {
    private val userService: UserService = mockk()
    private val themeRepository: ThemeRepository = mockk()
    private val boastRepository: BoastRepository = mockk()
    private val boastLikeRepository: BoastLikeRepository = mockk()
//    private val boastReportRepository: BoastReportRepository = mockk()

    private val boastService: BoastService = spyk(
        BoastService(userService, themeRepository, boastRepository, boastLikeRepository),
        recordPrivateCalls = true
    )

    private val mockUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
    private val mockBoastList = mutableListOf<Boast>()

    @BeforeAll
    fun init() {
        val imageUrl = "https://image.com/1"
        repeat(30) {
            mockBoastList.add(Boast(user = mockUser, theme = Theme(), imageUrl = imageUrl))
        }
        every { userService.getCurrentLoginUser() } returns mockUser
        every { boastLikeRepository.existsById(any()) } returns true
    }

    @AfterEach
    fun clear() = mockUser.clearMyBoast()


    @Test
    fun `인증 전체 조회`() {
        // given
        val pageable = PageRequest.of(0, 16)
        val start = pageable.offset.toInt()
        val end = min((start + pageable.pageSize), mockBoastList.size)
        val pagingList: Page<Boast> = PageImpl(mockBoastList.subList(start, end), pageable, mockBoastList.size.toLong())
        every { boastRepository.findAllBoasts(any() as Pageable) } returns pagingList

        // when
        val result = boastService.getAllBoasts("DATE", 0, 16)

        // then
        result.totalNumber shouldBe 30
    }

    @Test
    fun `인증 작성 성공`() {
        // given
        val theme = Theme()
        val hashtags = mutableListOf("해시태그1", "해시태그2")
        val imageUrl = "https://image.com/1"
        val form = BoastRequest(1L, imageUrl, hashtags)

        every { themeRepository.findById(1L) } returns Optional.of(theme)
        every { boastRepository.save(any()) } returns Boast(
            user = mockUser,
            theme = theme,
            imageUrl = form.imageUrl)

        // when
        boastService.createBoast(form)

        // then
        assertSoftly(mockUser.myBoastList) {
            size shouldBe 1
            first().imageUrl shouldBe imageUrl
            first().hashtagList.first().hashtag shouldBe "#${hashtags.first()}"
        }
    }

    @Test
    fun `인증 수정 성공`() {
        // given
        val oldBoast = Boast(
            user = mockUser,
            theme = Theme(),
            imageUrl = "https://image.com/3"
        )
        mockUser.addMyBoast(oldBoast)
        every { boastRepository.findById(1L) } returns Optional.of(oldBoast)
        every { themeRepository.findById(1L) } returns Optional.of(Theme())
        val hashtags = mutableListOf("해시태그3", "해시태그4")
        val form = BoastRequest(1L, "https://image.com/4", hashtags)

        // when
        boastService.updateBoast(1L ,form)

        // then
        assertSoftly(mockUser.myBoastList) {
            size shouldBe 1
            first().imageUrl shouldBe "https://image.com/4"
            first().hashtagList[0].hashtag shouldBe "#${hashtags[0]}"
        }
    }

    @Test
    fun `(private) toSort() 최신순 정렬 성공`() {
        // given
        val toSort = boastService.javaClass.getDeclaredMethod("toSort", String::class.java)
            .apply { isAccessible = true }
        val sortType = "DATE"

        // when
        val sort = toSort.invoke(boastService, sortType) as Sort

        // then
        sort.getOrderFor("modifiedAt")?.direction shouldBe Sort.Direction.DESC
    }

    @Test
    fun `(private) toSort() 인기순 정렬 성공`() {
        // given
        val toSort = boastService.javaClass.getDeclaredMethod("toSort", String::class.java)
            .apply { isAccessible = true }
        val sortType = "LIKE"

        // when
        val sort = toSort.invoke(boastService, sortType) as Sort

        // then
        sort.getOrderFor("likeCount")?.direction shouldBe Sort.Direction.DESC
    }
}