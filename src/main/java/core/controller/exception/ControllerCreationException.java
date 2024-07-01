package core.controller.exception;

public class ControllerCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ControllerCreationException(String message) {
		super(message);
	}

	public ControllerCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
