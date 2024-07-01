package mock.controller;

import core.controller.Controller;
import core.http.DefaultResponse;
import core.http.Request;
import core.http.Response;
import core.http.Status;
import core.http.URI;

public class ControllerMock implements Controller {

	protected boolean match;
	protected Response response;

	private ControllerMock() {
		this.match = true;
		this.response = DefaultResponse.create(Status.OK, "Success!");

	}

	public static class MockError extends ControllerMock {
		public MockError() {
			this.match = true;
			this.response = DefaultResponse.create(Status.INTERNAL_SERVER_ERROR, "Error!");

		}

	}

	public static class MockSuccess extends ControllerMock  {
		public MockSuccess() {
			this.match = true;
			this.response = DefaultResponse.create(Status.OK, "Success!");

		}

	}

	public static class MockNonMatch extends ControllerMock  {
		public MockNonMatch() {
			this.match = false;
		}

	}

	public static Mock mock() {
		return new ControllerMock().new Mock();
	}

	public class Mock {
		private Mock() {}

		public Mock matching(boolean match) {
			ControllerMock.this.match = match;
			return this;
		}

		public Mock respondingWith(Response response) {
			ControllerMock.this.response = response;
			return this;
		}

		public ControllerMock create() {
			return ControllerMock.this;
		}
	}


	public boolean isMatch(URI uri) {
		return match;
	}

	public Response execute(Request request) {
		return response;
	}

}
