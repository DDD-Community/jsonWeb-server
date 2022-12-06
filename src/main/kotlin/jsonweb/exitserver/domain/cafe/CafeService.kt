package jsonweb.exitserver.domain.cafe

import com.amazonaws.services.workdocs.model.EntityNotExistsException
import jsonweb.exitserver.domain.cafe.entity.Cafe
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class CafeService(
    private val cafeRepository: CafeRepository
) {
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


    fun makeSort(sort: String): Sort {
        return Sort.by(
            CafeSort.valueOf(sort).getDirection(),
            CafeSort.valueOf(sort).getSortBy()
        ).and(Sort.by("cafeId"))
    }


}