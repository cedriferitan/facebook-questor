package ro.tethys.facebook.questor.services;

import com.google.common.collect.ImmutableList;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Page;

import java.util.List;
import java.util.stream.Collectors;

import static com.restfb.Parameter.with;
import static com.restfb.Version.VERSION_2_8;

public class FacebookService {
    private static final int LIMIT = 1000;
    private static final String SEARCH_Q = "musician/band";
    private static final String PAGE = "page";
    private static final String FIELDS = "fan_count, location, emails";
    private final FacebookClient fbClient;
    private String afterCursor;

    public FacebookService(String accessToken) {
        fbClient = new DefaultFacebookClient(accessToken, VERSION_2_8);
    }

    private List<Parameter> queryParameters(String afterCursor) {
        if (afterCursor != null) {
            queryParameters().add(with("after", afterCursor));
        }
        return queryParameters();
    }

    private List<Parameter> queryParameters() {
        return ImmutableList.of(with("q", SEARCH_Q),
                with("type", PAGE),
                with("limit", LIMIT),
                with("fields", FIELDS));
    }

    public List<Page> getFacebookPagesDetails() {
        Connection<Page> search = fbClient.fetchConnection("search", Page.class,
                queryParameters(afterCursor).toArray(new Parameter[]{}));
        afterCursor = search.getAfterCursor();

        return filterByFanCount(search.getData());
    }

    private List<Page> filterByFanCount(List<Page> list) {
        return list.stream()
                .filter(p -> p.getFanCount() > 1000
                        && p.getFanCount() < 15000)
                .collect(Collectors.toList());
    }
}