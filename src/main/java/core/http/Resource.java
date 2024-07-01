package core.http;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class Resource implements AutoCloseable {

	private final InputStream inputStream;

	public static Resource create(InputStream inputStream) {
		if (Objects.isNull(inputStream)) {
			throw new IllegalArgumentException("Resource cannot be created!");
		}

		return new Resource(inputStream);
	}

	private Resource(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void write(OutputStream output) throws IOException {
		if (Objects.nonNull(output)) {
			int read;
			byte[] buffer = new byte[1024];

			BufferedOutputStream outputStream = new BufferedOutputStream(output);

			while ((read = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, read);
	        }

			outputStream.flush();
		}
	}

	@Override
	public void close() {
		if (Objects.nonNull(inputStream)) {
			try {
				inputStream.close();
			} catch (Exception e) {}
		}
	}
}
