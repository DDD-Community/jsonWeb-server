package jsonweb.exitserver.domain.report

import jsonweb.exitserver.domain.boast.BoastRepository
import jsonweb.exitserver.domain.review.ReviewRepository
import jsonweb.exitserver.domain.user.UserService
import jsonweb.exitserver.util.badge.BadgeDomain
import jsonweb.exitserver.util.badge.CheckBadge
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ReportService(
    private val userService: UserService,
    private val reviewRepository: ReviewRepository,
    private val boastRepository: BoastRepository,
    private val reportRepository: ReportRepository
) {
    // 리뷰 신고
    @CheckBadge(BadgeDomain.REVIEW)
    @Transactional
    fun createReviewReport(form: ReviewReportRequest) {
        val user = userService.getCurrentLoginUser()
        val review = reviewRepository.findById(form.reviewId).orElseThrow()
        user.addReport(ReviewReport(review = review, content = form.content, user = user))
    }

    // 인증 신고
    @Transactional
    fun createBoastReport(form: BoastReportRequest) {
        val user = userService.getCurrentLoginUser()
        val boast = boastRepository.findById(form.boastId).orElseThrow()
        user.addReport(BoastReport(boast = boast, content = form.content, user = user))
    }

    // TODO : 신고 전체 조회

    // TODO : 리뷰 신고 전체 조회

    // TODO : 인증 신고 전체 조회

    // 신고 해결
    @Transactional
    fun resolveReport(reportId: Long) {
        val report = reportRepository.findById(reportId).orElseThrow()
        report.resolve()
    }
}