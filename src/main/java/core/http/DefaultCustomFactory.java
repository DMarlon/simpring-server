package core.http;

import java.io.IOException;
import java.io.InputStream;

public class DefaultCustomFactory implements CustomFactory {

	public Request createRequest(InputStream input) throws IOException {
		return DefaultRequest.create(input);
	}

	public ExceptionHandler createExceptionHandler() {
		return DefaultExceptionHandler.create();
	}

}
