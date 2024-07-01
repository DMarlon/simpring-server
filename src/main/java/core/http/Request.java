package core.http;

import java.io.File;
import java.util.Map;

public interface Request extends AutoCloseable {

	Method getMethod();

	URI getURI();

	Map<String, String> getHeaders();

	String getBody();

	File getFile();

}