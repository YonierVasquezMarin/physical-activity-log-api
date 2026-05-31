package com.company.physical_activity_log_api.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {

	private Integer id;

	private String name;

	private String email;

	private LocalDateTime createdAt;

}
