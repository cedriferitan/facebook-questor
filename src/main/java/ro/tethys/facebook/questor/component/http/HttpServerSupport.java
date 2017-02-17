package ro.tethys.facebook.questor.component.http;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.component.Component;

import java.io.IOException;

public class HttpServerSupport implements Component {

	private static final Logger LOG = LoggerFactory.getLogger(HttpServerSupport.class);

	public boolean installed() {
		return true;
	}

	@Override
	public void init(String... args) {
		HttpServer server = new HttpServer();

		NetworkListener listener = new NetworkListener(
				"default", App.getProperties().serverAddress, App.getProperties().serverPort);

		server.addListener(listener);
		App.setHttpServer(server);
	}

	@SuppressWarnings("unchecked")
	public void start(String... args) throws IOException {
		App.getHttpServer().start();
	}

	public void stop() {
		App.getHttpServer().shutdownNow();
	}

	public String name() {
		return "HTTP Server";
	}

}
