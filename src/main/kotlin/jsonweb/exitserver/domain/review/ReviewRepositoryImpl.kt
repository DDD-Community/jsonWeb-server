package jsonweb.exitserver.domain.review

import com.querydsl.jpa.impl.JPAQueryFactory
import jsonweb.exitserver.domain.review.QReview.review
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(Review::class.java) {
    fun countWithEmotion(emotion: String): Int {
        return jpaQueryFactory.selectFrom(review)
            .where(
                review.emotionFirst.contains(emotion)
                    .or(review.emotionSecond.contains(emotion))
            )
            .fetch().count()
    }
}