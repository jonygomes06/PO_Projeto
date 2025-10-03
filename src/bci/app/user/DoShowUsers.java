package bci.app.user;

import bci.core.LibraryManager;
import bci.core.user.User;
import pt.tecnico.uilib.menus.Command;

import java.util.Collection;

/**
 * 4.2.4. Show all users.
 */
class DoShowUsers extends Command<LibraryManager> {

    DoShowUsers(LibraryManager receiver) {
        super(Label.SHOW_USERS, receiver);
    }

    @Override
    protected final void execute() {
        _display.popup(_receiver.getLibrary()
                                .getUsers()
                                .stream()
                                .map(User::toString)
                                .toList());
    }
}
