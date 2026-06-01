package com.company.physical_activity_log_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.physical_activity_log_api.dto.ActivityResponse;
import com.company.physical_activity_log_api.service.ActivityService;

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

	@GetMapping("/categories/{categoryId}/activities")
	public ResponseEntity<List<ActivityResponse>> listByCategory(@PathVariable Integer categoryId) {
		return ResponseEntity.ok(activityService.listByCategory(categoryId));
	}
}

