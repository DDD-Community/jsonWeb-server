package jsonweb.exitserver.domain.boast

import jsonweb.exitserver.domain.theme.ThemeRepository
import jsonweb.exitserver.domain.user.UserService
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
) {
    private fun String.toSort(): Sort {
        return if (this == "DATE") {
            Sort.by(Sort.Direction.DESC, "modifiedAt")
        } else {
            Sort.by(Sort.Direction.DESC, "likeCount")
        }
    }

    private fun BoastListResponse.markLike() {
        val userId = userService.getCurrentLoginUser().id
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

    fun getBoastList(sortType: String, page: Int, size: Int): BoastListResponse {
        val pageable = PageRequest.of(page, size, sortType.toSort())
        val boasts = boastRepository.findAll(pageable)
        return boasts.toBoastListResponse()
    }

    fun getUserBoastList(sortType: String, page: Int, size: Int): BoastListResponse {
        val user = userService.getCurrentLoginUser()
        val pageable = PageRequest.of(page, size, sortType.toSort())
        val boasts = boastRepository.findAllByUser(user, pageable)
        return boasts.toBoastListResponse()
    }

    fun getThemeBoastList(themeId: Long, sortType: String, page: Int, size: Int): BoastListResponse {
        val theme = themeRepository.findById(themeId).orElseThrow()
        val pageable = PageRequest.of(page, size, sortType.toSort())
        val boasts = boastRepository.findAllByTheme(theme, pageable)
        return boasts.toBoastListResponse()
    }

    @Transactional
    fun createBoast(form: BoastRequest) {
        val user = userService.getCurrentLoginUser()
        val theme = themeRepository.findById(form.themeId).orElseThrow()
        val boast = boastRepository.save(Boast(user = user, theme = theme))
        form.imageUrls.forEach {
            boast.addImage(BoastImage(imageUrl = it, boast = boast))
        }
        form.hashtags.forEach {
            boast.addHashtag(BoastHashtag(hashtag = "#$it", boast = boast))
        }
        user.addMyBoast(boast)
    }

    @Transactional
    fun createDummyBoast(form: BoastRequest, dummyKakaoId: Long) {
        val user = userService.getTestUser(dummyKakaoId)
        val theme = themeRepository.findById(form.themeId).orElseThrow()
        val boast = boastRepository.save(Boast(user = user, theme = theme))
        form.imageUrls.forEach {
            boast.addImage(BoastImage(imageUrl = it, boast = boast))
        }
        form.hashtags.forEach {
            boast.addHashtag(BoastHashtag(hashtag = "#$it", boast = boast))
        }
        user.addMyBoast(boast)
    }

    @Transactional
    fun updateBoast(id: Long, form: BoastRequest) {
        val boast = boastRepository.findById(id).orElseThrow()
        val newTheme = themeRepository.findById(form.themeId).orElseThrow()
        boast.update(form, newTheme)
    }

    @Transactional
    fun deleteBoast(id: Long) {
        val boast = boastRepository.findById(id).orElseThrow()
        boastRepository.delete(boast)
    }

    @Transactional
    fun likeBoast(userId: Long, boastId: Long) {
        boastLikeRepository.save(BoastLike(userId, boastId))
    }

    @Transactional
    fun unlikeBoast(userId: Long, boastId: Long) {
        boastLikeRepository.deleteById(BoastLikeId(userId, boastId))
    }

    @Transactional
    fun checkLike(boastId: Long) {
        val userId = userService.getCurrentLoginUser().id
        val boast = boastRepository.findById(boastId).orElseThrow()
        if (!boastLikeRepository.existsById(BoastLikeId(userId, boast.id))) {
            likeBoast(userId, boastId)
            boast.plusLike()
        } else {
            unlikeBoast(userId, boastId)
            boast.minusLike()
        }
    }

}