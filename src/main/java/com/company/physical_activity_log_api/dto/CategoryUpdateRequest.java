package com.company.physical_activity_log_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {

	@NotBlank(message = "El nombre es obligatorio")
	private String name;

	private String description;
}
