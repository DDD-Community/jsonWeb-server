package jsonweb.exitserver.domain.boast

import com.querydsl.jpa.impl.JPAQueryFactory
import jsonweb.exitserver.domain.boast.QBoast.boast
import jsonweb.exitserver.domain.boast.QBoastHashtag.boastHashtag
import jsonweb.exitserver.domain.theme.QTheme
import jsonweb.exitserver.domain.theme.QTheme.theme
import jsonweb.exitserver.domain.theme.Theme
import jsonweb.exitserver.domain.user.QUser
import jsonweb.exitserver.domain.user.QUser.user
import jsonweb.exitserver.domain.user.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils

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
) : BoastRepositoryCustom {
    override fun findAllBoasts(pageable: Pageable): Page<Boast> {
        return PageableExecutionUtils.getPage(
            jpaQueryFactory.selectFrom(boast)
                .leftJoin(boast.user, user)
                .leftJoin(boast.theme, theme)
                .leftJoin(boast.hashtagMutableList, boastHashtag)
                .fetchJoin()
                .where(boast.visibility.eq(true))
                .orderBy(boast.createdAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable
        ) {
            jpaQueryFactory.selectFrom(boast)
                .where(boast.visibility.eq(true))
                .fetch().size.toLong()
        }
    }

    override fun findAllByUser(user: User, pageable: Pageable): Page<Boast> {
        return PageableExecutionUtils.getPage(
            jpaQueryFactory.selectFrom(boast)
                .leftJoin(boast.user, QUser.user)
                .leftJoin(boast.theme, theme)
                .leftJoin(boast.hashtagMutableList, boastHashtag)
                .fetchJoin()
                .where(boast.user.eq(user))
                .orderBy(boast.createdAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable
        ) {
            jpaQueryFactory.selectFrom(boast)
                .where(boast.user.eq(user))
                .fetch().size.toLong()
        }
    }

    override fun findAllByTheme(theme: Theme, pageable: Pageable): Page<Boast> {
        return PageableExecutionUtils.getPage(
            jpaQueryFactory.selectFrom(boast)
                .leftJoin(boast.user, user)
                .leftJoin(boast.theme, QTheme.theme)
                .leftJoin(boast.hashtagMutableList, boastHashtag)
                .fetchJoin()
                .where(boast.theme.eq(theme))
                .orderBy(boast.createdAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch(),
            pageable
        ) {
            jpaQueryFactory.selectFrom(boast)
                .where(boast.theme.eq(theme))
                .fetch().size.toLong()
        }
    }
}
