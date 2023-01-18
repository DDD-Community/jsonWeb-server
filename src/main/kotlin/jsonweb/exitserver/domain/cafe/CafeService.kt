package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.user.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CafeService(
    private val cafeRepository: CafeRepository,
    private val cafeLikeRepository: CafeLikeRepository,
    private val userService: UserService
) {
    @Transactional
    fun registerCafe(form: RegisterCafeRequest): Long {
        val cafe = Cafe(
            name = form.name,
            address = form.address,
            tel = form.tel,
            homepage = form.homepage,
            imageUrl = form.imageUrl
        )
        form.openHourList
            .map { OpenHour(it.day, it.open, it.close, cafe) }
            .forEach { cafe.addOpenHour(it) }
        form.priceList
            .map { Price(it.headCount, it.day, it.price, cafe) }
            .forEach { cafe.addPrice(it) }
        return cafeRepository.save(cafe).cafeId
    }

    @Transactional
    fun deleteCafe(cafeId: Long) {
        cafeRepository.deleteById(cafeId)
    }

    fun getCafeSpec(cafeId: Long): CafeSpecResponse {
        val cafe = cafeRepository.findById(cafeId).orElseThrow()
        return markLike(CafeSpecResponse(cafe))
    }

    fun getCafeList(genreName: String?, page: Int, size: Int, sort: String): CafeListResponse {
        val pageable = PageRequest.of(
            page, size, makeSort(sort)
        )
        if (genreName.isNullOrEmpty()) {
            val result = cafeRepository.findAll(pageable)
            return markLike(
                CafeListResponse(
                    result.toList().map { CafeResponse(it) },
                    result.totalElements,
                    result.isLast
                )
            )
        } else {
            val result = cafeRepository.findAllByGenre(genreName, pageable)
            return markLike(
                CafeListResponse(
                    result.toList().map { CafeResponse(it) },
                    result.totalElements,
                    result.isLast
                )
            )
        }
    }

    fun getCafeListWithKeyword(keyword: String, page: Int, size: Int, sort: String): CafeListResponse {
        val pageable = PageRequest.of(
            page, size, makeSort(sort)
        )
        val cafes = cafeRepository.findAllCafes(keyword, pageable)
        return markLike(CafeListResponse(cafes.toList().map { CafeResponse(it) }, cafes.totalElements, cafes.isLast))
    }

    fun getLikeCafeList(page: Int, size: Int): CafeListResponse {
        val pageable = PageRequest.of(page, size)
        val userId = userService.getCurrentLoginUser().userId
        val cafeLikes = cafeLikeRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
        val cafes = cafeLikes.map { cafeRepository.findById(it.cafeId).get() }
        return CafeListResponse(
            cafes.toList().map { CafeResponse(it, true) },
            cafeLikes.totalElements,
            cafeLikes.isLast
        )
    }

    private fun makeSort(sort: String): Sort {
        return if (sort == "DEFAULT") Sort.by(
            CafeSort.valueOf(sort).getDirection(), CafeSort.valueOf(sort).getSortBy()
        )
        else Sort.by(
            CafeSort.valueOf(sort).getDirection(), CafeSort.valueOf(sort).getSortBy()
        ).and(Sort.by("cafeId"))
    }

    @Transactional
    fun checkLike(cafeId: Long) {
        val userId = userService.getCurrentLoginUser().userId
        if (!cafeLikeRepository.existsById(UserAndCafe(userId, cafeId)))
            cafeLikeRepository.save(CafeLike(userId, cafeId))
        else
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