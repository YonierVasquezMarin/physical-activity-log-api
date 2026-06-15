package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportOverviewResponse {

	private int totalSessions;
	private int activeDays;
	private double averageSessionsPerWeek;
	private MostFrequentCategoryResponse mostFrequentCategory;

}
