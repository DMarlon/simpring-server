package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import core.controller.Controller;
import core.http.ContentType;
import core.http.DefaultResponse;
import core.http.Header;
import core.http.Request;
import core.http.Resource;
import core.http.Response;
import core.http.Status;
import core.http.URI;
import core.http.URIProcessor;
import core.server.Server;

public class FileController implements Controller {

	private URIProcessor uriProcessor;

	public FileController(){
		this.uriProcessor = URIProcessor.create("/files/{name}");
	}

	public boolean isMatch(URI uri) {
		return uriProcessor.isMatch(uri);
	}

	public Response execute(Request request) {
		String name = uriProcessor.getPathVariables().getOrDefault("name", "");

		switch (request.getMethod()) {
			case POST:
				return write(name, request);
			default:
				return read(name);
		}
	}

	private Response read(String name) {
		try {
			File file = new File(Server.getConfiguration().getDirectory() + name);
			if (!file.exists() || !file.canRead()) {
				return DefaultResponse.create(Status.NOT_FOUND);
			}

			return DefaultResponse.create(Status.OK, getHeaders(file.length()), Resource.create(new FileInputStream(file)));
		} catch (FileNotFoundException exception) {
			System.out.println("File not found : " + Server.getConfiguration().getDirectory() + name);
			return DefaultResponse.create(Status.NOT_FOUND);
		}
	}

	private Response write(String name, Request request) {
		ContentType contentType = ContentType.getByDescription(request.getHeaders().getOrDefault(Header.CONTENT_TYPE, ""));

        try {
        	File file = new File(Server.getConfiguration().getDirectory() + name);
    		if (file.exists()) {
    			return DefaultResponse.create(Status.BAD_REQUEST, "File "+ name +" already exits"+ file.getAbsolutePath());
    		}

    		if (ContentType.OCTET_STREAM.equals(contentType)) {
    			Files.copy(request.getFile().toPath(), file.toPath());
    		} else {
    			try (FileWriter writer = new FileWriter(file)) {
    	            writer.write(request.getBody());
    	        }
    		}

		} catch (IOException exception) {
			return DefaultResponse.create(Status.INTERNAL_SERVER_ERROR, "Error to create file. Error:" + exception.getMessage());
		}

		return DefaultResponse.create(Status.CREATED);
	}

	private Map<String, String> getHeaders(Long size) {
		return Map.of(Header.CONTENT_TYPE.getName(), ContentType.OCTET_STREAM.getDescription(), Header.CONTENT_LENGTH.getName(), String.valueOf(size));
	}
}
