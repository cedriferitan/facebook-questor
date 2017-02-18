package ro.tethys.facebook.questor.properties;

/**
 * Verify if mandatory properties are not missing from config file.
 * If these properties are missing the application should not run.
 */
public class AppProperties extends GenericProperties {

    @Mapping(name = "fb.access.token")
    public String fbAccessToken;
    @Mapping(name = "cron.expression")
    public String cronExpression;
    @Mapping(name = "output.file")
    public String outputFile;

    public AppProperties() {
        super();
    }

    @Override
    public String getPropertiesFileName() {
        return "application.properties";
    }
}
