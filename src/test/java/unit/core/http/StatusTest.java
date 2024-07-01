package unit.core.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.Status;

public class StatusTest {

	@Test
	void shouldBeSuccessWhenCodeStartWith200() {
		Status[] status = Status.values();

		for (Status value : status) {
			if (Integer.toString(value.getCode()).charAt(0) == '2') {
				Assertions.assertTrue(value.isSuccess());
			}
		}
	}

	@Test
	void shouldBeClientErrorWhenCodeStartWith400() {
		Status[] status = Status.values();

		for (Status value : status) {
			if (Integer.toString(value.getCode()).charAt(0) == '4') {
				Assertions.assertTrue(value.isClientError());
			}
		}
	}

	@Test
	void shouldBeServerErrorWhenCodeStartWith500() {
		Status[] status = Status.values();

		for (Status value : status) {
			if (Integer.toString(value.getCode()).charAt(0) == '5') {
				Assertions.assertTrue(value.isServerError());
			}
		}
	}

}
