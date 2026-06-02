package com.company.physical_activity_log_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {

	private Integer id;
	private Integer userId;
	private String name;
	private String description;
}

