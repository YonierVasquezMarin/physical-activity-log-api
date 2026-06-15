package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryReportResponse {

	private ReportPeriodResponse period;
	private ReportOverviewResponse overview;
	private ReportConsistencyResponse consistency;
	private ReportCategoriesResponse categories;
	private ReportActivitiesResponse activities;
	private ReportGoalsResponse goals;

}
