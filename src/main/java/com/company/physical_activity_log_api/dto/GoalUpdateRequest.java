package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalUpdateRequest {

	@NotBlank(message = "El título es obligatorio")
	private String title;

	@NotBlank(message = "La descripción es obligatoria")
	private String description;

	@NotNull(message = "La fecha de inicio es obligatoria")
	private OffsetDateTime startDate;

	@NotNull(message = "La fecha de fin es obligatoria")
	private OffsetDateTime endDate;
}

