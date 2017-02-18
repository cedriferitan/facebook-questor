package ro.tethys.facebook.questor.cron.job;

import com.restfb.Connection;
import com.restfb.types.Page;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.services.CsvService;
import ro.tethys.facebook.questor.services.FacebookService;

import java.util.List;


public class SearchFacebookQuery implements Job {
    public static final Logger LOG = LoggerFactory.getLogger(SearchFacebookQuery.class);
    private static String afterCursor;
    private FacebookService fbService = App.getFacebookService();
    private CsvService csvService = App.getCsvService();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Connection<Page> facebookPagesDetails = fbService.getFacebookPagesDetails(afterCursor);
        LOG.info("Next cursor: {}", afterCursor);
        afterCursor = facebookPagesDetails.getAfterCursor();
        List<Page> pages = fbService.filterByFanCount(facebookPagesDetails.getData());
        csvService.writeToFile(pages);
        LOG.info("Wrote {} entries", pages.size());
        csvService.flush();
    }
}
