package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.*
import jsonweb.exitserver.domain.user.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
@Transactional(readOnly = true)
class CafeService(
    private val cafeRepository: CafeRepository,
    private val cafeRepositoryImpl: CafeRepositoryImpl,
    private val cafeLikeRepository: CafeLikeRepository,
    private val userService: UserService
) {
    @Transactional
    fun registerCafe(form: RegisterCafeRequest) {
        val cafe = makeCafe(form.name, form.address, form.tel, form.homepage)
        form.openHourList
            .map { OpenHour(it.day, it.open, it.close, cafe) }
            .forEach { cafe.addOpenHour(it) }
        form.priceList
            .map { Price(it.headCount, it.day, it.price, cafe) }
            .forEach { cafe.addPrice(it) }
        cafeRepository.save(cafe)
    }

    @Transactional
    fun deleteCafe(cafeId: Long) {
        cafeRepository.deleteById(cafeId)
    }

    fun getCafeSpec(cafeId: Long): CafeSpecResponse {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        return markLike(CafeSpecResponse(cafe))
    }

    fun getCafeList(page: Int, size: Int, sort: String): CafeListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            makeSort(sort)
        )
        val result = cafeRepository.findAll(pageable)
        return markLike(CafeListResponse(result.toList().map { CafeResponse(it) }, result.totalElements, result.isLast))
    }

    fun getCafeListWithKeyword(keyword: String, page: Int, size: Int, sort: String): CafeListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            makeSort(sort)
        )
        val result = cafeRepositoryImpl.getList(keyword, pageable)
        return markLike(CafeListResponse(result.toList().map { CafeResponse(it) }, result.totalElements, result.isLast))
    }

    @Transactional
    fun markCafeWrong(cafeId: Long) {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        cafe.markWrong()
    }

    @Transactional
    fun markCafeRight(cafeId: Long) {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        cafe.markRight()
    }

    private fun makeCafe(name: String, address: String, tel: String, homepage: String): Cafe {
        return Cafe(name, address, tel, homepage, "")
    }

    private fun makeSort(sort: String): Sort {
        return if (sort == "DEFAULT") Sort.by(
            CafeSort.valueOf(sort).getDirection(),
            CafeSort.valueOf(sort).getSortBy()
        )
        else Sort.by(
            CafeSort.valueOf(sort).getDirection(),
            CafeSort.valueOf(sort).getSortBy()
        ).and(Sort.by("cafeId"))
    }

    @Transactional
    fun likeCafe(cafeId: Long) {
        val userId = userService.getCurrentLoginUser().userId
        cafeLikeRepository.save(CafeLike(userId, cafeId))
    }

    @Transactional
    fun unlikeCafe(cafeId: Long) {
        val userId = userService.getCurrentLoginUser().userId
        cafeLikeRepository.deleteById(UserAndCafe(userId, cafeId))
    }

    private fun markLike(cafeListResponse: CafeListResponse): CafeListResponse {
        val userId = userService.getCurrentLoginUser().userId
        val likes = cafeLikeRepository.findAllByUserId(userId).map { it.cafeId }
        for (cafeResponse in cafeListResponse.cafeList) {
            if (cafeResponse.cafeId in likes) cafeResponse.isLiked = true
        }
        return cafeListResponse
    }

    private fun markLike(cafeSpecResponse: CafeSpecResponse): CafeSpecResponse {
        val userId = userService.getCurrentLoginUser().userId
        val likes = cafeLikeRepository.findAllByUserId(userId).map { it.cafeId }
        if (cafeSpecResponse.cafeId in likes) cafeSpecResponse.isLiked = true
        return cafeSpecResponse
    }

}