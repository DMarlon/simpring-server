package mock;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.StringJoiner;

import core.http.Resource;
import core.http.Response;
import mock.http.ResponseMock;

public class HTTPResponseMock {
	private static final String HTTP_VERSION = "HTTP/1.1";
	private static final String CRLF = "\r\n";

	private Response response;

	private HTTPResponseMock() {
		this.response = ResponseMock.mock().create();
	}

	public static Mock mock() {
		return new HTTPResponseMock().new Mock();
	}

	public class Mock {

		public Mock using(Response response) {
			HTTPResponseMock.this.response = response;
			return this;
		}

		public HTTPResponseMock create() {
			return HTTPResponseMock.this;
		}
	}

	public String getFullResponse() {
		StringJoiner joiner = new StringJoiner(CRLF);

		joiner.add(HTTP_VERSION + " " + response.getStatus().getCode() + " " + response.getStatus().getDescription());


		if (Objects.nonNull(response.getHeaders())) {
			response.getHeaders().entrySet().forEach(entry -> joiner.add(entry.getKey() + ": " + entry.getValue()));
		}

		if (Objects.nonNull(response.getResource())) {
			joiner.add("");
			joiner.add(getMessage(response.getResource()));
		}

		if (Objects.nonNull(response.getBody())) {
			joiner.add("");
			joiner.add(response.getBody());
		}

		return joiner.toString();
	}

	private String getMessage(Resource resource) {
		try {
			ByteArrayOutputStream message = new ByteArrayOutputStream();
			response.getResource().write(message);

			return message.toString();
		} catch (Exception exception) {
			return "";
		}
	}

}
