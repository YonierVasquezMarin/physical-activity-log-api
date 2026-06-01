package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoalResponse {

	private Integer id;
	private String title;
	private String description;
	private OffsetDateTime startDate;
	private OffsetDateTime endDate;
}

