package com.company.physical_activity_log_api.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.company.physical_activity_log_api.auth.CurrentUserArgumentResolver;
import com.company.physical_activity_log_api.auth.UserTokenInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final UserTokenInterceptor userTokenInterceptor;
	private final CurrentUserArgumentResolver currentUserArgumentResolver;
	private final CorsProperties corsProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns(corsProperties.getAllowedOriginPatterns().toArray(String[]::new))
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.exposedHeaders("Authorization")
				.allowCredentials(false)
				.maxAge(3600);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userTokenInterceptor);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentUserArgumentResolver);
	}
}

