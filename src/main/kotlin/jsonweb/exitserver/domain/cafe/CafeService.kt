package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.cafe.entity.OpenHour
import jsonweb.exitserver.domain.cafe.entity.Price
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CafeService(
    private val cafeRepository: CafeRepository
) {
    fun registerCafe(form: RegisterCafeRequest) {
        val cafe = makeCafe(form.name, form.address, form.tel, form.homepage)
        form.openHourList
            .map { OpenHour(it.day, it.open, it.close, cafe) }
            .forEach { cafe.addOpenHour(it) }
        val priceList = form.priceList
            .map { Price(it.headCount, it.day, it.price, cafe) }
            .forEach { cafe.addPrice(it) }
        cafeRepository.save(cafe)
    }

    fun deleteCafe(cafeId: Long) {
        cafeRepository.deleteById(cafeId)
    }

    fun getCafeSpec(cafeId: Long): CafeSpecResponse {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        return CafeSpecResponse(cafe)
    }

    fun getCafeList(page: Int, size: Int, sort: String): CafeListResponse {
        val pageable = PageRequest.of(
            page,
            size,
            makeSort(sort)
        )
        val result = cafeRepository.findAll(pageable)
        return CafeListResponse(result.toList().map { CafeResponse(it) }, result.isLast)
    }

    fun markCafeWrong(cafeId: Long) {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        cafe.markWrong()
    }

    fun markCafeRight(cafeId: Long) {
        val cafe = cafeRepository.findById(cafeId).orElseThrow { throw EntityNotFoundException() }
        cafe.markRight()
    }

    fun makeCafe(name: String, address: String, tel: String, homepage: String): Cafe {
        return Cafe(name, address, tel, homepage, "")
    }

    fun makeSort(sort: String): Sort {
        return if (sort == "DEFAULT") Sort.by(
            CafeSort.valueOf(sort).getDirection(),
            CafeSort.valueOf(sort).getSortBy()
        )
        else Sort.by(
            CafeSort.valueOf(sort).getDirection(),
            CafeSort.valueOf(sort).getSortBy()
        ).and(Sort.by("cafeId"))
    }


}