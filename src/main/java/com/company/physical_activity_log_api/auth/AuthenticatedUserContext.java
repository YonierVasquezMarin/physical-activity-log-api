package com.company.physical_activity_log_api.auth;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.company.physical_activity_log_api.model.User;

public final class AuthenticatedUserContext {

	public static final String REQUEST_ATTR_USER = "auth.user";

	private AuthenticatedUserContext() {
	}

	public static User getRequiredUser() {
		RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			return null;
		}
		Object value = attrs.getAttribute(REQUEST_ATTR_USER, RequestAttributes.SCOPE_REQUEST);
		return (User) value;
	}
}

