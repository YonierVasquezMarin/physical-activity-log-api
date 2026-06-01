package com.company.physical_activity_log_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.physical_activity_log_api.auth.CurrentUser;
import com.company.physical_activity_log_api.auth.UserTokenRequired;
import com.company.physical_activity_log_api.dto.LoginRequest;
import com.company.physical_activity_log_api.dto.LoginResponse;
import com.company.physical_activity_log_api.dto.MeResponse;
import com.company.physical_activity_log_api.dto.RegisterRequest;
import com.company.physical_activity_log_api.dto.RegisterResponse;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
		RegisterResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		LoginResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@UserTokenRequired
	@GetMapping("/me")
	public ResponseEntity<MeResponse> me(@CurrentUser User user) {
		return ResponseEntity.ok(new MeResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt()));
	}

}
