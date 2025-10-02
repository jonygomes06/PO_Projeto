package bci.core.exception;

import java.io.Serial;

public class InvalidArgumentsException extends Exception {

    @Serial
    private static final long serialVersionUID = 202107171003L;

    /**
     * Invalid registration argument specification.
     */
    private final String _argSpecification;

    public InvalidArgumentsException(String argSpecification) {
        _argSpecification = argSpecification;
    }

    public String getArgSpecification() {
        return _argSpecification;
    }
}
