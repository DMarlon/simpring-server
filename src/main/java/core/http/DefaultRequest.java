package core.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class DefaultRequest implements AutoCloseable, Request {

	private Method method;
	private URI uri;
	private Map<String, String> headers;
	private String body;
	private File file;

	public static Request create(InputStream stream) throws IOException {
		if (Objects.isNull(stream)) {
			throw new IllegalArgumentException("Stream cannot be null!");
		}

		return new DefaultRequest(stream);
	}

	private DefaultRequest(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		this.request(reader.readLine());
		if (Objects.nonNull(method)) {
			this.headers(reader);
			this.body(reader);
		}

	}

	private void request(String request) throws IOException {
		if (Objects.nonNull(request)) {
			String[] values = request.split(" ");
			if (values.length > 1) {
				this.method = Method.get(values[0]);
				this.uri = URI.create(values[1]);
			}
		}
	}

	private void headers(BufferedReader reader) throws IOException {
		String line;
		headers = new HashMap<String, String>();

		while (Objects.nonNull((line = reader.readLine())) && !line.isEmpty()) {
			int splitter = line.indexOf(":");
			if (splitter > 0) {
				String header = Header.normalize(line.substring(0, splitter).trim());
				String value = line.length() - 1 > splitter ? line.substring(splitter + 1, line.length()).trim() : "";

				headers.put(header, value);
			}
		}
	}

	private void body(BufferedReader reader) throws IOException {
		ContentType contentType = ContentType.getByDescription(headers.getOrDefault(Header.CONTENT_TYPE.getName(), ""));

		switch (contentType) {
			case OCTET_STREAM:
				loadFile(reader);
				break;
			default:
				loadBody(reader);
		}
	}

	private void loadBody(BufferedReader reader) throws IOException {
		StringBuffer body = new StringBuffer();
		processBody(reader, (value, read) -> body.append(value, 0, read));
	    this.body = body.toString();
	}

	private void loadFile(BufferedReader reader) throws IOException {
        File file = File.createTempFile("req", ".tmp");

        try (FileWriter writer = new FileWriter(file)) {
			processBody(reader, (value, read) -> {
				try {
					writer.write(value);
				} catch (IOException exception) {}
			});
        }

		this.file = file;
	}

	private void processBody(BufferedReader reader, BiConsumer<char[], Integer> writer) throws IOException {
		String contentLength = headers.getOrDefault(Header.CONTENT_LENGTH.getName(), "");

		if (!contentLength.isEmpty() && contentLength.trim().matches("^[0-9]+$")) {
			int read = 0;
			int length = Integer.parseInt(contentLength);

			while (length > 0) {
				int buffer = length > 1024 ? 1024 : length;
				char[] value = new char[buffer];

				read = reader.read(value, 0, buffer);

				if (read < 1) {
					break;
				}

				length -= read;

				writer.accept(value, read);
			}
		}
	}

	public Method getMethod() {
		return this.method;
	}

	public URI getURI() {
		return this.uri;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public String getBody() {
		return this.body;
	}

	public File getFile() {
		return file;
	}

	public void close() {
		if (Objects.nonNull(file) && file.exists()) {
			file.delete();
		}
	}
}
