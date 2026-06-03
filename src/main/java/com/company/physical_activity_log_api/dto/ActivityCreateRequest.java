package com.company.physical_activity_log_api.dto;

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
public class ActivityCreateRequest {

	@NotNull(message = "El categoryId es obligatorio")
	private Integer categoryId;

	@NotBlank(message = "El nombre es obligatorio")
	private String name;

	@NotBlank(message = "La descripción es obligatoria")
	private String description;
}
