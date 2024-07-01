package unit.core.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.Resource;

public class ResourceTest {

	@Test
	void createShouldRaiseAnExceptionWhenInputStreamIsNull() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Resource.create(null));

		Assertions.assertEquals("Resource cannot be created!", exception.getMessage());
	}

	@Test
	void writeShouldNotThrowAnExceptionWhenOutputStreamIsEmpty() throws IOException {
		Resource resource = Resource.create(new ByteArrayInputStream("".getBytes()));

		Assertions.assertDoesNotThrow(() ->resource.write(null));
	}

	@Test
	void writeShouldWriteValueWhenItIsPresentOnTheInputStream() throws IOException {
		String value = "test";
		Resource resource = Resource.create(new ByteArrayInputStream(value.getBytes()));

		OutputStream outputStream = new ByteArrayOutputStream();
		resource.write(outputStream);

		Assertions.assertEquals(value, outputStream.toString());
	}
}
