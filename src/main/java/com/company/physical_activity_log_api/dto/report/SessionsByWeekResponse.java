package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionsByWeekResponse {

	private String week;
	private int count;

}
