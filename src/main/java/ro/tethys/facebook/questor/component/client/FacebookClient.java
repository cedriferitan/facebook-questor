package ro.tethys.facebook.questor.component.client;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.scope.ScopeBuilder;
import com.restfb.scope.UserDataPermissions;
import com.restfb.types.Page;
import ro.tethys.facebook.questor.App;
import ro.tethys.facebook.questor.component.Component;
import ro.tethys.facebook.questor.properties.FacebookClientProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.restfb.Version.VERSION_2_8;

public class FacebookClient implements Component {

	List<Page> all = new ArrayList<>();
	FacebookClientProperties fbClientProperties = App.getFacebookClientProperties();

	@Override
	public boolean installed() {
		return false;
	}

	@Override
	public void init(String... args) {
		//TODO: replace hardcoded with app.properties
		ScopeBuilder scopeBuilder = new ScopeBuilder();
		scopeBuilder.addPermission(UserDataPermissions.USER_LIKES);
		scopeBuilder.addPermission(UserDataPermissions.USER_ABOUT_ME);

		AccessToken accessToken = new DefaultFacebookClient(VERSION_2_8).obtainAppAccessToken("159846021185756", "05d0c954067846697fd71cdc207000a4");

		com.restfb.FacebookClient fbClient = new DefaultFacebookClient("EAACEdEose0cBAPZCGTrLqZCYpmgNfdgqninAW8YA6A2hnNZAZBXIjpzBvh6l3Hr0ZCLzwfXO0dzhBmCZAFcYs35saq3EX4hi1j37hv3pTfLulVpDqhOyGIHu6yW4Rq14RLU8A5nX5NZAAfFwbYnmlFSiLnJsF1FEYJwXNh4nvBhBjWh60BQ5jGLG2oU57ZCftxUZD",
				VERSION_2_8);

		Connection<Page> first = fbClient.fetchConnection("search", Page.class,
				Parameter.with("q", "musician/band"),
				Parameter.with("type", "page"),
				Parameter.with("limit", "1000"),
				Parameter.with("fields", "fan_count, location, emails"));

		List<Page> data = first.getData();
		List<Page> collect = data.stream()
				.filter(p -> p.getFanCount() > 1000
						&& p.getFanCount() < 15000)
				.collect(Collectors.toList());

		all.addAll(collect);

		Connection<Page> second = fbClient.fetchConnection("search", Page.class,
				Parameter.with("q", "musician/band"),
				Parameter.with("type", "page"),
				Parameter.with("fields", "fan_count, location, emails"),
				Parameter.with("after", first.getAfterCursor()));

		data = second.getData();
		all.addAll(data.stream()
				.filter(p -> p.getFanCount() > 1000
						&& p.getFanCount() < 15000)
				.collect(Collectors.toList()));

		Connection<Page> third = fbClient.fetchConnection("search", Page.class,
				Parameter.with("q", "musician/band"),
				Parameter.with("type", "page"),
				Parameter.with("fields", "fan_count, location, emails"),
				Parameter.with("after", second.getAfterCursor()));

		data = third.getData();
		all.addAll(data.stream()
				.filter(p -> p.getFanCount() > 1000
						&& p.getFanCount() < 15000)
				.collect(Collectors.toList()));

		System.out.println("");
//		while (publicSearch.hasNext()) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			all.addAll(fetchConnection(fbClient, publicSearch.getNextPageUrl(), publicSearch.getAfterCursor()).getData().stream().filter(p -> p.getFanCount() > 1000
//					&& p.getFanCount() < 15000)
//					.collect(Collectors.toList()));
//			System.out.println("ran while...");
//		}
//		Connection<User> targetedSearch =
//				fbClient.fetchConnection("me/home", User.class,
//						Parameter.with("q", "Mark"), Parameter.with("type", "user"));
//
//		System.out.println("Posts on my wall by friends named Mark: " + targetedSearch.getData().size());

		System.out.println(accessToken);

	}

	public Connection<Page> fetchConnection(com.restfb.FacebookClient fb, String what, String after) {
		return fb.fetchConnection(what, Page.class,
				Parameter.with("q", "musician/band"),
				Parameter.with("type", "page"),
				Parameter.with("fields", "fan_count, location, emails"),
				Parameter.with("after", after));
	}

	@Override
	public void start(String... args) throws IOException {

	}

	@Override
	public void stop() {

	}

	@Override
	public String name() {
		return null;
	}

	private void generatePerminsions() {
		ScopeBuilder scopeBuilder = new ScopeBuilder();
		scopeBuilder.addPermission(UserDataPermissions.USER_LIKES);
		scopeBuilder.addPermission(UserDataPermissions.USER_POSTS);
	}
}
