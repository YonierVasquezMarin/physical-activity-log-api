package com.company.physical_activity_log_api.dto.report;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportConsistencyResponse {

	private int currentStreakDays;
	private int longestStreakDays;
	private List<SessionsByWeekResponse> sessionsByWeek;
	private int inactiveDays;

}
