package ro.tethys.facebook.questor.component;

import java.io.IOException;

public interface Component {
    /**
     * This verification should return false if certain configurations which can be done using the
     * install method have not been executed
     *
     * @return If the component's installation has been executed
     */
    boolean installed();

	/**
     * Initializes the component.
     *
     * @param args
     */
    void init(String... args);

    void start(String... args) throws IOException;

    void stop();

    String name();
}
