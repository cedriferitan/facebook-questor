package ro.tethys.facebook.questor.component.http;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.component.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		try {
			setupSsl(listener);
		} catch (IOException e) {
			throw new RuntimeException("Cannot start HTTP server", e);
		}

		server.addListener(listener);
		App.setHttpServer(server);
	}

	@SuppressWarnings("unchecked")
	public void start(String... args) throws IOException {
		App.getHttpServer().start();
	}

	private void setupSsl(NetworkListener listener) throws IOException {
		System.getProperties().load(HttpServerSupport.class.getClassLoader().getResourceAsStream("ssl.properties"));
		boolean sslOn = System.getProperty("ssl.disable", "false").equals("false");
		if (sslOn) {
			if (!Files.exists(Paths.get(System.getProperty("javax.net.ssl.keyStore")))) {
				LOG.error("Certificate missing, path is: " + System.getProperty("javax.net.ssl.keyStore"));
				throw new IllegalStateException("Check SSL Configuration");
			}

			SSLEngineConfigurator configurator = new SSLEngineConfigurator(new SSLContextConfigurator());
			configurator.setClientMode(false);
			configurator.setNeedClientAuth(false);

			listener.setSecure(true);
			listener.setSSLEngineConfig(configurator);
		} else {
			LOG.warn("SSL has been disabled from ssl.properties. Make sure you use http:// in your browser.");
		}

	}

	public void stop() {
		App.getHttpServer().shutdownNow();
	}

	public String name() {
		return "HTTP Server";
	}

}
