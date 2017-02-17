package ro.tethys.facebook.questor.component.akka;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ro.tethys.facebook.questor.component.Component;

public class AkkaSystem implements Component {
	private ActorSystem actorSystem;

	@Override
	public boolean installed() {
		return true;
	}

	@Override
	public void init(String... args) {
		Config configuration = ConfigFactory.load("akka.config");

		actorSystem = ActorSystem.create("cams-action-provider-internal", configuration);
	}

	@Override
	public void start(String... args) {
		//nothing
	}

	@Override
	public void stop() {
		actorSystem.shutdown();
		actorSystem.awaitTermination();
	}

	@Override
	public String name() {
		return "Akka System";
	}
}
