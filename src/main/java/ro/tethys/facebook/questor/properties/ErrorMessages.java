package ro.tethys.facebook.questor.properties;

public enum ErrorMessages {
    WRONG_FIELD_FORMAT("An error occurred when trying to set a field!"),
    WRONG_PROPERTY_TYPE("Can not initialize property with name %s. Unsupported property type."),
    NULLABLE_FIELD_FILLED("Property %s was intentionally defined null in the config file"),
    ERROR_LOADING_FILE("Could not load config from missing resource=[%s]"),
    ERROR_READING_FILE("Could not read resource=[%s]"),
    ERROR_MISSING_REQUIRED_PROPERTY("Required property [%s] missing from the configuration file");

    private final String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
