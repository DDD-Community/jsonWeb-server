package jsonweb.exitserver.domain.boast

import com.querydsl.jpa.impl.JPAQueryFactory
import jsonweb.exitserver.domain.boast.QBoast.boast
import jsonweb.exitserver.domain.boast.QBoastHashtag.boastHashtag
import jsonweb.exitserver.domain.theme.QTheme
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.QUser
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface BoastRepository : JpaRepository<Boast, Long>, BoastRepositoryCustom {
}

interface BoastLikeRepository : JpaRepository<BoastLike, BoastLikeId> {
}

interface BoastRepositoryCustom {
    fun findAllBoasts(pageable: Pageable): Page<Boast>
    fun findAllByUser(user: User, pageable: Pageable): Page<Boast>
    fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Boast>
}

class BoastRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : BoastRepositoryCustom, QuerydslRepositorySupport(Boast::class.java) {
    override fun findAllBoasts(pageable: Pageable): Page<Boast> {
        val query = jpaQueryFactory.selectFrom(boast)
            .leftJoin(boast.hashtagMutableList, boastHashtag)
            .fetchJoin()
            .where(boast.visibility.eq(true))
            .distinct()
        val total = query.fetch().count()
        val result = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(result, pageable, total.toLong())
    }

    override fun findAllByUser(user: User, pageable: Pageable): Page<Boast> {
        val query = jpaQueryFactory.selectFrom(boast)
            .leftJoin(boast.user, QUser.user)
            .leftJoin(boast.hashtagMutableList, boastHashtag)
            .fetchJoin()
            .where(boast.visibility.eq(true))
            .distinct()
        val total = query.fetch().count()
        val result = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(result, pageable, total.toLong())
    }

    override fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Boast> {
        val query = jpaQueryFactory.selectFrom(boast)
            .leftJoin(boast.theme, QTheme.theme)
            .leftJoin(boast.hashtagMutableList, boastHashtag)
            .fetchJoin()
            .where(boast.visibility.eq(true))
            .distinct()
        val total = query.fetch().count()
        val result = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(result, pageable, total.toLong())
    }
}
