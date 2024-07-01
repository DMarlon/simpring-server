package unit.core.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.URI;

public class URITest {

	private static final String QUERY_SEPARETOR = "?";

	@Test
	void createShouldReturnEmptyStringForPathAndQueryWhenValueIsNull() {
		URI uri = URI.create(null);

		Assertions.assertEquals("", uri.getPath());
		Assertions.assertEquals("", uri.getQuery());
	}

	@Test
	void createShouldReturnEmptyStringForPathAndQueryWhenValueIsEmpty() {
		URI uri = URI.create("");

		Assertions.assertEquals("", uri.getPath());
		Assertions.assertEquals("", uri.getQuery());
	}

	@Test
	void createShouldReturnEmptyValuesWhenThereIsOnlyQuerySeparator() {
		URI uri = URI.create(QUERY_SEPARETOR);

		Assertions.assertEquals("", uri.getPath());
		Assertions.assertEquals("", uri.getQuery());
	}

	@Test
	void createShouldReturnQueryWhenThereIsNoPathSeparator() {
		String value = "value";
		URI uri = URI.create(QUERY_SEPARETOR+value);

		Assertions.assertEquals("", uri.getPath());
		Assertions.assertEquals(value, uri.getQuery());
	}

	@Test
	void createShouldReturnQueryStartingFromTheFirstSeparatorFoundWhenThereIsMoreThanOneQuerySeparator() {
		String value1 = "value1";
		String value2 = "value1";
		URI uri = URI.create(QUERY_SEPARETOR+value1+QUERY_SEPARETOR+value2);

		Assertions.assertEquals("", uri.getPath());
		Assertions.assertEquals(value1+QUERY_SEPARETOR+value2, uri.getQuery());
	}

	@Test
	void createShouldReturnValueOnPathWhenValueThereIsNoSeparation() {
		String value = "/";
		URI uri = URI.create(value);

		Assertions.assertEquals(value, uri.getPath());
		Assertions.assertEquals("", uri.getQuery());
	}

	@Test
	void createShouldReturnPathWhenThereIsNoQuerySeparation() {
		String value = "/my/path/";
		URI uri = URI.create(value);

		Assertions.assertEquals(value, uri.getPath());
		Assertions.assertEquals("", uri.getQuery());
	}

	@Test
	void createShouldReturnPathAndIgnoreingQueryWhenThereIsNoValueAfterQuerySeparator() {
		String value = "/";
		URI uri = URI.create(value+QUERY_SEPARETOR);

		Assertions.assertEquals(value, uri.getPath());
		Assertions.assertEquals("", uri.getQuery());
	}

	@Test
	void createShouldReturnPathAndQueryWhenThereAreBothValues() {
		String path = "/";
		String query = "key=value";
		URI uri = URI.create(path+QUERY_SEPARETOR+query);

		Assertions.assertEquals(path, uri.getPath());
		Assertions.assertEquals(query, uri.getQuery());
	}

}
