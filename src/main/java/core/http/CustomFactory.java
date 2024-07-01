package core.http;

import java.io.IOException;
import java.io.InputStream;

public interface CustomFactory {

	Request createRequest(InputStream input) throws IOException;
	ExceptionHandler createExceptionHandler();
}
