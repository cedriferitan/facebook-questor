package ro.tethys.facebook.questor.cron.job;

import com.restfb.types.Page;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import ro.tethys.facebook.questor.App;

import java.util.List;

public class SearchFacebookQuery implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Page> facebookPagesDetails = App.getFacebookService().getFacebookPagesDetails();
        App.getCsvService().writeToFile(facebookPagesDetails);
    }
}
