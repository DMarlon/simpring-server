package core.controller;

import core.http.Request;
import core.http.Response;
import core.http.URI;

public interface Controller {
	boolean isMatch(URI uri);
	Response execute(Request request);
}
