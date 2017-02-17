package ro.tethys.facebook.questor.properties;

/**
 * Verify if mandatory properties are not missing from config file.
 * If these properties are missing the application should not run.
 */
public class AppProperties extends GenericProperties {

	@Mapping(name = "api.version")
	public String apiVersion;
	@Mapping(name = "api.address")
	public String apiAddress;
	@Mapping(name = "server.address")
	public String serverAddress;
	@Mapping(name = "server.port")
	public Integer serverPort;

	public AppProperties() {
		super();
	}

	@Override
	public String getPropertiesFileName() {
		return "application.properties";
	}
}
