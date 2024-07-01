package core.controller;

import java.util.Objects;

import core.controller.exception.ControllerCreationException;
import core.http.DefaultResponse;
import core.http.Request;
import core.http.Response;
import core.http.Status;

public class ControllerExecutor {
	private Class<? extends Controller>[] controllers;

	public static ControllerExecutor control(Class<? extends Controller>[] controllers) {
		return new ControllerExecutor(controllers);
	}

	private ControllerExecutor(Class<? extends Controller>[] controllers) {
		this.controllers = controllers;
	}

	public Response execute(Request request) {
		if (Objects.isNull(controllers) || Objects.isNull(request) || Objects.isNull(request.getURI())) {
			return DefaultResponse.create(Status.NOT_FOUND);
		}

		for (Class<? extends Controller> xclass : controllers) {
			Controller controller = create(xclass);

			if (controller.isMatch(request.getURI())) {
				return controller.execute(request);
			}
		}

		return DefaultResponse.create(Status.NOT_FOUND);
	}

	private Controller create(Class<? extends Controller> controller) {
		try {
			return controller.getDeclaredConstructor().newInstance();
		} catch (Exception exception) {
			throw new ControllerCreationException("Error to create controller " + controller.getName(), exception);
		}
	}
}
