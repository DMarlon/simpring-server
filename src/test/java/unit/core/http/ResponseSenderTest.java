package unit.core.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.ContentType;
import core.http.DefaultResponse;
import core.http.Header;
import core.http.Resource;
import core.http.Response;
import core.http.ResponseSender;
import core.http.Status;
import mock.HTTPResponseMock;
import mock.http.ResponseMock;

public class ResponseSenderTest {

	@Test
	void createShouldThrowAnExceptionWhenResponseIsNull() throws IOException {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> ResponseSender.create(null));

		Assertions.assertEquals("Response cannot be null!", exception.getMessage());
	}

	@Test
	void onShouldThrowAnExceptionWhenOutputStreamIsNull() throws IOException {
		Response response = DefaultResponse.create(Status.OK, Collections.emptyMap(),"");

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> ResponseSender.create(response).on(null));
		Assertions.assertEquals("OutputStream cannot be null!", exception.getMessage());
	}

	@Test
	void shouldResponseOnlyWithStatus() throws IOException {
		OutputStream stream = new ByteArrayOutputStream();

		ResponseSender.create(DefaultResponse.create(Status.BAD_REQUEST)).on(stream);

		HTTPResponseMock mock = HTTPResponseMock.mock().using(
			ResponseMock.mock()
				.clear()
				.usingStatus(Status.BAD_REQUEST)
			.create()
		).create();

		Assertions.assertEquals(mock.getFullResponse(), stream.toString());
	}

	@Test
	void createShouldBeCreatedWithStatusAndBodyAndAutomaticallySetHeaders() throws IOException {
		String body = "body";
		OutputStream stream = new ByteArrayOutputStream();

		ResponseSender.create(DefaultResponse.create(Status.OK, body)).on(stream);

		HTTPResponseMock mock = HTTPResponseMock.mock().using(
			ResponseMock.mock()
				.clear()
				.usingStatus(Status.OK)
				.containingHeaders(Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length())))
				.withBody(body)
			.create()
		).create();

		Assertions.assertEquals(mock.getFullResponse(), stream.toString());
	}

	@Test
	void onShouldResponseWithoutBodyWhenBodyIsNull() throws IOException {
		String body = null;
		Map<String, String> headers = Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), "0");
		OutputStream stream = new ByteArrayOutputStream();

		ResponseSender.create(DefaultResponse.create(Status.OK, headers, body)).on(stream);

		HTTPResponseMock mock = HTTPResponseMock.mock().using(
			ResponseMock.mock()
				.clear()
				.usingStatus(Status.OK)
				.containingHeaders(headers)
				.withBody(body)
			.create()
		).create();

		Assertions.assertEquals(mock.getFullResponse(), stream.toString());
	}

	@Test
	void onShouldResponseWithBody() throws IOException {
		String body = "body";
		Map<String, String> headers = Map.of(Header.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(body.length()));
		OutputStream stream = new ByteArrayOutputStream();

		ResponseSender.create(DefaultResponse.create(Status.OK, headers, body)).on(stream);

		HTTPResponseMock mock = HTTPResponseMock.mock().using(
			ResponseMock.mock()
				.clear()
				.usingStatus(Status.OK)
				.containingHeaders(headers)
				.withBody(body)
			.create()
		).create();

		Assertions.assertEquals(mock.getFullResponse(), stream.toString());
	}

	@Test
	void onShouldResponseWithResource() throws IOException {
		String message = "resource";
		Resource resource = Resource.create(new ByteArrayInputStream(message.getBytes()));
		Map<String, String> headers = Map.of(Header.CONTENT_TYPE.getName(), ContentType.OCTET_STREAM.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(message.length()));
		OutputStream stream = new ByteArrayOutputStream();

		ResponseSender.create(DefaultResponse.create(Status.OK, headers, resource)).on(stream);

		HTTPResponseMock mock = HTTPResponseMock.mock().using(
			ResponseMock.mock()
				.clear()
				.usingStatus(Status.OK)
				.containingHeaders(headers)
				.withResource(Resource.create(new ByteArrayInputStream(message.getBytes())))
			.create()
		).create();

		Assertions.assertEquals(mock.getFullResponse(), stream.toString());
	}
}
