package core.http;

import java.util.Objects;

public enum ContentType {
	TEXT_PLAIN("text/plain"),
	OCTET_STREAM("application/octet-stream");

	String description;

	private ContentType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public static ContentType getByDescription(String description) {
		if (Objects.isNull(description)) {
			return TEXT_PLAIN;
		}

		for (ContentType contentType : values()) {
			if (contentType.getDescription().equals(description.trim().toLowerCase())) {
				return contentType;
			}
		}

		return TEXT_PLAIN;
	}
}
