package com.company.physical_activity_log_api.dto.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportGoalsResponse {

	private int activeCount;
	private int expiredCount;
	private int withoutProgressCount;
	private List<GoalSummaryItemResponse> items;

}
