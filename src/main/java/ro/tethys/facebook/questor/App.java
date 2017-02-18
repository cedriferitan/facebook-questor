package ro.tethys.facebook.questor;

import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import ro.tethys.facebook.questor.cron.JobScheduler;
import ro.tethys.facebook.questor.properties.AppProperties;
import ro.tethys.facebook.questor.services.CsvService;
import ro.tethys.facebook.questor.services.FacebookService;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static AppProperties properties;
    private static FacebookService facebookService;
    private static CsvService csvService;
    private static JobScheduler jobScheduler;

    private App() {
        //hide implicit constructor
    }

    /*
     * Getters and Setters
     */
    public static AppProperties getProperties() {
        return properties;
    }

    public static void main(String[] args) throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        initializeProperties();
        initializeApplication();
        startApplication();
    }

    /*
     *	Private methods
     */
    private static void startApplication() throws Exception {
        LOG.info("Starting application components");

        jobScheduler = new JobScheduler(new StdSchedulerFactory());
        jobScheduler.runScheduler();

        LOG.warn("Application started. Enjoy!");
    }

    private static void initializeApplication() throws Exception {
        LOG.info("Initializing application components");
        facebookService = new FacebookService(properties.fbAccessToken);
        csvService = new CsvService(properties.outputFile);
    }

    private static void initializeProperties() {
        properties = new AppProperties();
        properties.initializeProperties();
    }

    public static FacebookService getFacebookService() {
        return facebookService;
    }

    public static CsvService getCsvService() {
        return csvService;
    }
}
