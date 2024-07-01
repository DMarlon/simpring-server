package core.server;

import java.util.Objects;

public class Argument {
	private static final String PORT_ARG_PATTERN = "--port";
	private static final String POOL_SIZE_ARG_PATTERN = "--pool-size";
	private static final String DIRECTORY_ARG_PATTERN = "--directory";

	private int port;
	private int poolSize;
	private String directory;

	public static Argument process(String[] args) {
		return new Argument(args);
	}

	private Argument(String[] args) {
		this.port = 8080;
		this.poolSize = 8;
		this.directory = System.getProperty("user.dir");
		split(args);
	}

	private void split(String[] args) {
		if (Objects.nonNull(args)) {
			for (int i = 1; i < args.length; i += 2) {
				String argument = args[i - 1];
				String value = args[i];

				if (isPortArg(argument) && isNumber(value)) {
					this.port= Integer.valueOf(value);
				} else if (isPoolSizeArg(argument) && isNumber(value)) {
					this.poolSize = Integer.valueOf(value);
				} else if (isDirectoryArg(argument)) {
					this.directory = value;
				}
			}
		}
	}

	private static boolean isPortArg(String arg) {
		return arg.toLowerCase().equals(PORT_ARG_PATTERN);
	}

	private static boolean isPoolSizeArg(String arg) {
		return arg.toLowerCase().equals(POOL_SIZE_ARG_PATTERN);
	}

	private static boolean isDirectoryArg(String arg) {
		return arg.toLowerCase().equals(DIRECTORY_ARG_PATTERN);
	}

	private static boolean isNumber(String value) {
		return Objects.nonNull(value) && value.matches("^[0-9]+$");
	}

	public int getPort() {
		return port;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public String getDirectory() {
		return directory;
	}
}
