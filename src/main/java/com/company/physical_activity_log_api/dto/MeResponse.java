package com.company.physical_activity_log_api.dto;

import java.time.OffsetDateTime;

public class MeResponse {

	private Integer id;
	private String name;
	private String email;
	private OffsetDateTime createdAt;

	public MeResponse(Integer id, String name, String email, OffsetDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.createdAt = createdAt;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}
}

