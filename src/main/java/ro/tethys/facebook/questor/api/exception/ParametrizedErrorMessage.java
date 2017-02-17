package ro.tethys.facebook.questor.api.exception;

public class ParametrizedErrorMessage extends ErrorMessage {

    private String parameterName;

    public ParametrizedErrorMessage(ErrorMessage message, String parameterName, Object... args) {
        super(message.getErrorCode(), String.format(message.getErrorMessage(), joinArgs(parameterName, args)));
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String toString() {
        return "ParametrizedErrorMessage{" +
                "parameterName='" + parameterName + '\'' +
                '}';
    }

    private static Object[] joinArgs(Object arg, Object[] args) {
        if (args == null || args.length == 0) {
            return new Object[]{arg};
        }

        Object[] result = new Object[args.length + 1];
        result[0] = arg;
        System.arraycopy(args, 0, result, 1, args.length);

        return result;
    }

}
