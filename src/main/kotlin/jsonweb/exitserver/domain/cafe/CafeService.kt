package jsonweb.exitserver.domain.cafe

import com.amazonaws.services.workdocs.model.EntityNotExistsException
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
            Sort.by(CafeSort.valueOf(sort).getDirection(), CafeSort.valueOf(sort).getSortBy())
        )
        val result = cafeRepository.findAll(pageable)
        return CafeListResponse(result.toList().map { CafeResponse(it) }, result.isLast)
    }

}