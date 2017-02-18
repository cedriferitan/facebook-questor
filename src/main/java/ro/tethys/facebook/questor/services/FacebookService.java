package ro.tethys.facebook.questor.services;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.properties.AppProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.restfb.Parameter.with;
import static com.restfb.Version.VERSION_2_8;

public class FacebookService {
    private static final String PAGE = "page";
    private static final String FIELDS = "fan_count, location, emails";
    private final FacebookClient fbClient;
    private List<Parameter> parameterList;

    private AppProperties props = App.getProperties();

    public FacebookService(String accessToken) {
        fbClient = new DefaultFacebookClient(accessToken, VERSION_2_8);
        buildQueryParameters();
    }

    private void buildQueryParameters() {
        parameterList = new ArrayList<>();
        parameterList.add(with("q", props.searchQuery));
        parameterList.add(with("type", PAGE));
        parameterList.add(with("limit", props.queryLimit));
        parameterList.add(with("fields", FIELDS));
    }

    public Connection<Page> getFacebookPagesDetails(String afterCursor) {
        return fbClient.fetchConnection("search", Page.class, queryParametersAsArray(afterCursor));
    }

    private Parameter[] queryParametersAsArray(String afterCursor) {
        if (afterCursor != null) {
            parameterList.add(with("after", afterCursor));
        }
        return parameterList.toArray(new Parameter[parameterList.size()]);
    }

    public List<Page> filterByFanCount(List<Page> list) {
        return list.stream()
                .filter(p -> p.getFanCount() > props.likesMin
                        && p.getFanCount() < props.likesMax)
                .collect(Collectors.toList());
    }
}