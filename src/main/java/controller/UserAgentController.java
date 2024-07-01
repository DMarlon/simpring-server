package controller;

import java.util.Map;

import core.controller.Controller;
import core.http.ContentType;
import core.http.DefaultResponse;
import core.http.Header;
import core.http.Request;
import core.http.Response;
import core.http.Status;
import core.http.URI;
import core.http.URIProcessor;

public class UserAgentController implements Controller {

	private URIProcessor uriProcessor;

	public UserAgentController(){
		this.uriProcessor = URIProcessor.create("/user-agent");
	}

	public boolean isMatch(URI uri) {
		return uriProcessor.isMatch(uri);
	}

	public Response execute(Request request) {
		String value = request.getHeaders().getOrDefault(Header.USER_AGENT.getName(), "");

		return DefaultResponse.create(Status.OK, getHeaders(value), value);
	}

	private Map<String, String> getHeaders(String value) {
		return Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(value.length()));
	}

}
