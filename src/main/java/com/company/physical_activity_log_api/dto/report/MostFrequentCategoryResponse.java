package com.company.physical_activity_log_api.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MostFrequentCategoryResponse {

	private Integer id;
	private String name;
	private int sessionCount;

}
