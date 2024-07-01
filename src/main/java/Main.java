import controller.EchoController;
import controller.FileController;
import controller.RootController;
import controller.UserAgentController;
import core.http.DefaultCustomFactory;
import core.server.Argument;
import core.server.ServerRunner;

public class Main {
	public static void main(String[] args) {
		Argument argument = Argument.process(args);

		ServerRunner.init()
			.onPort(argument.getPort())
			.usingVirtualThreads()
			//.usingRealThreads()
			//.withPoolSize(argument.getPoolSize())
			.usingDirectory(argument.getDirectory())
			.usingCustomFactory(new DefaultCustomFactory())
			.controlling(RootController.class, EchoController.class, UserAgentController.class, FileController.class)
		.run();
	}
}
