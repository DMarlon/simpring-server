package mock.http;

import java.util.Map;

import core.http.ContentType;
import core.http.Header;
import core.http.Resource;
import core.http.Response;
import core.http.Status;

public class ResponseMock implements Response {

	private Status status;
	private Map<String, String> headers;
	private String body;
	private Resource resource;

	private ResponseMock() {
		this.status = Status.OK;
		this.body = "body value";
		this.headers = Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length()));
	}

	public static Mock mock() {
		return new ResponseMock().new Mock();
	}

	public class Mock {
		private Mock() {}

		public Mock clear() {
			ResponseMock.this.status = null;
			ResponseMock.this.headers = null;
			ResponseMock.this.body = null;
			ResponseMock.this.resource = null;
			return this;
		}

		public Mock usingStatus(Status status) {
			ResponseMock.this.status = status;
			return this;
		}

		public Mock containingHeaders(Map<String, String> headers) {
			ResponseMock.this.headers = headers;
			return this;
		}

		public Mock withBody(String body) {
			ResponseMock.this.body = body;
			return this;
		}

		public Mock withResource(Resource resource) {
			ResponseMock.this.resource = resource;
			return this;
		}

		public ResponseMock create() {
			return ResponseMock.this;
		}

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

	public void close() throws Exception {
	}
}
