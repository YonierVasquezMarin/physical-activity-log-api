package com.company.physical_activity_log_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityResponse {

	private Integer id;
	private Integer categoryId;
	private String name;
	private String description;
}

