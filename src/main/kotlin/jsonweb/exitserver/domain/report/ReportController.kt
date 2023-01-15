package jsonweb.exitserver.domain.report

import jsonweb.exitserver.common.CommonResponse
import jsonweb.exitserver.common.success
import org.springframework.web.bind.annotation.*

@RestController
class ReportController(private val reportService: ReportService) {

    @PostMapping("/boasts/reports")
    fun createBoastReport(@RequestBody form: BoastReportRequest): CommonResponse<Any> {
        reportService.createBoastReport(form)
        return success()
    }

    @PostMapping("/reviews/reports")
    fun createReviewReport(@RequestBody form: ReviewReportRequest): CommonResponse<Any> {
        reportService.createReviewReport(form)
        return success()
    }

    @PutMapping("/boasts/reports/{reportId}")
    fun resolveReport(@PathVariable reportId: Long): CommonResponse<Any> {
        reportService.resolveReport(reportId)
        return success()
    }

}