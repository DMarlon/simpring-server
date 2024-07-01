package core.http;

import java.util.Objects;

public class URI {
	private final static String URI_QUERY_INDICATOR = "?";

	private String path;
	private String query;

	public static URI create(String uri) {
		return new URI(uri);
	}

	private URI(String uri) {
		this.path = "";
		this.query = "";

		separate(uri);
	}

	private void separate(String uri) {
		if (Objects.nonNull(uri)) {
			int startOfQuery = uri.indexOf(URI_QUERY_INDICATOR);

			if (isOnlyPath(startOfQuery)) {
				this.path = uri;
			} else if (isOnlyQuery(startOfQuery, uri)) {
				this.query = uri.substring(1, uri.length());
			} else if (areBoth(startOfQuery)) {
				this.path = uri.substring(0, startOfQuery);

				if (uri.length() -1 > startOfQuery) {
					this.query = uri.substring(startOfQuery + 1, uri.length());
				}
			}
		}
	}

	private boolean isOnlyPath(int startOfQuery) {
		return startOfQuery == -1;
	}

	private boolean isOnlyQuery(int startOfQuery, String uri) {
		return startOfQuery == 0 && uri.length() > 1;
	}

	private boolean areBoth(int startOfQuery) {
		return startOfQuery > 0;
	}

	public String getPath() {
		return this.path;
	}

	public String getQuery() {
		return this.query;
	}
}
