package core.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import core.controller.Controller;
import core.controller.ControllerExecutor;
import core.http.CustomFactory;
import core.http.Request;
import core.http.Response;
import core.http.ResponseSender;

public class Server {
	private static Configuration configuration;

	private int port;
	private CustomFactory factory;
	private ExecutorService executor;
	private ServerSocket serverSocket;
	private ControllerExecutor controller;

	public static Server create(int port, boolean usingVirtualThread, int poolSize, String directory, CustomFactory factory, Class<? extends Controller>[] controllers) {
		return new Server(port, usingVirtualThread, poolSize, directory, factory, controllers);
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	private Server(int port, boolean usingVirtualThread, int poolSize, String directory, CustomFactory factory, Class<? extends Controller>[] controllers) {
		if (Objects.isNull(factory)) {
			throw new IllegalArgumentException("Server cannot be created with null factory");
		}

		this.port = port;
		this.factory = factory;
		this.executor = usingVirtualThread ? Executors.newVirtualThreadPerTaskExecutor() : Executors.newFixedThreadPool(poolSize);
		this.controller = ControllerExecutor.control(controllers);

		configuration = Configuration.create(directory);
	}

	public void start() {
		try {
			System.out.println("Starting server...");
			serverSocket = new ServerSocket(port);
			serverSocket.setReuseAddress(true);

			loop();
		} catch (IOException e) {
			System.out.println("Server Exception: " + e.getMessage());
		}
	}

	private void loop() throws IOException {
		System.out.println("Waiting new connection");
		handle(serverSocket.accept());
		loop();
	}

	private void handle(Socket connection) {
		executor.submit(() -> {
			UUID id = UUID.randomUUID();
			try (connection) {
				System.out.println(formattMessage(id, "Accepted new connection!"));

				try (Request request = factory.createRequest(connection.getInputStream())) {
					try (Response response = controller.execute(request)) {
						ResponseSender.create(response).on(connection.getOutputStream());
					}
				}

				System.out.println(formattMessage(id, "Processed connection!"));

			} catch (Exception exception) {
				factory.createExceptionHandler().handle(exception);
			}
		});
	}

	private String formattMessage(UUID id, String message) {
		return "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " - ID=" + id + "] " + message;
	}
}
