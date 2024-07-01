package mock;

import java.util.Objects;
import java.util.StringJoiner;

import core.http.Request;
import mock.http.RequestMock;

public class HTTPRequestMock {

	private Request request;

	private HTTPRequestMock() {
		this.request = RequestMock.mock().create();
	}

	public static Mock mock() {
		return new HTTPRequestMock().new Mock();
	}

	public class Mock {

		public Mock using(Request request) {
			HTTPRequestMock.this.request = request;
			return this;
		}

		public HTTPRequestMock create() {
			return HTTPRequestMock.this;
		}
	}

	public String getFullRequest() {
		StringJoiner joiner = new StringJoiner("\n");

		if (Objects.isNull(request.getURI())) {
			joiner.add(request.getMethod().name());
		} else {
			joiner.add(request.getMethod().name() + " " + request.getURI().getPath() + (Objects.isNull(request.getURI().getQuery()) ? "" : "?"+request.getURI().getQuery()));
		}

		if (Objects.nonNull(request.getHeaders())) {
			request.getHeaders().entrySet().forEach(entry -> joiner.add(entry.getKey() + ":" + entry.getValue()));
		}

		if (Objects.nonNull(request.getBody())) {
			joiner.add("");
			joiner.add(request.getBody());
		}

		return joiner.toString();
	}

}
