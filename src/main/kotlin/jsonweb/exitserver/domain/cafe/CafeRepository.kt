package jsonweb.exitserver.domain.cafe

import com.querydsl.jpa.impl.JPAQueryFactory
import jsonweb.exitserver.domain.cafe.QCafe.cafe
import jsonweb.exitserver.domain.theme.QTheme.theme
import jsonweb.exitserver.domain.theme.QThemeGenre.themeGenre
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

interface CafeRepository: JpaRepository<Cafe, Long>, CafeRepositoryCustom {
}

interface CafeRepositoryCustom {
    fun findAllCafes(keyword: String, pageable: Pageable): Page<Cafe>
    fun findAllByGenre(genreName: String, pageable: Pageable): Page<Cafe>
}

class CafeRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CafeRepositoryCustom {

    override fun findAllCafes(keyword: String, pageable: Pageable): Page<Cafe> {
        return PageableExecutionUtils.getPage(
            jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.themeList, theme)
                .leftJoin(theme.themeGenreList, themeGenre)
                .fetchJoin()
                .where(
                    cafe.name.contains(keyword)
                        .or(theme.name.contains(keyword))
                        .or(themeGenre.genre.genreName.contains(keyword))
                )
                .orderBy(cafe.cafeId.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable
        ) {
            jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.themeList, theme)
                .leftJoin(theme.themeGenreList, themeGenre)
                .fetchJoin()
                .where(
                    cafe.name.contains(keyword)
                        .or(theme.name.contains(keyword))
                        .or(themeGenre.genre.genreName.contains(keyword))
                )
                .fetch().size.toLong()
        }
    }

    override fun findAllByGenre(genreName: String, pageable: Pageable): Page<Cafe> {
        return PageableExecutionUtils.getPage(
            jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.themeList, theme)
                .leftJoin(theme.themeGenreList, themeGenre)
                .fetchJoin()
                .where(themeGenre.genre.genreName.eq(genreName))
                .orderBy(cafe.cafeId.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable
        ) {
            jpaQueryFactory.selectFrom(cafe)
                .leftJoin(cafe.themeList, theme)
                .leftJoin(theme.themeGenreList, themeGenre)
                .fetchJoin()
                .where(themeGenre.genre.genreName.eq(genreName))
                .fetch().size.toLong()
        }
    }
}

interface CafeLikeRepository: JpaRepository<CafeLike, UserAndCafe> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<CafeLike>
    fun findAllByUserId(userId: Long): List<CafeLike>
}
