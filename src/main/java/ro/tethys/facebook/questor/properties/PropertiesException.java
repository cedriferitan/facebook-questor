package ro.tethys.facebook.questor.properties;

public class PropertiesException extends RuntimeException {
	public PropertiesException() {
		super();
	}

	public PropertiesException(String message) {
		super(message);
	}

	public PropertiesException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertiesException(Throwable cause) {
		super(cause);
	}

	protected PropertiesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
