package core.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class URIProcessor {
	private final static String URI_PATH_SEPARATOR = "/";
	private final static String URI_QUERY_SEPARATOR = "&";
	private final static String URI_QUERY_SPLITTER = "=";

	private String pattern;
	private Map<String, String> pathVariables;
	private Map<String, String> queryParameters;

	public static URIProcessor create(String pattern) {
		return new URIProcessor(pattern);
	}

	private URIProcessor(String pattern) {
		this.pattern = pattern;
	}

	public Map<String, String> getPathVariables() {
		return pathVariables;
	}

	public Map<String, String> getQueryParameters() {
		return queryParameters;
	}

	public boolean isMatch(URI uri) {
		pathVariables = Collections.emptyMap();
		queryParameters = Collections.emptyMap();

		if (Objects.isNull(pattern) || Objects.isNull(uri)) {
			return false;
		}

		String[] original = this.pattern.split(URI_PATH_SEPARATOR);
		String[] received = uri.getPath().split(URI_PATH_SEPARATOR);

		if (original.length != received.length) {
			return false;
		}

		processQueryParameters(uri.getQuery());

		boolean matched = true;
		pathVariables = new HashMap<String, String>();
		for (int i = 0; matched && i < original.length; i++) {
			if (isPathParameter(original[i])) {
				pathVariables.put(getPathParameterName(original[i]), received[i]);
			} else if (!original[i].equals(received[i])) {
				matched = false;
			}
		}

		if (!matched) {
			pathVariables.clear();
		}

		return matched;
	}

	private void processQueryParameters(String query) {
		if (query != "") {
			this.queryParameters = new HashMap<String, String>();

			String[] queries = query.split(URI_QUERY_SEPARATOR);

			for (String values : queries) {
				String key = values;
				String value = "";

				int splitter = values.indexOf(URI_QUERY_SPLITTER);
				if (splitter > 0) {
					 key = values.substring(0, splitter);
					 value = values.substring(splitter + 1, values.length());
				}

				this.queryParameters.put(key, value);
			}
		}
	}


	private boolean isPathParameter(String value) {
		return value.length() > 2 && value.startsWith("{") && value.endsWith("}");
	}

	private String getPathParameterName(String value) {
		return value.substring(1, value.length() -1);
	}

}
