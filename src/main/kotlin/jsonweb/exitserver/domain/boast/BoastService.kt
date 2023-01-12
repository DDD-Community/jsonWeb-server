package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.common.logger
import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.UserService
import jsonweb.exitserver.util.Exp
import jsonweb.exitserver.util.badge.BadgeType
import jsonweb.exitserver.util.badge.CheckBadge
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BoastService(
    private val userService: UserService,
    private val themeRepository: ThemeRepository,
    private val boastRepository: BoastRepository,
    private val boastLikeRepository: BoastLikeRepository,
    private val boastReportRepository: BoastReportRepository
) {

    val log = logger()

    private fun String.toSort(): Sort {
        return if (this == "DATE") {
            Sort.by(Sort.Direction.DESC, "modifiedAt")
        } else {
            Sort.by(Sort.Direction.DESC, "likeCount")
        }
    }

    private fun BoastListResponse.markLike() {
        val userId = userService.getCurrentLoginUser().userId
        this.boastList.forEach {
            if (boastLikeRepository.existsById(BoastLikeId(userId, it.boastId))) {
                it.isLiked = true
            }
        }
    }

    private fun Page<Boast>.toBoastListResponse(): BoastListResponse {
        val result = BoastListResponse(
            this.content.map { BoastResponse(it) },
            this.totalElements,
            this.isLast
        )
        result.markLike()
        return result
    }

    fun getAllBoasts(sortType: String, page: Int, size: Int): BoastListResponse {
        val pageable = PageRequest.of(page, size, sortType.toSort())
        val boasts = boastRepository.findAllBoasts(pageable)
        return boasts.toBoastListResponse()
    }

    fun getUserBoasts(sortType: String, page: Int, size: Int): BoastListResponse {
        val user = userService.getCurrentLoginUser()
        val pageable = PageRequest.of(page, size, sortType.toSort())
        val boasts = boastRepository.findAllByUser(user, pageable)
        return boasts.toBoastListResponse()
    }

    fun getThemeBoasts(themeId: Long, sortType: String, page: Int, size: Int): BoastListResponse {
        val theme = themeRepository.findById(themeId).orElseThrow()
        val pageable = PageRequest.of(page, size, sortType.toSort())
        val boasts = boastRepository.findAllByTheme(theme, pageable)
        return boasts.toBoastListResponse()
    }

    @Exp(20)
    @CheckBadge(BadgeType.BOAST)
    @Transactional
    fun createBoast(form: BoastRequest) {
        val user = userService.getCurrentLoginUser()
        val theme = themeRepository.findById(form.themeId).orElseThrow()
        val boast = boastRepository.save(Boast(
            user = user,
            theme = theme,
            imageUrl = form.imageUrl
        ))
        form.hashtags.forEach {
            boast.addHashtag(BoastHashtag(hashtag = "#$it", boast = boast))
        }
        user.addMyBoast(boast)

//        // 인증 글 10개 작성 시 탈출중독 뱃지 획득
//        if (user.myBoastList.size == 10) {
//            log.info("${user.nickname}님이 탈출중독 뱃지 획득.")
//            user.addBadge(BadgeEnum.ADDICTED_ESCAPE)
//        }
    }

    @Transactional
    fun updateBoast(id: Long, form: BoastRequest) {
        val boast = boastRepository.findById(id).orElseThrow()
        val newTheme = themeRepository.findById(form.themeId).orElseThrow()
        boast.update(form, newTheme)
    }

    @Transactional
    fun deleteBoast(id: Long) {
        boastRepository.deleteById(id)
    }

    @Transactional
    fun checkLike(boastId: Long) {
        val userId = userService.getCurrentLoginUser().userId
        val boast = boastRepository.findById(boastId).orElseThrow()
        if (!boastLikeRepository.existsById(BoastLikeId(userId, boast.id))) {
            boastLikeRepository.save(BoastLike(userId, boastId))
            boast.plusLike()
        } else {
            boastLikeRepository.deleteById(BoastLikeId(userId, boastId))
            boast.minusLike()
        }
    }

    @Exp(20)
    @Transactional
    fun reportBoast(boastId: Long, form: ReportBoastRequest) {
        val boast = boastRepository.findById(boastId).orElseThrow()
        boastReportRepository.save(BoastReport(boast = boast, reportContent = form.reportContent))
        boast.setInvisible()
    }

    /**
     * 테스트용
     */
    @Exp(20)
    @Transactional
    fun createDummyBoast(form: BoastRequest, dummyKakaoId: Long) {
        val user = userService.getTestUser(dummyKakaoId)
        val theme = themeRepository.findById(form.themeId).orElseThrow()
        val boast = boastRepository.save(Boast(
            user = user,
            theme = theme,
            imageUrl = form.imageUrl))
        form.hashtags.forEach {
            boast.addHashtag(BoastHashtag(hashtag = "#$it", boast = boast))
        }
        user.addMyBoast(boast)
    }

}