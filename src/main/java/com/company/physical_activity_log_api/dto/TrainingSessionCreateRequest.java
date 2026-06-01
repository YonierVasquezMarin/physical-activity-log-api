package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionCreateRequest {

	@NotNull(message = "El activityId es obligatorio")
	private Integer activityId;

	@NotNull(message = "La fecha es obligatoria")
	private OffsetDateTime date;

	private String observations;
}

