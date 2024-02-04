package io.portfel.exception;

public class NotFoundException extends BaseRuntimeException {

    public static final String TEMPLATE = "Unable to find %s with id %s";

    public NotFoundException(String message) {
        super( message );
    }

    public NotFoundException(String message, Throwable cause) {
        super( message, cause );
    }

    public NotFoundException(Class<?> clazz, Object id) {
        super( String.format( TEMPLATE,  clazz.getSimpleName(), id.toString()) );
    }
}
