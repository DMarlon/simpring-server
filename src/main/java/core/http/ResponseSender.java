package core.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResponseSender {
	private static final String HTTP_VERSION = "HTTP/1.1";
	private static final String CRLF = "\r\n";

	private Response response;

	public static ResponseSender create(Response response) {
		if (Objects.isNull(response)) {
			throw new IllegalArgumentException("Response cannot be null!");
		}

		return new ResponseSender(response);
	}

	private ResponseSender(Response response) {
		this.response = response;
	}

	public void on(OutputStream output) throws IOException {
		if (Objects.isNull(output)) {
			throw new IllegalArgumentException("OutputStream cannot be null!");
		}

		if (Objects.isNull(response.getResource())) {
			simple(output);
		} else {
			resource(output);
		}
	}

	private void simple(OutputStream output) throws IOException {
		String response = getHTTPHeader() + getResponseHeaders() + getBody();
		output.write(response.getBytes());
	}

	private void resource(OutputStream output) throws IOException {
		output.write((getHTTPHeader() + getResponseHeaders() + CRLF + CRLF).getBytes());
		response.getResource().write(output);
	}

	private String getHTTPHeader() {
		return  HTTP_VERSION + " " + response.getStatus().getCode() + " " + response.getStatus().getDescription();
	}

	private String getResponseHeaders() {
		if (response.getHeaders().isEmpty()) {
			return "";
		}

		return CRLF + response.getHeaders().entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(CRLF));
	}

	private String getBody() {
		if (Objects.isNull(response.getBody())) {
			return "";
		}

		return CRLF + CRLF + response.getBody();
	}

}
