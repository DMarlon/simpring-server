package core.http;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class DefaultResponse implements Response, AutoCloseable {

	private final Status status;
	private final Map<String, String> headers;
	private final String body;
	private final Resource resource;

	public static Response create(Status status) {
		return DefaultResponse.create(status, Collections.emptyMap(), (String) null);
	}

	public static Response create(Status status, String body) {
		Map<String, String> header = Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length()));
		return DefaultResponse.create(status, header, body);
	}

	public static Response create(Status status, Map<String, String> headers, String body) {
		if (Objects.isNull(status) || Objects.isNull(headers)) {
			throw new IllegalArgumentException("Response cannot be created!");
		}

		return new DefaultResponse(status, headers, body, null);
	}

	public static Response create(Status status, Map<String, String> headers, Resource resource) {
		if (Objects.isNull(resource)) {
			throw new IllegalArgumentException("Response cannot be created!");
		}

		if (Objects.isNull(status) || Objects.isNull(headers)) {
			resource.close();

			throw new IllegalArgumentException("Response cannot be created!");
		}

		return new DefaultResponse(status, headers, null, resource);
	}

	private DefaultResponse(Status status, Map<String, String> headers, String body, Resource resource) {
		this.status = status;
		this.headers = headers;
		this.body = body;
		this.resource = resource;
	}

	public Status getStatus() {
		return status;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public Resource getResource() {
		return resource;
	}

	public void close() {
		if (Objects.nonNull(resource)) {
			resource.close();
		}
	}
}
