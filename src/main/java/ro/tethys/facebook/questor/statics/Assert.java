package ro.tethys.facebook.questor.statics;

import ro.tethys.facebook.questor.api.exception.BadRequestException;
import ro.tethys.facebook.questor.api.exception.ErrorMessage;

import javax.ws.rs.NotFoundException;

public final class Assert {

	private Assert() {
	}

	public static void assertNull(Object object, ErrorMessage message) throws BadRequestException {
		if (object != null) {
			throw new BadRequestException(message);
		}
	}

	public static void assertNotNull(Object object, ErrorMessage message) throws BadRequestException {
		if (object == null) {
			throw new BadRequestException(message);
		}
	}

	public static void assertNotEmptyStringParameter(String value, ErrorMessage message) throws BadRequestException {
		if (value == null || "".equals(value.trim())) {
			throw new BadRequestException(message);
		}
	}

	public static void assertTrue(Boolean result, ErrorMessage message) throws BadRequestException {
		if (result == null || !result) {
			throw new BadRequestException(message);
		}
	}

	public static void assertEquals(Object expected, Object actual, ErrorMessage message)
			throws BadRequestException {
		if (expected != null) {
			assertNotNull(actual, message);
			if (!expected.equals(actual)) {
				throw new BadRequestException(message);
			}
		} else {
			assertNull(actual, message);
		}
	}

	public static void assertFoundEntity(Object object) throws NotFoundException {
		if (object == null) {
			throw new NotFoundException();
		}
	}
}
