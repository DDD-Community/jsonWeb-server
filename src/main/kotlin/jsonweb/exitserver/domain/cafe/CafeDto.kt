package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.cafe.entity.OpenHour
import jsonweb.exitserver.domain.cafe.entity.Price
import jsonweb.exitserver.domain.review.ReviewResponse
import jsonweb.exitserver.domain.theme.ThemeResponse
import org.springframework.data.domain.Sort

enum class CafeSort(private val sort: String, private val direction: Sort.Direction) {
    DEFAULT("cafeId", Sort.Direction.ASC),
    STAR("avgStar", Sort.Direction.DESC),
    REVIEW("reviewCount", Sort.Direction.DESC);

    fun getSortBy() = sort
    fun getDirection() = direction
}

data class CafeListResponse(
    val cafeList: List<CafeResponse>,
    val totalNumber: Long,
    val isLast: Boolean
)

data class CafeResponse(
    val cafeId: Long,
    val name: String,
    var isLiked: Boolean,
    val avgStar: Double,
    val address: String,
    val imageUrl: String,
    val reviewCount: Int
) {
    constructor(cafe: Cafe): this(
        cafeId = cafe.cafeId,
        name = cafe.name,
        isLiked = false, // TODO: 관련 작업 후 수정
        avgStar = cafe.avgStar,
        address = cafe.address,
        imageUrl = cafe.imageUrl,
        reviewCount = cafe.reviewCount
    )
}

data class CafeSpecResponse(
    val cafeId: Long,
    val name: String,
    var isLiked: Boolean,
    val avgStar: Double,
    val address: String,
    val tel: String,
    val homepage: String,
    val openHourList: List<OpenHourResponse>,
    val priceList: List<PriceResponse>,
    val themeCount: Int,
    val themeList: List<ThemeResponse>,
    val reviewCount: Int
//    val reviewList: MutableList<ReviewResponse> // TODO: 관련 작업 후 수정
) {
    constructor(cafe: Cafe): this(
        cafeId = cafe.cafeId,
        name = cafe.name,
        isLiked = false, // TODO: 관련 작업 후 수정
        avgStar = cafe.avgStar,
        address = cafe.address,
        tel = cafe.tel,
        homepage = cafe.homepage,
        openHourList = cafe.openHourList.map { OpenHourResponse(it) },
        priceList = cafe.priceList.map { PriceResponse(it) },
        themeCount = cafe.themeCount,
        themeList = cafe.themeList.map { ThemeResponse(it) },
        reviewCount = cafe.reviewCount
    )
}

data class OpenHourResponse(
    val day: String,
    val open: String,
    val close: String
) {
    constructor(openHour: OpenHour): this(
        day = openHour.day,
        open = openHour.openHour,
        close = openHour.closeHour
    )
}

data class PriceResponse(
    val day: String,
    val headCount: String,
    val price: Int
) {
    constructor(price: Price): this(
        day = price.day,
        headCount = price.headCount,
        price = price.price
    )
}

data class RegisterCafeRequest(
    val name: String,
    val address: String,
    val tel: String,
    val homepage: String,
    val openHourList: List<OpenHourRequest>,
    val priceList: List<PriceRequest>
)

data class OpenHourRequest(
    val day: String,
    val open: String,
    val close: String
)

data class PriceRequest(
    val day: String,
    val headCount: String,
    val price: Int
)