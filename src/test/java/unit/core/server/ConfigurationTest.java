package unit.core.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.server.Configuration;

public class ConfigurationTest {

	@Test
	void shouldThrowExceptionWhenCreateUsingNullDirectory() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Configuration.create(null));

		Assertions.assertEquals("Directory cannot be null!", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenCreateUsingInvalidDirectory() {
		IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> Configuration.create("marlon"));

		Assertions.assertEquals("Directory is invalid!", exception.getMessage());
	}

	@Test
	void shouldGetDirectory() {
		Configuration configuration = Configuration.create(System.getProperty("user.dir"));

		Assertions.assertEquals(System.getProperty("user.dir"), configuration.getDirectory());
	}

}
