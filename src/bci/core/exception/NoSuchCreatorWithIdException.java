package bci.core.exception;

import java.io.Serial;

public class NoSuchCreatorWithIdException extends Exception {

    @Serial
    private static final long serialVersionUID = 215119372293L;

    public NoSuchCreatorWithIdException(String id) {
        super("O criador com o id " + id + " não existe.");
    }
}
