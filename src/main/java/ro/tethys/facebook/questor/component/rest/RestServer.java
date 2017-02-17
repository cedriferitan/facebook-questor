package ro.tethys.facebook.questor.component.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.component.Component;

public class RestServer implements Component {
	private GrizzlyHttpContainer handler;

	public boolean installed() {
		return true;
	}

	@Override
	public void init(String... args) {
		ResourceConfig resourceConfig = new ResourceConfig()
				.packages(
						"ro.tethys.facebook.questor.api",
						"com.wordnik.swagger.jaxrs.listing")
				.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
				.register(JacksonJaxbJsonProvider.class)
				.registerInstances(new RolesAllowedDynamicFeature());
		handler = new GrizzlyHttpContainerProvider().createContainer(GrizzlyHttpContainer.class, resourceConfig);
	}

	public void start(String... args) {
		App.getHttpServer().getServerConfiguration().addHttpHandler(handler, "/api");
	}

	public void stop() {
		//nothing to stop
	}

	public String name() {
		return "REST Server";
	}

}
