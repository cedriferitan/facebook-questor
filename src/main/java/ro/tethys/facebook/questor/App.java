package ro.tethys.facebook.questor;

import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.converter.OverrideConverter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import ro.tethys.facebook.questor.component.Component;
import ro.tethys.facebook.questor.component.akka.AkkaSystem;
import ro.tethys.facebook.questor.component.client.FacebookClient;
import ro.tethys.facebook.questor.component.http.HttpServerSupport;
import ro.tethys.facebook.questor.component.rest.Apidoc;
import ro.tethys.facebook.questor.component.rest.RestServer;
import ro.tethys.facebook.questor.properties.AppProperties;
import ro.tethys.facebook.questor.properties.FacebookClientProperties;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private static HttpServer httpServer;
	private static AppProperties properties;
	private static FacebookClientProperties facebookClientProperties;

	private static LinkedList<Component> components = new LinkedList<>();


	static {
		components.add(new AkkaSystem());
		components.add(new HttpServerSupport());
		components.add(new RestServer());
		components.add(new Apidoc());
		components.add(new FacebookClient());
	}

	private App() {
		//hide implicit constructor
	}

	/*
	 * Getters and Setters
	 */
	public static AppProperties getProperties() {
		return properties;
	}

	public static HttpServer getHttpServer() {
		return httpServer;
	}

	public static void setHttpServer(HttpServer httpServer) {
		App.httpServer = httpServer;
	}

	public static FacebookClientProperties getFacebookClientProperties() {
		return facebookClientProperties;
	}

	public static void main(String[] args) throws Exception {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		initializeProperties();

		initializeApplication();
		startApplication(args);
		addShutdownHook();
	}

	/*
	 *	Private methods
	 */
	private static void startApplication(String[] args) throws Exception {
		LOG.info("Starting application components");

		startComponents(args);

		overrideModelConverterSwagger();

		LOG.warn("Application started. Enjoy!");
	}

	private static void initializeApplication() {
		LOG.info("Initializing application components");
		for (Component component : components) {
			try {
				component.init();
			} catch (IllegalStateException e) {
				LOG.error("Problem starting: " + component.name() + ". " + e);
			}
		}
	}

	private static void startComponents(String[] args) throws IOException {
		for (Component component : components) {
			LOG.debug("Starting: " + component.name());
			component.start(args);
		}
		LOG.warn("Components started");
	}

	private static void initializeProperties() {
		properties = new AppProperties();
		properties.initializeProperties();
		facebookClientProperties = new FacebookClientProperties();
		facebookClientProperties.initializeProperties();
	}

	private static void overrideModelConverterSwagger() {
		String emptyJSON = "{\"id\": \"\"}";
		OverrideConverter converter = new OverrideConverter();

		converter.add(Timestamp.class.getName(), emptyJSON);
		converter.add(List.class.getName(), emptyJSON);
		converter.add(Collection.class.getName(), emptyJSON);
		converter.add(BigDecimal.class.getName(), emptyJSON);

		ModelConverters.addConverter(converter, true);
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			stopComponents(components);
			SLF4JBridgeHandler.uninstall();
		}));

	}

	private static void stopComponents(LinkedList<Component> components) {
		Iterator<Component> descendingIterator = components.descendingIterator();
		while (descendingIterator.hasNext()) {
			Component component = descendingIterator.next();
			LOG.debug("Stopping: " + component.name());
			component.stop();
		}
		LOG.warn("Components stopped");
	}
}
