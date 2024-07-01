package unit.core.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.Header;

public class HeaderTest {

	@Test
	void normalizeShouldFindValueIgnoringTrimSpaces() {
		String header = Header.normalize(" Content-Type ");

		Assertions.assertNotNull(header);
		Assertions.assertEquals(Header.CONTENT_TYPE.getName(), header);
	}

	@Test
	void normalizeShouldFindValueAsCaseInsensitive() {
		String header = Header.normalize("CoNteNt-TyPe");

		Assertions.assertNotNull(header);
		Assertions.assertEquals(Header.CONTENT_TYPE.getName(), header);
	}

	@Test
	void normalizeShouldReturnNullWhenValueIsNull() {
		String header = Header.normalize(null);

		Assertions.assertNull(header);
	}

	@Test
	void normalizeShouldReturnTheSameValueWhenNotFindCorrespondingValue() {
		String originalValue = "UndeFineD";
		String header = Header.normalize(originalValue);

		Assertions.assertNotNull(header);
		Assertions.assertEquals(originalValue, header);
	}
}
