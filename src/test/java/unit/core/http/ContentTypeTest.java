package unit.core.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.ContentType;

public class ContentTypeTest {

	@Test
	void getByDescriptionShouldFindValueIgnoringTrimSpaces() {
		ContentType type = ContentType.getByDescription(" application/octet-stream ");

		Assertions.assertNotNull(type);
		Assertions.assertEquals(ContentType.OCTET_STREAM, type);
	}

	@Test
	void getByDescriptionShouldFindValueAsCaseInsensitive() {
		ContentType type = ContentType.getByDescription(" aPPlication/octet-STreaM");

		Assertions.assertNotNull(type);
		Assertions.assertEquals(ContentType.OCTET_STREAM, type);
	}

	@Test
	void getByDescriptionShouldReturnTextPlainWhenValueIsNull() {
		ContentType type = ContentType.getByDescription(null);

		Assertions.assertNotNull(type);
		Assertions.assertEquals(ContentType.TEXT_PLAIN, type);
	}

	@Test
	void getByDescriptionShouldReturnTextPlainWhenNotFoundValue() {
		ContentType type = ContentType.getByDescription("undefined");

		Assertions.assertNotNull(type);
		Assertions.assertEquals(ContentType.TEXT_PLAIN, type);
	}
}
