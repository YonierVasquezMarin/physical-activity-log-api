package com.company.physical_activity_log_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.physical_activity_log_api.auth.CurrentUser;
import com.company.physical_activity_log_api.auth.UserTokenRequired;
import com.company.physical_activity_log_api.dto.ActivityCreateRequest;
import com.company.physical_activity_log_api.dto.ActivityResponse;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.service.ActivityService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ActivityController {

	private final ActivityService activityService;

	@GetMapping("/activities")
	public ResponseEntity<List<ActivityResponse>> listAll() {
		return ResponseEntity.ok(activityService.listAll());
	}

	@UserTokenRequired
	@PostMapping("/activities")
	public ResponseEntity<ActivityResponse> create(
			@CurrentUser User user,
			@Valid @RequestBody ActivityCreateRequest request) {
		ActivityResponse response = activityService.create(user, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/categories/{categoryId}/activities")
	@UserTokenRequired
	public ResponseEntity<List<ActivityResponse>> listByCategory(
			@CurrentUser User user,
			@PathVariable Integer categoryId) {
		return ResponseEntity.ok(activityService.listByCategory(user, categoryId));
	}
}

