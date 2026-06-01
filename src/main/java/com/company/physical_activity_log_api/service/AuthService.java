package com.company.physical_activity_log_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.physical_activity_log_api.dto.LoginRequest;
import com.company.physical_activity_log_api.dto.LoginResponse;
import com.company.physical_activity_log_api.dto.RegisterRequest;
import com.company.physical_activity_log_api.dto.RegisterResponse;
import com.company.physical_activity_log_api.exception.EmailAlreadyExistsException;
import com.company.physical_activity_log_api.exception.InvalidCredentialsException;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	public RegisterResponse register(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("El email ya está registrado");
		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

		User savedUser = userRepository.save(user);

		return new RegisterResponse(
				savedUser.getId(),
				savedUser.getName(),
				savedUser.getEmail(),
				savedUser.getCreatedAt());
	}

	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
			throw new InvalidCredentialsException("Credenciales inválidas");
		}

		String token = jwtService.generateToken(user);

		return new LoginResponse(token, "Bearer", jwtService.getExpirationMs());
	}

}
