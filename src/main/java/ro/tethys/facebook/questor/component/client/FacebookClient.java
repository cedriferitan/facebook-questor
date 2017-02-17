package ro.tethys.facebook.questor.component.client;

import ro.tethys.facebook.questor.component.Component;

import java.io.IOException;

public class FacebookClient implements Component {
	@Override
	public boolean installed() {
		return false;
	}

	@Override
	public void init(String... args) {
		
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
}
