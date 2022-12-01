package jsonweb.exitserver.util

import jsonweb.exitserver.domain.cafe.CafeRepository
import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.cafe.entity.OpenHour
import jsonweb.exitserver.domain.cafe.entity.Price
import jsonweb.exitserver.domain.review.ReviewRepository
import jsonweb.exitserver.domain.theme.*
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Component
@Transactional
class DataGenerator(
    private val cafeRepository: CafeRepository,
    private val themeRepository: ThemeRepository,
    private val reviewRepository: ReviewRepository,
    private val genreRepository: GenreRepository
) {
    @PostConstruct
    fun addData() {
        val genres: Array<String> = arrayOf("장르1", "장르2", "장르3", "장르4", "장르5", "장르6", "장르7", "장르8")
//        val genreEntities = emptyArray<Genre>()
        val genreEntities = arrayOfNulls<Genre>(8)
        for (i: Int in 0..7) {
            genreEntities[i] = genreRepository.save(Genre(genres[i]))
        }

        var cntCafe: Int = 1

        // 방카페 50회 반복
        repeat(50) {
            val tempCafe: Cafe = cafeRepository.save(Cafe("카페카페$cntCafe", "주소주소주소주소", "042-8282-8282", "asdf.net"))

            // 테마 10회씩 반복
            var cntTheme = 1
            var difficulty = 5.0
            repeat(10) {
                tempCafe.addTheme(
                    Theme(
                        "테마테마$cntTheme",
                        "설명설명",
                        "https://jsonweb-image.s3.ap-northeast-2.amazonaws.com/spring.png",
                        100,
                        2,
                        5,
                        difficulty,
                        "12세 이상",
                        tempCafe
                    )
                )

                cntTheme++
                difficulty -= 0.2
            }

            for (themeI: Theme in tempCafe.themeList) {
                val a = (1..7).random() % 8
                val b = ((1..7).random() + a) % 8
                genreEntities[a]?.let { it1 -> ThemeGenre(themeI, it1) }?.let { it2 -> themeI.addThemeGenre(it2) }
                genreEntities[b]?.let { it1 -> ThemeGenre(themeI, it1) }?.let { it2 -> themeI.addThemeGenre(it2) }
            }

            // 가격 3회씩 반복
            var cntHead: Int = 2
            repeat(3) {
                tempCafe.addPrice(
                    Price(
                        cntHead.toString(),
                        "요일요일",
                        8000 * cntHead,
                        tempCafe
                    )
                )
                cntHead++
            }

            // 영업 시간 6회 반복
            val days: Array<String> = arrayOf("월", "화", "수", "목", "금", "주말")
            for (i: Int in 0..5) {
                tempCafe.addOpenHour(
                    OpenHour(
                        days[i],
                        "대충 시작 시간 $i",
                        "대충 마감 시간 $i",
                        tempCafe
                    )
                )
            }
            cafeRepository.save(tempCafe)
            cntCafe++
        }
    }
}