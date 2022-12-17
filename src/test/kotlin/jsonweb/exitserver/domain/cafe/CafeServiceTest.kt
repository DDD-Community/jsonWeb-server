package jsonweb.exitserver.domain.cafe

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jsonweb.exitserver.auth.KakaoClient
import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserRepository
import jsonweb.exitserver.domain.user.UserService
import java.util.*

class CafeServiceTest : AnnotationSpec() {
    private val cafeRepository = mockk<CafeRepository>()
    private val cafeRepositoryImpl = mockk<CafeRepositoryImpl>()
    private val userRepository = mockk<UserRepository>()
    private val cafeLikeRepository = mockk<CafeLikeRepository>()
    private val userService = mockk<UserService>()
//    private val cafeService = CafeService(cafeRepository, cafeRepositoryImpl, cafeLikeRepository, userService)


    @BeforeAll
    fun `초기 설정`() {
        val testCafe = Cafe("test", "test", "test", "test", "test")
        every { cafeRepository.findById(any()) } returns Optional.of(testCafe)
    }

//    @Test
//    fun `WrongCheck 켜지는지 바뀌는지 테스트`() {
//        cafeService.markCafeWrong(1L)
//        val testCafe = cafeRepository.findById(1L)
//        testCafe.get().wrongCheck shouldBe true
//    }
//
//    @Test
//    fun `WorngCheck 꺼지는지 테스트`() {
//        cafeService.markCafeRight(1L)
//        val testCafe = cafeRepository.findById(1L)
//        testCafe.get().wrongCheck shouldBe false
//    }

    @Test
    fun `좋아요 마킹 테스트 (CafeSpecResponse)`() {

    }

    @Test
    fun `좋아요 마킹 테스트 (CafeListResponse)`() {

    }
}