package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrainingSessionResponse {

	private Integer id;

	private List<ActivityResponse> activities;

	private OffsetDateTime date;

	private String observations;
}

