package ro.tethys.facebook.questor.jobs;

import com.restfb.Connection;
import com.restfb.types.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.services.CsvService;
import ro.tethys.facebook.questor.services.FacebookService;

import java.util.List;
import java.util.Random;


public class SearchFacebookThread implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SearchFacebookThread.class);
    private static String afterCursor;
    private FacebookService fbService = App.getFacebookService();
    private CsvService csvService = App.getCsvService();
    private Random random = new Random();

    @Override
    public void run() {
        getInformation();
        while (afterCursor != null) {
            int randomInterval = getRandomInterval();
            LOG.info("Next cursor: {}", afterCursor);
            LOG.info("Waiting for {} millis", randomInterval);
            try {
                Thread.sleep(randomInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getInformation();
        }
        csvService.getPw().close();
        LOG.info("The cursor is {}", afterCursor);
    }

    private void getInformation() {
        Connection<Page> facebookPagesDetails = fbService.getFacebookPagesDetails(afterCursor);

        afterCursor = facebookPagesDetails.getAfterCursor();
        List<Page> pages = fbService.filterByFanCount(facebookPagesDetails.getData());
        csvService.writeToFile(pages);
        LOG.info("Wrote {} entries", pages.size());
        csvService.flushStringBuilder();
    }

    private int getRandomInterval() {
//        return random.nextInt(40000) + 20000;
        return random.nextInt(1000);
    }
}
