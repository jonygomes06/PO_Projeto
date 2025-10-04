package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to open the users menu in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to navigate to the users' menu.
 */
class DoOpenMenuUsers extends Command<LibraryManager> {

    /**
     * Constructs the command to open the users' menu.
     * Initializes the command with the label for opening the users' menu.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoOpenMenuUsers(LibraryManager receiver) {
        super(Label.OPEN_MENU_USERS, receiver);
    }

    /**
     * Executes the command to open the users' menu.
     * Creates and opens the users menu, allowing the user to interact with user-related operations.
     *
     * @throws CommandException if an error occurs while opening the menu.
     */
    @Override
    protected final void execute() throws CommandException {
        (new bci.app.user.Menu(_receiver)).open();
    }
}