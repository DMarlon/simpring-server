package core.http;

import java.util.Objects;

public enum Method {
	GET,
	HEAD,
	POST,
	PUT,
	DELETE,
	CONNECT,
	OPTIONS,
	TRACE,
	PATCH;

	public static Method get(String method) {
		if (Objects.isNull(method)) {
			throw new IllegalArgumentException("Method cannot be null!");
		}


		for (Method value : values()) {
			if (value.name().equals(method.trim().toUpperCase())) {
				return value;
			}
		}

		throw new IllegalArgumentException("Method " + method + " not found!");
	}
}
