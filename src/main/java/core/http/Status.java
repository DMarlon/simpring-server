package core.http;

public enum Status {

	OK(2, 200, "OK"),
	CREATED(2, 201, "Created"),
	NO_CONTENT(2, 204, "No Content"),
	BAD_REQUEST(4, 400, "Bad Request"),
	NOT_FOUND(4, 404, "Not Found"),
	INTERNAL_SERVER_ERROR(5, 500, "Internal Server Error");

	private int type;
	private int code;
	private String description;

	private Status(int type, int code, String description) {
		this.type = type;
		this.code = code;
		this.description = description;
	}

	public boolean isSuccess() {
		return type == 2;
	}

	public boolean isClientError() {
		return type == 4;
	}

	public boolean isServerError() {
		return type == 5;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

}
