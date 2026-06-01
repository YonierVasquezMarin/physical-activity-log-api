package com.company.physical_activity_log_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.physical_activity_log_api.auth.CurrentUser;
import com.company.physical_activity_log_api.auth.UserTokenRequired;
import com.company.physical_activity_log_api.dto.GoalCreateRequest;
import com.company.physical_activity_log_api.dto.GoalResponse;
import com.company.physical_activity_log_api.dto.GoalUpdateRequest;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.service.GoalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/goals")
@UserTokenRequired
@RequiredArgsConstructor
public class GoalController {

	private final GoalService goalService;

	@GetMapping
	public ResponseEntity<List<GoalResponse>> list(@CurrentUser User user) {
		return ResponseEntity.ok(goalService.list(user));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GoalResponse> getById(@CurrentUser User user, @PathVariable Integer id) {
		return ResponseEntity.ok(goalService.getById(user, id));
	}

	@PostMapping
	public ResponseEntity<GoalResponse> create(@CurrentUser User user, @Valid @RequestBody GoalCreateRequest request) {
		GoalResponse response = goalService.create(user, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<GoalResponse> update(
			@CurrentUser User user,
			@PathVariable Integer id,
			@Valid @RequestBody GoalUpdateRequest request) {
		return ResponseEntity.ok(goalService.update(user, id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@CurrentUser User user, @PathVariable Integer id) {
		goalService.delete(user, id);
		return ResponseEntity.noContent().build();
	}
}

