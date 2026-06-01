package com.company.physical_activity_log_api.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.company.physical_activity_log_api.config.JwtProperties;
import com.company.physical_activity_log_api.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtProperties jwtProperties;

	public String generateToken(User user) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + jwtProperties.getExpirationMs());

		return Jwts.builder()
				.subject(String.valueOf(user.getId()))
				.claim("email", user.getEmail())
				.issuedAt(now)
				.expiration(expiration)
				.signWith(getSigningKey())
				.compact();
	}

	public int parseUserId(String token) throws JwtException, IllegalArgumentException {
		String subject = Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
		return Integer.parseInt(subject);
	}

	public long getExpirationMs() {
		return jwtProperties.getExpirationMs();
	}

	private SecretKey getSigningKey() {
		var secret = jwtProperties.getSecret();
		if (secret == null || secret.isEmpty()) {
			throw new IllegalStateException("JWT secret is not configured");
		}
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

}
