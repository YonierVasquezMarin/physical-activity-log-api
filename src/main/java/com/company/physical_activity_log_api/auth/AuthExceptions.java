package com.company.physical_activity_log_api.auth;

public final class AuthExceptions {

	private AuthExceptions() {
	}

	public static class MissingAuthTokenException extends RuntimeException {
		public MissingAuthTokenException(String message) {
			super(message);
		}
	}

	public static class InvalidAuthTokenException extends RuntimeException {
		public InvalidAuthTokenException(String message) {
			super(message);
		}
	}

}

