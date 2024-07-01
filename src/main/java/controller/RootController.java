package controller;

import core.controller.Controller;
import core.http.DefaultResponse;
import core.http.Request;
import core.http.Response;
import core.http.Status;
import core.http.URI;

public class RootController implements Controller {

	public boolean isMatch(URI uri) {
		return "".equals(uri.getPath()) || "/".equals(uri.getPath());
	}

	public Response execute(Request request) {
		return DefaultResponse.create(Status.OK);
	}
}
