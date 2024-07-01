package unit.core.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.ContentType;
import core.http.DefaultResponse;
import core.http.Header;
import core.http.Resource;
import core.http.Response;
import core.http.Status;

public class DefaultResponseTest {

	@Test
	void createShouldThrowAnExceptionWhenStatusIsNull() throws IOException {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultResponse.create(null, Collections.emptyMap(), ""));

		Assertions.assertEquals("Response cannot be created!", exception.getMessage());
	}

	@Test
	void createShouldThrowAnExceptionWhenHeaderIsNull() throws IOException {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultResponse.create(Status.OK, null, ""));

		Assertions.assertEquals("Response cannot be created!", exception.getMessage());
	}

	@Test
	void createShouldThrowAnExceptionWhenResourceIsNull() throws IOException {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultResponse.create(Status.OK, Collections.emptyMap(), (Resource)null));

		Assertions.assertEquals("Response cannot be created!", exception.getMessage());
	}


	@Test
	void createShouldThrowAnExceptionAndCloseResourceWhenStatusIsNull() throws IOException {
		Resource resource = Resource.create(InputStream.nullInputStream());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultResponse.create(null, Collections.emptyMap(), resource));
		Assertions.assertEquals("Response cannot be created!", exception.getMessage());

		exception = Assertions.assertThrows(IOException.class, () -> resource.write(new ByteArrayOutputStream()));
		Assertions.assertEquals("Stream closed", exception.getMessage());
	}

	@Test
	void createShouldThrowAnExceptionAndCloseResourceWhenHeaderIsNull() throws IOException {
		Resource resource = Resource.create(InputStream.nullInputStream());

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> DefaultResponse.create(Status.OK, null, resource));
		Assertions.assertEquals("Response cannot be created!", exception.getMessage());

		exception = Assertions.assertThrows(IOException.class, () -> resource.write(new ByteArrayOutputStream()));
		Assertions.assertEquals("Stream closed", exception.getMessage());
	}

	@Test
	void createShouldBeCreatedOnlyWithStatus() throws IOException {
		Response response = DefaultResponse.create(Status.BAD_REQUEST);


		Assertions.assertEquals(Status.BAD_REQUEST, response.getStatus());
		Assertions.assertTrue(response.getHeaders().isEmpty());
		Assertions.assertNull(response.getBody());
		Assertions.assertNull(response.getResource());
	}

	@Test
	void createShouldBeCreatedWithStatusAndBodyAndAutomaticallySetHeaders() throws IOException {
		String body = "body";
		Response response = DefaultResponse.create(Status.OK, body);


		Assertions.assertEquals(Status.OK, response.getStatus());
		Assertions.assertEquals(ContentType.TEXT_PLAIN.getDescription(), response.getHeaders().getOrDefault(Header.CONTENT_TYPE.getName(), ""));
		Assertions.assertEquals(String.valueOf(body.length()), response.getHeaders().getOrDefault(Header.CONTENT_LENGTH.getName(), ""));
		Assertions.assertEquals(body, response.getBody());
		Assertions.assertNull(response.getResource());
	}

	@Test
	void shouldCloseResourceWhenResponseClose() throws Exception {
		Resource resource = Resource.create(InputStream.nullInputStream());

		Response response = DefaultResponse.create(Status.OK, Collections.emptyMap(), resource);
		response.close();

		IOException exception = Assertions.assertThrows(IOException.class, () -> resource.write(new ByteArrayOutputStream()));
		Assertions.assertEquals("Stream closed", exception.getMessage());
	}

	@Test
	void shouldNotRaiseExceptionWhenResponseCloseAndThereIsNotResource() throws Exception {
		Response response = DefaultResponse.create(Status.OK, Collections.emptyMap(), "");

		Assertions.assertDoesNotThrow(() -> response.close());
	}
}
