package jsonweb.exitserver.domain.cafe

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jsonweb.exitserver.domain.cafe.entity.Cafe
import java.util.*

class CafeServiceTest : AnnotationSpec() {
    private val cafeRepository = mockk<CafeRepository>()
    private val cafeService = CafeService(cafeRepository)

    @BeforeAll
    fun `초기 설정`() {
        val testCafe = Cafe("test", "test", "test", "test", "test")
        every { cafeRepository.findById(any()) } returns Optional.of(testCafe)
    }

    @Test
    fun `WrongCheck 켜지는지 바뀌는지 테스트`() {
        cafeService.markCafeWrong(1L)
        val testCafe = cafeRepository.findById(1L)
        testCafe.get().wrongCheck shouldBe true
    }

    @Test
    fun `WorngCheck 꺼지는지 테스트`() {
        cafeService.markCafeRight(1L)
        val testCafe = cafeRepository.findById(1L)
        testCafe.get().wrongCheck shouldBe false
    }
}