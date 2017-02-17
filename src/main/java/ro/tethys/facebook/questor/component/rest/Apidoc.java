package ro.tethys.facebook.questor.component.rest;

import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.converter.OverrideConverter;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.joda.time.LocalDateTime;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.component.Component;

public class Apidoc implements Component {

	private static void overrideModelConverterSwagger() {
		String localDateTimeJson = "{" +
				"\"id\": \"LocalDateTime\"," +
				"\"properties\": {" +
				"\"value\": {" +
				"\"required\": true," +
				"\"description\": \"Date in ISO-8601 format.\"," +
				"\"notes\": \"Ex: 2014-09-10T10:00:00Z\"," +
				"\"type\": \"string\"" +
				"}" +
				"}" +
				"}";
		OverrideConverter converter = new OverrideConverter();
		converter.add(LocalDateTime.class.getName(), localDateTimeJson);
		ModelConverters.addConverter(converter, true);
	}

	public boolean installed() {
		return true;
	}

	@Override
	public void init(String... args) {
		BeanConfig config = new BeanConfig();
		config.setResourcePackage("ro.tethys.facebook.questor.api");
		config.setVersion(App.getProperties().apiVersion);
		config.setBasePath(App.getProperties().apiAddress);
		config.setScan(true);
		config.setApiReader(DefaultJaxrsApiReader.class.getCanonicalName());

		ScannerFactory.setScanner(config);

		overrideModelConverterSwagger();
	}

	public void start(String... args) {
		CLStaticHttpHandler cshh = new CLStaticHttpHandler(getClass().getClassLoader(), "swagger/");
		App.getHttpServer().getServerConfiguration().addHttpHandler(cshh, "/");
	}

	public void stop() {
		//nothing to stop
	}

	public String name() {
		return "Apidoc website";
	}

}
