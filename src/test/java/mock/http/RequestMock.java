package mock.http;

import java.io.File;
import java.util.Map;

import core.http.ContentType;
import core.http.Header;
import core.http.Method;
import core.http.Request;
import core.http.URI;

public class RequestMock implements Request {

	private Method method;
	private URI uri;
	private Map<String, String> headers;
	private String body;

	private RequestMock() {
		this.method = Method.GET;
		this.uri = URI.create("/path?param=value");
		this.body = "body value";
		this.headers = Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length()));
	}

	public static Mock mock() {
		return new RequestMock().new Mock();
	}

	public class Mock {
		private Mock() {}

		public Mock clear() {
			RequestMock.this.method = null;
			RequestMock.this.uri = null;
			RequestMock.this.headers = null;
			RequestMock.this.body = null;
			return this;
		}

		public Mock withMethod(Method method) {
			RequestMock.this.method = method;
			return this;
		}

		public Mock usingUri(URI uri) {
			RequestMock.this.uri = uri;
			return this;
		}

		public Mock containingHeaders(Map<String, String> headers) {
			RequestMock.this.headers = headers;
			return this;
		}

		public Mock withBody(String body) {
			RequestMock.this.body = body;
			return this;
		}

		public RequestMock create() {
			return RequestMock.this;
		}

	}

	public Method getMethod() {
		return method;
	}

	public URI getURI() {
		return uri;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public File getFile() {
		return null;
	}

	public void close() throws Exception {

	}
}
