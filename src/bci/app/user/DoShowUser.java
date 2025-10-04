package bci.app.user;

import bci.app.exception.NoSuchUserException;
import bci.core.LibraryManager;
import bci.core.exception.NoSuchUserWithIdException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to show details of a specific user in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to retrieve and display user information.
 */
class DoShowUser extends Command<LibraryManager> {

    /**
     * Constructs the command to show a specific user's details.
     * Initializes the command with the label and input field for the user ID.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoShowUser(LibraryManager receiver) {
        super(Label.SHOW_USER, receiver);
        addIntegerField("userId", Prompt.userId());
    }

    /**
     * Executes the command to display a specific user's details.
     * Reads the user-provided ID, retrieves the user from the `LibraryManager`,
     * and displays the user's details. If the user does not exist, throws a
     * `NoSuchUserException`.
     *
     * @throws CommandException if the user ID is invalid or the user does not exist.
     */
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