package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
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

	@NotEmpty(message = "Debe incluir al menos una actividad")
	private List<Integer> activityIds;

	@NotNull(message = "La fecha es obligatoria")
	private OffsetDateTime date;

	private String observations;

	private String photoName;
}

