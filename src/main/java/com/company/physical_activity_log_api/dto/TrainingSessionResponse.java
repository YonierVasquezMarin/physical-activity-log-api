package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainingSessionResponse {

	private Integer id;

	private Integer activityId;
	private String activityName;

	private Integer categoryId;
	private String categoryName;

	private OffsetDateTime date;

	private String observations;
}

