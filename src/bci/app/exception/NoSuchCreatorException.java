package bci.app.exception;

import pt.tecnico.uilib.menus.CommandException;

/**
 * Exception thrown when a creator with the given identifier does not exist.
 */
public class NoSuchCreatorException extends CommandException {

    /** Serialization identifier. */
    @java.io.Serial
    private static final long serialVersionUID = 202507171003L;

    /**
     * Creates an exception for a missing creator.
     *
     * @param id the creator identifier that was not found
     */
    public NoSuchCreatorException(String id) {
        super(Message.noSuchCreator(id));
    }
}
