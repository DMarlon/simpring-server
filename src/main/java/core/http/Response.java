package core.http;

import java.util.Map;

public interface Response extends AutoCloseable {

	public Status getStatus();
	public Map<String, String> getHeaders();
	public String getBody();
	public Resource getResource();
}
