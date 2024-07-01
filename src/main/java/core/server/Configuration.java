package core.server;

import java.io.File;
import java.util.Objects;

public class Configuration {

	private final String directory;

	public static Configuration create(String directory) {
		if (Objects.isNull(directory)) {
			throw new IllegalArgumentException("Directory cannot be null!");
		}

		File file = new File(directory);
		if (!file.exists() || !file.isDirectory()) {
			throw new IllegalArgumentException("Directory is invalid!");
		}

		return new Configuration(directory);
	}

	private Configuration(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return this.directory;
	}

}
