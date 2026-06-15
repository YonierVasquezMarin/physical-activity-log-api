package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryBreakdownResponse {

	private Integer categoryId;
	private String categoryName;
	private int sessionCount;
	private double percentage;

}
