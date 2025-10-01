package bci.core.exception;

import java.io.Serial;

public class NoSuchUserWithIdException extends Exception {

    @Serial
    private static final long serialVersionUID = 202507171003L;

    public NoSuchUserWithIdException(int id) {
        super("O utente com o id " + id + " não existe.");
    }
}
