package bci.core.exception;

import java.io.Serial;

public class InvalidRegistrationEntryException extends Exception {

    @Serial
    private static final long serialVersionUID = 202107171003L;

    /**
     * Invalid registration entry specification.
     */
    private final String _entrySpecification;

    public InvalidRegistrationEntryException(String entrySpecification) {
        _entrySpecification = entrySpecification;
    }

    public String getEntrySpecification() {
        return _entrySpecification;
    }
}
