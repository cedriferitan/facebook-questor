package ro.tethys.facebook.questor.api.exception;

public class ErrorMessage {

	/**
	 * Generic
	 */
	public static final ErrorMessage MISSING_PARAMETER =
			new ErrorMessage(100, "%s is missing.");

	public static final ErrorMessage WRONG_TYPE =
			new ErrorMessage(101, "Invalid type");

	private final Integer errorCode;
	private final String errorMessage;

	public ErrorMessage(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public ErrorMessage(String errorMessage) {
		this.errorCode = 0;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "ErrorMessage{" +
				"errorCode=" + errorCode +
				", errorMessage='" + errorMessage + '\'' +
				'}';
	}

}
