package com.company.physical_activity_log_api.controller;

import java.time.OffsetDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.physical_activity_log_api.auth.CurrentUser;
import com.company.physical_activity_log_api.auth.UserTokenRequired;
import com.company.physical_activity_log_api.dto.report.SummaryReportResponse;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reports")
@UserTokenRequired
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@GetMapping("/summary")
	public ResponseEntity<SummaryReportResponse> summary(
			@CurrentUser User user,
			@RequestParam(required = false) OffsetDateTime from,
			@RequestParam(required = false) OffsetDateTime to,
			@RequestParam(defaultValue = "5") int topActivitiesLimit) {
		return ResponseEntity.ok(reportService.getSummary(user, from, to, topActivitiesLimit));
	}
}
