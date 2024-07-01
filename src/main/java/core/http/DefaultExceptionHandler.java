package core.http;

public class DefaultExceptionHandler implements ExceptionHandler {

	public static DefaultExceptionHandler create() {
		return new DefaultExceptionHandler();
	}

	private DefaultExceptionHandler() {}

	public void handle(Throwable exception) {
		System.out.println("Exception Handler: " + exception.getMessage());
	}
}
