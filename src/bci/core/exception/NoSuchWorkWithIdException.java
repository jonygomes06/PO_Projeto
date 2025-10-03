package bci.core.exception;

import java.io.Serial;

public class NoSuchWorkWithIdException extends Exception {

    @Serial
    private static final long serialVersionUID = 211129672203L;

    public NoSuchWorkWithIdException(int id) {
        super("A obra com o id " + id + " não existe.");
    }
}
