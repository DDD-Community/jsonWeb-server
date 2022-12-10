package jsonweb.exitserver.domain.cafe

import com.querydsl.jpa.impl.JPAQueryFactory
import jsonweb.exitserver.domain.cafe.entity.Cafe
import jsonweb.exitserver.domain.cafe.entity.QCafe
import jsonweb.exitserver.domain.theme.QTheme
import jsonweb.exitserver.domain.theme.QTheme.theme
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CafeRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Cafe::class.java) {

    fun getList(keyword: String, pageable: Pageable): Page<Cafe> {
        val query = jpaQueryFactory.selectFrom(QCafe.cafe)
            .leftJoin(QCafe.cafe.themeList, theme)
            .fetchJoin()
            .where(
                QCafe.cafe.name.contains(keyword)
                    .or(QCafe.cafe.address.contains(keyword))
                    .or(theme.name.contains(keyword))
            )
            .distinct()
        val total = query.fetch().count()
        val result = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(result, pageable, total.toLong())
    }
}