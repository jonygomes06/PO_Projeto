package bci.app.user;

import bci.core.LibraryManager;
import bci.core.user.User;
import pt.tecnico.uilib.menus.Command;

/**
 * Command to show all users in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to retrieve and display a list of all registered users.
 */
class DoShowUsers extends Command<LibraryManager> {

    /**
     * Constructs the command to show all users.
     * Initializes the command with the label for displaying all users.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoShowUsers(LibraryManager receiver) {
        super(Label.SHOW_USERS, receiver);
    }

    /**
     * Executes the command to display all users.
     * Retrieves the list of all users from the `LibraryManager`, converts each user
     * to its string representation, and displays the list in a popup.
     */
    @Override
    protected final void execute() {
        _display.popup(_receiver.getLibrary()
                                .getUsers()
                                .stream()
                                .map(User::toString)
                                .toList());
    }
}