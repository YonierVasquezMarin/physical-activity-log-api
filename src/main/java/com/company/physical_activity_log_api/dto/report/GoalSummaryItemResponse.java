package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalSummaryItemResponse {

	private Integer goalId;
	private String title;
	private String status;
	private int sessionsLinked;
	private Integer latestLevel;
	private Integer firstLevel;
	private Integer levelChange;

}
