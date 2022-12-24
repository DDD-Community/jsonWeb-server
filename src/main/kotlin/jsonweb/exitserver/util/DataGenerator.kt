package jsonweb.exitserver.util

import jsonweb.exitserver.common.TEST_ADMIN_KAKAO_ID
import jsonweb.exitserver.common.TEST_USER_KAKAO_ID
import jsonweb.exitserver.domain.boast.BoastRepository
import jsonweb.exitserver.domain.boast.BoastRequest
import jsonweb.exitserver.domain.boast.BoastService
import jsonweb.exitserver.domain.cafe.Cafe
import jsonweb.exitserver.domain.cafe.CafeRepository
import jsonweb.exitserver.domain.cafe.OpenHour
import jsonweb.exitserver.domain.cafe.Price
import jsonweb.exitserver.domain.review.ReviewRepository
import jsonweb.exitserver.domain.theme.*
import jsonweb.exitserver.domain.user.User
import jsonweb.exitserver.domain.user.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Component
@Transactional
class DataGenerator(
    private val cafeRepository: CafeRepository,
    private val themeRepository: ThemeRepository,
    private val reviewRepository: ReviewRepository,
    private val genreRepository: GenreRepository,
    private val boastRepository: BoastRepository,
    private val boastService: BoastService,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository,
) {

    @PostConstruct
    fun addData() {
        /**
         * 테스트 회원 세팅
         */
        if (userRepository.count().toInt() != 0) return
        val dummyAdmin = User(
            kakaoId = TEST_ADMIN_KAKAO_ID,
            nickname = "어드민",
            ageRange = "20~29",
            gender = "male",
            password = passwordEncoder.encode("1234")
        )
        dummyAdmin.setAdmin()
        userRepository.save(dummyAdmin)

        val dummyUser = User(
            kakaoId = TEST_USER_KAKAO_ID,
            nickname = "더미 유저",
            ageRange = "20~29",
            gender = "male",
            password = passwordEncoder.encode("1234")
        )
        userRepository.save(dummyUser)
        userRepository.flush()

        if (cafeRepository.count().toInt() != 0) return

        val genres: Array<String> = arrayOf("장르1", "장르2", "장르3", "장르4", "장르5", "장르6", "장르7", "장르8")
//        val genreEntities = emptyArray<Genre>()
        val genreEntities = arrayOfNulls<Genre>(8)
        for (i: Int in 0..7) {
            genreEntities[i] = genreRepository.save(Genre(genres[i]))
        }

        var cntCafe = 1

        // 방카페 50회 반복
        repeat(50) {
            val tempCafe: Cafe = cafeRepository.save(Cafe("카페카페$cntCafe", "주소주소주소주소", "042-8282-8282", "asdf.net", ""))

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
            var cntHead = 2
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
            val days: Array<String> = arrayOf("월", "화", "수", "목", "금", "토", "일")
            for (i: Int in 0..6) {
                tempCafe.addOpenHour(
                    OpenHour(
                        days[i],
                        "11:00",
                        "22:00",
                        tempCafe
                    )
                )
            }
            cafeRepository.save(tempCafe)
            cntCafe++
        }
        cafeRepository.flush()

        /**
         * 인증 글 세팅
         */
        if (boastRepository.count().toInt() != 0) return
        val imageUrls = listOf(
            "https://jsonweb-image.s3.ap-northeast-2.amazonaws.com/spring.png",
            "https://jsonweb-image.s3.ap-northeast-2.amazonaws.com/spring.png"
        )
        val randomHashtags = listOf(
            "어려워요",
            "내가 최고",
            "나만깸",
            "쉬워요",
            "무서워요",
            "재밌어요",
            "추천해요"
        )
        val randomIds = listOf(
            TEST_ADMIN_KAKAO_ID,
            TEST_USER_KAKAO_ID
        )
        repeat(3) { themeId ->
            repeat(10) {
                val hashtags = mutableListOf<String>()
                repeat((0..3).random()) {
                    hashtags.add(randomHashtags[(randomHashtags.indices).random()])
                }
                val form = BoastRequest(themeId.toLong() + 1, imageUrls, hashtags)
                boastService.createDummyBoast(form, randomIds[(randomIds.indices).random()])
            }
        }
    }
}