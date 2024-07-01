package core.http;

import java.util.Objects;

public enum Header {
	ACCEPT("Accept"),
	ACCEPT_ENCODING("Accept-Encoding"),
	CONNECTION("Connection"),
	CONTENT_LENGTH("Content-Length"),
	CONTENT_TYPE("Content-Type"),
	CONTENT_ENCODING("Content-Encoding"),
	DATE("Date"),
	HOST("Host"),
	USER_AGENT("User-Agent");

	private String name;

	private Header(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public static String normalize(String name) {
		if (Objects.isNull(name)) {
			return null;
		}

		for (Header header: values()) {
			if (header.getName().toLowerCase().equals(name.toLowerCase().trim())) {
				return header.getName();
			}
		}

		return name;
	}
}
