package unit.core.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.server.Argument;

public class ArgumentTest {

	private final int DEFAULT_PORT = 8080;
	private final int DEFAULT_POOL_SIZE = 8;
	private final String DEFAULT_DIRECTORY = System.getProperty("user.dir");

	@Test
	void shouldProcessNotThrowExceptionWhenArgumentIsNull() {
		Assertions.assertDoesNotThrow(() -> Argument.process(null));
	}

	@Test
	void shouldProcessDefaultValueWhenArgumentsAreNotInformed() {
		Argument argument = Argument.process(null);

		Assertions.assertEquals(DEFAULT_PORT, argument.getPort());
		Assertions.assertEquals(DEFAULT_POOL_SIZE, argument.getPoolSize());
		Assertions.assertEquals(DEFAULT_DIRECTORY, argument.getDirectory());
	}

	@Test
	void shouldIgnorePortArgumentWhenThereIsNotValue() {
		Argument argument = Argument.process(new String[]{"--port"});

		Assertions.assertEquals(DEFAULT_PORT, argument.getPort());
	}

	@Test
	void shouldIgnorePortArgumentWhenValueIsNotANumber() {
		Argument argument = Argument.process(new String[]{"--port", "abc"});

		Assertions.assertEquals(DEFAULT_PORT, argument.getPort());
	}

	@Test
	void shouldPortArgumentBeRetrieveIgnoringCases() {
		Argument argument = Argument.process(new String[]{"--PorT", "123"});

		Assertions.assertEquals(123, argument.getPort());
	}

	@Test
	void shouldIgnorePoolSizeArgumentWhenThereIsNotValue() {
		Argument argument = Argument.process(new String[]{"--pool-size"});

		Assertions.assertEquals(DEFAULT_POOL_SIZE, argument.getPoolSize());
	}

	@Test
	void shouldIgnorePoolSizeArgumentWhenValueIsNotANumber() {
		Argument argument = Argument.process(new String[]{"--pool-size", "abc"});

		Assertions.assertEquals(DEFAULT_POOL_SIZE, argument.getPoolSize());
	}

	@Test
	void shouldPoolSizeArgumentBeRetrieveIgnoringCases() {
		Argument argument = Argument.process(new String[]{"--PoOl-SiZe", "123"});

		Assertions.assertEquals(123, argument.getPoolSize());
	}

	@Test
	void shouldIgnoreDirectoryArgumentWhenThereIsNotValue() {
		Argument argument = Argument.process(new String[]{"--directory"});

		Assertions.assertEquals(DEFAULT_DIRECTORY, argument.getDirectory());
	}

	@Test
	void shouldDirectoryArgumentBeRetrieveIgnoringCases() {
		Argument argument = Argument.process(new String[]{"--DiReCtoRy", "/"});

		Assertions.assertEquals("/", argument.getDirectory());
	}
}
