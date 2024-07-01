package controller;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Map;

import core.controller.Controller;
import core.http.ContentType;
import core.http.DefaultResponse;
import core.http.Header;
import core.http.Request;
import core.http.Resource;
import core.http.Response;
import core.http.Status;
import core.http.URI;
import core.http.URIProcessor;
import utility.Gzip;

public class EchoController implements Controller {

	private URIProcessor uriProcessor;

	public EchoController(){
		this.uriProcessor = URIProcessor.create("/echo/{str}");
	}

	public boolean isMatch(URI uri) {
		return uriProcessor.isMatch(uri);
	}

	public Response execute(Request request) {
		String value = uriProcessor.getPathVariables().getOrDefault("str", "");

		if (isEncodeNeed(request)) {
			byte[] encode = Gzip.encoding(value);
			return DefaultResponse.create(Status.OK, getHeadersWithContentEncoding(encode), Resource.create(new ByteArrayInputStream(encode)));
		}

		return DefaultResponse.create(Status.OK, getHeaders(value), value);
	}

	private boolean isEncodeNeed(Request request) {
		return Arrays.asList(request.getHeaders().getOrDefault(Header.ACCEPT_ENCODING.getName(), "").split("\\s*,\\\s*")).contains("gzip");
	}

	private Map<String, String> getHeaders(String value) {
		return Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(value.length()));
	}

	private Map<String, String> getHeadersWithContentEncoding(byte[] value) {
		return Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(value.length), Header.CONTENT_ENCODING.getName(), "gzip");
	}
}
