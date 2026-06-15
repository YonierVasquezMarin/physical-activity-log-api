package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopActivityResponse {

	private Integer activityId;
	private String activityName;
	private String categoryName;
	private int occurrences;

}
