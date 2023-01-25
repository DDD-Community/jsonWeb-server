package jsonweb.exitserver.domain.cafe

import jsonweb.exitserver.domain.theme.ThemeResponse
import org.springframework.data.domain.Sort

enum class CafeSort(private val sort: String, private val direction: Sort.Direction) {
    DEFAULT("cafeId", Sort.Direction.ASC),
    STAR("avgStar", Sort.Direction.DESC),
    REVIEW("reviewCount", Sort.Direction.DESC);

    fun getSortBy() = sort
    fun getDirection() = direction
}

data class CafeThemeListResponse(
    val themeList: List<ThemeResponse>
)

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
        isLiked = false,
        avgStar = cafe.avgStar,
        address = cafe.address,
        imageUrl = cafe.imageUrl,
        reviewCount = cafe.reviewCount
    )

    constructor(cafe: Cafe, like: Boolean): this(
        cafeId = cafe.cafeId,
        name = cafe.name,
        isLiked = like,
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
    val reviewCount: Int,
    val imageUrl: String
) {
    constructor(cafe: Cafe): this(
        cafeId = cafe.cafeId,
        name = cafe.name,
        isLiked = false,
        avgStar = cafe.avgStar,
        address = cafe.address,
        tel = cafe.tel,
        homepage = cafe.homepage,
        openHourList = cafe.openHourList.map { OpenHourResponse(it) },
        priceList = cafe.priceList.map { PriceResponse(it) },
        themeCount = cafe.themeCount,
        reviewCount = cafe.reviewCount,
        imageUrl = cafe.imageUrl
    )
}

data class OpenHourResponse(
    val day: String,
    val time: String,
) {
    constructor(openHour: OpenHour): this(
        day = openHour.day,
        time = openHour.time
    )
}

data class PriceResponse(
    val day: String,
    val headCount: Int,
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
    val imageUrl: String,
    val openHourList: List<OpenHourRequest>,
    val priceList: List<PriceRequest>
)

data class OpenHourRequest(
    val day: String,
    val time: String
)

data class PriceRequest(
    val day: String,
    val headCount: Int,
    val price: Int
)