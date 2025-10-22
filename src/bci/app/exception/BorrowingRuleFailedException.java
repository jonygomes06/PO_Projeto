package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * Exception thrown when a borrowing operation violates a specific rule.
 */
public class BorrowingRuleFailedException extends CommandException {
    /** Serialization identifier. */
    @java.io.Serial
    private static final long serialVersionUID = 202507171003L;

    /**
     * Creates a new exception for a borrowing rule failure.
     *
     * @param idUser the identifier of the user
     * @param idWork the identifier of the work
     * @param idRule the identifier of the violated rule
     */
    public BorrowingRuleFailedException(int idUser, int idWork, int idRule) {
        super(Message.borrowingRuleFailed(idUser, idWork, idRule));
    }
}
