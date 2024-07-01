package utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {

	public static byte[] encoding(String value) {
		if (Objects.isNull(value)) {
			return new byte[]{};
		}

		try (ByteArrayOutputStream output = new ByteArrayOutputStream(value.length())) {
			try (GZIPOutputStream gzip = new GZIPOutputStream(output)) {
				gzip.write(value.getBytes());
				gzip.finish();
			}
			return output.toByteArray();
		} catch (IOException exception) {
			System.out.println("Error to encode data. Error: " + exception.getMessage());
		}

		return new byte[]{};
	}

	public static byte[] decoding(byte[] value) {
		if (Objects.isNull(value)) {
			return new byte[]{};
		}

		try (ByteArrayInputStream input = new ByteArrayInputStream(value)) {
			try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
				try (GZIPInputStream gzip = new GZIPInputStream(input)) {

					int read;
					byte[] buffer = new byte[1024];
					while ((read = gzip.read(buffer)) != -1) {
						output.write(buffer, 0, read);
					}

					return output.toByteArray();
				}
			}
		} catch (IOException exception) {
			System.out.println("Error to decode data. Error: " + exception.getMessage());
		}

		return new byte[]{};
	}
}
