package unit.core.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.controller.ControllerExecutor;
import core.controller.exception.ControllerCreationException;
import core.http.Response;
import core.http.Status;
import mock.controller.ControllerMock;
import mock.http.RequestMock;

public class ControllerExecutorTest {

	@Test
	void shouldNotThrowExceptionWhenControllers() {
		ControllerExecutor executor = ControllerExecutor.control(null);

		Assertions.assertDoesNotThrow(() -> executor.execute(null));
	}

	@Test
	void shouldThrowExceptionWhenThereIsNoPublicNoArgsContructor() {
		ControllerExecutor executor = ControllerExecutor.control(new Class[]{ControllerMock.class});

		ControllerCreationException exception = Assertions.assertThrows(ControllerCreationException.class, () -> executor.execute(RequestMock.mock().create()));
		Assertions.assertEquals("Error to create controller " + ControllerMock.class.getName(), exception.getMessage());
	}

	@Test
	void shouldReturnSucess() {
		ControllerExecutor executor = ControllerExecutor.control(new Class[]{ControllerMock.MockSuccess.class});

		Response response = executor.execute(RequestMock.mock().create());
		Assertions.assertEquals(Status.OK, response.getStatus());
	}

	@Test
	void shouldReturnError() {
		ControllerExecutor executor = ControllerExecutor.control(new Class[]{ControllerMock.MockError.class});

		Response response = executor.execute(RequestMock.mock().create());
		Assertions.assertEquals(Status.INTERNAL_SERVER_ERROR, response.getStatus());
	}

	@Test
	void shouldReturnNotFoundWhenThereIsNoMatch() {
		ControllerExecutor executor = ControllerExecutor.control(new Class[]{ControllerMock.MockNonMatch.class});

		Response response = executor.execute(RequestMock.mock().create());
		Assertions.assertEquals(Status.NOT_FOUND, response.getStatus());
	}
}
