package bci.core.exception;

import java.io.Serial;

public class UserNotSuspendedException extends Exception {

    @Serial
    private static final long serialVersionUID = 201111911119L;

    public UserNotSuspendedException(int id) {
        super("O utente com o id " + id + " está ativo. Sem multas para pagar.");
    }
}
