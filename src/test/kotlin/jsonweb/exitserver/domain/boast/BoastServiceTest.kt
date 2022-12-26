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
    private val boastReportRepository: BoastReportRepository = mockk()

    private val boastService: BoastService = spyk(
        BoastService(userService, themeRepository, boastRepository, boastLikeRepository, boastReportRepository),
        recordPrivateCalls = true
    )

    private val mockUser = User(-1, "pwd", "male", "20~29", "랜덤 닉네임")
    private val mockBoastList = mutableListOf<Boast>()

    @BeforeAll
    fun init() {
        repeat(30) {
            mockBoastList.add(Boast(user = mockUser, theme = Theme()))
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
        every { boastRepository.findAll(any() as Pageable) } returns pagingList

        // when
        val result = boastService.getAllBoasts("DATE", 0, 16)

        // then
        result.totalNumber shouldBe 30
    }

    @Test
    fun `인증 작성 성공`() {
        // given
        every { themeRepository.findById(1L) } returns Optional.of(Theme())
        every { boastRepository.save(any()) } returns Boast(user = mockUser, theme = Theme())
        val imageUrls = mutableListOf("https://image.com/1", "https://image.com/2")
        val hashtags = mutableListOf("해시태그1", "해시태그2")
        val form = BoastRequest(1L, imageUrls, hashtags)

        // when
        boastService.createBoast(form)

        // then
        assertSoftly(mockUser.myBoastList) {
            size shouldBe 1
            first().boastImageList.first().imageUrl shouldBe imageUrls.first()
            first().hashtagList.first().hashtag shouldBe "#${hashtags.first()}"
        }
    }

    @Test
    fun `인증 수정 성공`() {
        // given
        val oldBoast = Boast(user = mockUser, theme = Theme())
        every { boastRepository.findById(1L) } returns Optional.of(oldBoast)
        every { themeRepository.findById(1L) } returns Optional.of(Theme())
        val imageUrls = mutableListOf("https://image.com/3", "https://image.com/4")
        val hashtags = mutableListOf("해시태그3", "해시태그4")
        val form = BoastRequest(1L, imageUrls, hashtags)

        // when
        boastService.updateBoast(1L ,form)

        // then
        assertSoftly(mockUser.myBoastList) {
            first().boastImageList[0].imageUrl shouldBe imageUrls[0]
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