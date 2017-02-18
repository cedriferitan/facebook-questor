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
    //
    @Mapping(name = "search.query")
    public String searchQuery;
    @Mapping(name = "query.limit")
    public String queryLimit;
    @Mapping(name = "likes.min")
    public Long likesMin;
    @Mapping(name = "likes.max")
    public Long likesMax;


    public AppProperties() {
        super();
    }

    @Override
    public String getPropertiesFileName() {
        return "application.properties";
    }
}
