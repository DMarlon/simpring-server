package core.server;

import core.controller.Controller;
import core.http.CustomFactory;

public class ServerRunner {

	private int port;
	private int poolSize;
	private String directory;
	private CustomFactory factory;
	private boolean usingVirtualThreads;
	private Class<? extends Controller>[] controllers;

	private ServerRunner() {}

	public static ServerRunner init() {
		return new ServerRunner();
	}

	public ServerRunner onPort(int port) {
		this.port = port;
		return this;
	}

	public ServerRunner usingVirtualThreads() {
		this.usingVirtualThreads = true;
		return this;
	}

	public ServerRunner usingRealThreads() {
		this.usingVirtualThreads = false;
		return this;
	}

	public ServerRunner usingDirectory(String directory) {
		this.directory = directory;
		return this;
	}

	public ServerRunner withPoolSize(int poolSize) {
		this.poolSize = poolSize;
		return this;
	}

	public ServerRunner usingCustomFactory(CustomFactory factory) {
		this.factory = factory;
		return this;
	}

	public ServerRunner controlling(Class<? extends Controller> ...controllers) {
		this.controllers = controllers;
		return this;
	}

	public void run() {
		Server.create(port, usingVirtualThreads, poolSize, directory, factory, controllers).start();
	}


}
