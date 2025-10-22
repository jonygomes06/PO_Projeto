package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.UserIsActiveException;
import bci.core.exception.NoSuchUserWithIdException;
import bci.core.exception.UserNotSuspendedException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Creates the command and registers the required input fields.
 */
class DoPayFine extends Command<LibraryManager> {

    DoPayFine(LibraryManager receiver) {
        super(Label.PAY_FINE, receiver);
        addIntegerField("userId", Prompt.userId());
    }
    /**
     * Executes the pay fine action for the provided user ID.
     * <p>
     * Retrieves the {@code userId} from the form, invokes the library service to pay
     * the user's fine, and translates domain exceptions into UI exceptions.
     *
     * @throws CommandException if the user does not exist or is active
     */
    @Override
    protected final void execute() throws CommandException {
        int userId = integerField("userId");

        try {
            _receiver.getLibrary().payFine(userId);
        } catch (NoSuchUserWithIdException e) {
            throw new NoSuchUserException(userId);
        } catch (UserNotSuspendedException e) {
            throw new UserIsActiveException(userId);
        }
    }
}
