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
import com.company.physical_activity_log_api.dto.CategoryCreateRequest;
import com.company.physical_activity_log_api.dto.CategoryResponse;
import com.company.physical_activity_log_api.dto.CategoryUpdateRequest;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@UserTokenRequired
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<List<CategoryResponse>> list(@CurrentUser User user) {
		return ResponseEntity.ok(categoryService.list(user));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> getById(@CurrentUser User user, @PathVariable Integer id) {
		return ResponseEntity.ok(categoryService.getById(user, id));
	}

	@PostMapping
	public ResponseEntity<CategoryResponse> create(
			@CurrentUser User user,
			@Valid @RequestBody CategoryCreateRequest request) {
		CategoryResponse response = categoryService.create(user, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(
			@CurrentUser User user,
			@PathVariable Integer id,
			@Valid @RequestBody CategoryUpdateRequest request) {
		return ResponseEntity.ok(categoryService.update(user, id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@CurrentUser User user, @PathVariable Integer id) {
		categoryService.delete(user, id);
		return ResponseEntity.noContent().build();
	}
}
