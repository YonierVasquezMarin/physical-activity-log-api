package com.company.physical_activity_log_api.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.company.physical_activity_log_api.auth.AuthExceptions.InvalidAuthTokenException;
import com.company.physical_activity_log_api.auth.AuthExceptions.MissingAuthTokenException;
import com.company.physical_activity_log_api.model.User;
import com.company.physical_activity_log_api.repository.UserRepository;
import com.company.physical_activity_log_api.service.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserTokenInterceptor implements HandlerInterceptor {

	private final JwtService jwtService;
	private final UserRepository userRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (!requiresAuth(handler)) {
			return true;
		}

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			return true;
		}

		String header = request.getHeader("Authorization");
		if (header == null || header.isBlank()) {
			throw new MissingAuthTokenException("Falta el header Authorization");
		}
		if (!header.startsWith("Bearer ")) {
			throw new InvalidAuthTokenException("Authorization debe ser Bearer <token>");
		}

		String token = header.substring("Bearer ".length()).trim();
		if (token.isEmpty()) {
			throw new InvalidAuthTokenException("Token vacío");
		}

		final int userId;
		try {
			userId = jwtService.parseUserId(token);
		} catch (JwtException | IllegalArgumentException ex) {
			throw new InvalidAuthTokenException("Token inválido");
		}

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new InvalidAuthTokenException("Usuario no existe"));

		request.setAttribute(AuthenticatedUserContext.REQUEST_ATTR_USER, user);
		return true;
	}

	private boolean requiresAuth(Object handler) {
		if (!(handler instanceof HandlerMethod handlerMethod)) {
			return false;
		}

		if (handlerMethod.hasMethodAnnotation(UserTokenRequired.class)
				|| handlerMethod.getBeanType().isAnnotationPresent(UserTokenRequired.class)) {
			return true;
		}

		return false;
	}
}

