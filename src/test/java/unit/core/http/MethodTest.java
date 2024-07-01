package unit.core.http;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.Method;

public class MethodTest {

	@Test
	void getShouldFindValueIgnoringTrimSpaces() {
		Method method = Method.get(" get ");

		Assertions.assertNotNull(method);
		Assertions.assertEquals(Method.GET, method);
	}

	@Test
	void getShouldFindValueAsCaseInsensitive() {
		Method method = Method.get("gEt");

		Assertions.assertNotNull(method);
		Assertions.assertEquals(Method.GET, method);
	}

	@Test
	void getShouldThrowAnExceptionWhenValueIsNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Method.get(null));

		Assertions.assertNotNull(exception);
		Assertions.assertEquals("Method cannot be null!", exception.getMessage());
	}

	@Test
	void getByDescriptionShouldReturnTextPlainWhenNotFoundValue() {
		String invalidMethod = "NO";
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Method.get(invalidMethod));

		Assertions.assertNotNull(exception);
		Assertions.assertEquals("Method " + invalidMethod + " not found!", exception.getMessage());
	}
}
