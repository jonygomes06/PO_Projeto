package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.UserIsActiveException;
import bci.core.exception.NoSuchUserWithIdException;
import bci.core.exception.UserNotSuspendedException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.5. Settle a fine.
 */
class DoPayFine extends Command<LibraryManager> {

    DoPayFine(LibraryManager receiver) {
        super(Label.PAY_FINE, receiver);
        addIntegerField("userId", bci.app.user.Prompt.userId());
    }

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
