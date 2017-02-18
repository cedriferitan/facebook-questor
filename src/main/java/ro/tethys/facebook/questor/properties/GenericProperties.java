package ro.tethys.facebook.questor.properties;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class GenericProperties {
	private static final Logger LOG = LoggerFactory.getLogger(GenericProperties.class);

	private List nullableProperties;
	protected Properties properties;
	private String propertiesFileName;


	GenericProperties() {
		super();
	}

	public Properties getProperties() {
		return properties;
	}

	private List getNullableProperties() {
		return nullableProperties;
	}

	public String getPropertiesFileName() {
		return propertiesFileName;
	}

	public void initializeProperties() {
		properties = new Properties();
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(getPropertiesFileName());
		try {
			if (stream == null) {
				throw new PropertiesException(
						String.format(ErrorMessages.ERROR_LOADING_FILE.getMessage(), getPropertiesFileName()));
			}
			properties.load(stream);
		} catch (IOException e) {
			throw new PropertiesException(
					String.format(ErrorMessages.ERROR_READING_FILE.getMessage(), getPropertiesFileName()), e);
		}

		for (Field field : this.getClass().getDeclaredFields()) {
			configureField(field);
		}
	}

	private void configureField(Field field) {
		if (!field.isAnnotationPresent(Mapping.class)) {
			return;
		}

		Mapping mapping = field.getAnnotation(Mapping.class);
		String name = mapping.name();

		String value = retrieveValue(name);

		validateRequiredField(field, name, value);

		if (field.getType().isAssignableFrom(String.class)) {
			validateStringField(field, name, value);
		} else if (field.getType().isAssignableFrom(Integer.class)) {
			validateIntegerField(field, value);
		} else if (field.getType().isAssignableFrom(Long.class)) {
			validateLongField(field, value);
		} else if (field.getType().isAssignableFrom(Boolean.class)) {
			validateBooleanField(field, value);
		} else {
			LOG.error(String.format(ErrorMessages.WRONG_PROPERTY_TYPE.getMessage(), name));
			throw new PropertiesException(String.format(ErrorMessages.WRONG_PROPERTY_TYPE.getMessage(), name));
		}
	}

	private String retrieveValue(String name) {
		String value = isEmpty(properties.getProperty(name)) ? null : properties.getProperty(name).trim();

		String systemValue = System.getProperty(name, null);
		if (!Strings.isNullOrEmpty(systemValue)) {
			value = systemValue.trim();
		}
		return value;
	}

	/**
	 * Each type of Properties accepts a list of fields that can be omitted in properties file. This method
	 * verifies if the one that is empty should contain a value or not.
	 *
	 * @param field the field to be filled
	 * @param name  the name of the field from the properties file
	 * @param value the value to set the field
	 */
	private void validateRequiredField(Field field, String name, String value) {
		if (isEmpty(value) && (getNullableProperties() == null || !getNullableProperties().contains(field.getName()))) {
			throw new PropertiesException(String.format(ErrorMessages.ERROR_MISSING_REQUIRED_PROPERTY.getMessage(), name));
		}
	}

	/**
	 * Validates if a required String field received a value of the expected type.
	 * If the field was empty, it will not accept a "null" value as String.
	 *
	 * @param field the field to be filled
	 * @param name  the name of the field from the properties file
	 * @param value the value to set the field
	 */
	private void validateStringField(Field field, String name, String value) {
		LOG.debug("Load value = {} for = {}", value, name);
		try {
			if (!"null".equals(value)) {
				field.set(this, value);
			} else {
				LOG.error(String.format(ErrorMessages.NULLABLE_FIELD_FILLED.getMessage(), name));
			}
		} catch (IllegalAccessException e) {
			LOG.error(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
			throw new PropertiesException(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
		}
	}

	/**
	 * Validates if a required Integer field received a value of the expected type.
	 *
	 * @param field the field to be filled
	 * @param value the value to set the field
	 */
	private void validateIntegerField(Field field, String value) {
		try {
			field.set(this, Integer.valueOf(value));
		} catch (IllegalAccessException e) {
			LOG.error(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
			throw new PropertiesException(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
		}
	}

	/**
	 * Validates if a required Long field received a value of the expected type.
	 *
	 * @param field the field to be filled
	 * @param value the value to set the field
	 */
	private void validateLongField(Field field, String value) {
		try {
			field.set(this, Long.valueOf(value));
		} catch (IllegalAccessException e) {
			LOG.error(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
			throw new PropertiesException(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
		}
	}

	/**
	 * Validates if a required Boolean field received a value of the expected type.
	 *
	 * @param field the field to be filled
	 * @param value the value to set the field
	 */
	private void validateBooleanField(Field field, String value) {
		try {
			field.set(this, Boolean.valueOf(value));
		} catch (IllegalAccessException e) {
			LOG.error(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
			throw new PropertiesException(ErrorMessages.WRONG_FIELD_FORMAT.getMessage(), e);
		}
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Mapping {
		String name();
	}
}
