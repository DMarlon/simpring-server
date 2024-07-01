package unit.core.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.ContentType;
import core.http.DefaultRequest;
import core.http.Header;
import core.http.Method;
import core.http.Request;
import core.http.URI;
import mock.HTTPRequestMock;
import mock.http.RequestMock;

public class DefaultRequestTest {

	@Test
	void createShouldRaiseAnExceptionWhenInputStreamIsNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultRequest.create(null));

		Assertions.assertEquals("Stream cannot be null!", exception.getMessage());
	}

	@Test
	void createShouldNotThrowAnExceptionWhenInputStreamIsEmpty() {
		Assertions.assertDoesNotThrow(() -> DefaultRequest.create(new ByteArrayInputStream(new byte[0])));
	}

	@Test
	void shouldNotLoadValuesWhenFirstLineIsNotMethodAndURI() throws IOException {
		Request request = DefaultRequest.create(new ByteArrayInputStream("/".getBytes()));
		Assertions.assertNull(request.getMethod());
		Assertions.assertNull(request.getURI());

		HTTPRequestMock mock = HTTPRequestMock.mock().using(
			RequestMock.mock()
				.clear()
				.withMethod(Method.GET)
			.create()
		).create();

		request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertNull(request.getMethod());
		Assertions.assertNull(request.getURI());
	}

	@Test
	void shouldGetMethodAndURIFromFirstLine() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.clear()
			.withMethod(Method.GET)
			.usingUri(URI.create("/"))
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
	}

	@Test
	void shouldIgnoreHeaderIfNameIsEmptys() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of("", ""))
			.withBody(null)
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());

		Assertions.assertEquals(0, request.getHeaders().size());
	}

	@Test
	void shouldLoadHeadersEvenWithoutValue() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_TYPE.getName(), ""))
			.withBody(null)
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());

		Assertions.assertEquals(1, request.getHeaders().size());

		String value = request.getHeaders().getOrDefault(Header.CONTENT_TYPE.getName(), "error");
		Assertions.assertEquals("", value);
	}

	@Test
	void shouldNormalizeKnownHeaders() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(" CoNtEnT-tyPe ", "  value  "))
			.withBody(null)
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());

		Assertions.assertEquals(1, request.getHeaders().size());

		String value = request.getHeaders().getOrDefault(Header.CONTENT_TYPE.getName(), "error");
		Assertions.assertEquals("value", value);
	}

	@Test
	void shouldLoadHeaders() throws IOException {
		RequestMock requestMock = RequestMock.mock().withBody(null).create();
		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());

		request.getHeaders().entrySet().forEach(entry -> {
			Assertions.assertTrue(requestMock.getHeaders().containsKey(entry.getKey()));
			Assertions.assertEquals(entry.getValue(), requestMock.getHeaders().getOrDefault(entry.getKey(), ""));
		});
	}

	@Test
	void shouldIgnoreBodyWhenHeaderContentLengthIsMissing() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription()))
			.withBody("body")
		.create();
		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertEquals("", request.getBody());
	}

	@Test
	void shouldIgnoreBodyWhenHeaderContentLengthIsNotANumber() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_LENGTH.getName(), "ABC"))
			.withBody("body")
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertEquals("", request.getBody());
	}

	@Test
	void shouldOnlyLoadBodyWithExactlySizeOfContentLengthHeader() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_LENGTH.getName(), "1"))
			.withBody("body")
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertEquals("b", request.getBody());
	}

	@Test
	void shouldLoadAllBodyEvenIfSizeOnContentLengthHeaderIsBigger() throws IOException {
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_LENGTH.getName(), "200"))
			.withBody("body")
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertEquals("body", request.getBody());
	}

	@Test
	void shouldLoadBody() throws IOException {
		RequestMock requestMock = RequestMock.mock().withBody("body").create();
		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertEquals("body", request.getBody());
	}

	@Test
	void shouldDeleteFileWhenCloseTheRequest() throws Exception {
		String body = "file-body";

		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_TYPE.getName(), ContentType.OCTET_STREAM.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length())))
			.withBody(body)
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertNull(request.getBody());
		Assertions.assertNotNull(request.getFile());

		request.close();

		Assertions.assertFalse(request.getFile().exists());
	}

	@Test
	void shouldLoadTheFile() throws IOException {
		String body = "file-body";
		RequestMock requestMock = RequestMock.mock()
			.containingHeaders(Map.of(Header.CONTENT_TYPE.getName(), ContentType.OCTET_STREAM.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length())))
			.withBody(body)
		.create();

		HTTPRequestMock mock = HTTPRequestMock.mock().using(requestMock).create();

		Request request = DefaultRequest.create(new ByteArrayInputStream(mock.getFullRequest().getBytes()));
		Assertions.assertEquals(requestMock.getMethod(), request.getMethod());
		Assertions.assertEquals(requestMock.getURI().getPath(), request.getURI().getPath());
		Assertions.assertEquals(requestMock.getURI().getQuery(), request.getURI().getQuery());
		Assertions.assertNull(request.getBody());
		Assertions.assertNotNull(request.getFile());

		String content = Files.readAllLines(request.getFile().toPath()).stream().collect(Collectors.joining());
		Assertions.assertEquals(body, content);
	}

}
