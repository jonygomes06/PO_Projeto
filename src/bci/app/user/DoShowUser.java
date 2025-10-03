package bci.app.user;

import bci.app.exception.NoSuchUserException;
import bci.core.LibraryManager;
import bci.core.exception.NoSuchUserWithIdException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.2. Show specific user.
 */
class DoShowUser extends Command<LibraryManager> {

    DoShowUser(LibraryManager receiver) {
        super(Label.SHOW_USER, receiver);
        addIntegerField("userId", Prompt.userId());
    }

    @Override
    protected final void execute() throws CommandException {
        int userId = integerField("userId");
        try {
            _display.popup(_receiver.getLibrary().getUserById(userId).toString());
        } catch (NoSuchUserWithIdException e) {
            throw new NoSuchUserException(userId);
        }
    }
}
