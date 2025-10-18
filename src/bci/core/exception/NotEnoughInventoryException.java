package bci.core.exception;

import java.io.Serial;

public class NotEnoughInventoryException extends Exception {

    @Serial
    private static final long serialVersionUID = 222222222228L;

    public NotEnoughInventoryException(int id, int amountRequested, int amountAvailable) {
        super(String.format("Not enough inventory for work with id %d: requested %d, available %d",
                id,  amountRequested, amountAvailable));
    }
}
