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
    fun set_up() {
        val testCafe = Cafe("test", "test", "test", "test", "test")
        every { cafeRepository.findById(any()) } returns Optional.of(testCafe)
    }

    @Test
    fun check_wrong_test() {
        cafeService.markCafeWrong(1L)
        val testCafe = cafeRepository.findById(1L)
        testCafe.get().wrongCheck shouldBe true
    }

    @Test
    fun check_right_test() {
        cafeService.markCafeRight(1L)
        val testCafe = cafeRepository.findById(1L)
        testCafe.get().wrongCheck shouldBe false
    }
}