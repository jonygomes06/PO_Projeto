package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to open the requests menu in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to navigate to the requests' menu.
 */
class DoOpenMenuRequests extends Command<LibraryManager> {

    /**
     * Constructs the command to open the requests' menu.
     * Initializes the command with the label for opening the requests' menu.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoOpenMenuRequests(LibraryManager receiver) {
        super(Label.OPEN_MENU_REQUESTS, receiver);
    }

    /**
     * Executes the command to open the requests' menu.
     * Creates and opens the requests menu, allowing the user to interact with request-related operations.
     *
     * @throws CommandException if an error occurs while opening the menu.
     */
    @Override
    protected final void execute() throws CommandException {
        (new bci.app.request.Menu(_receiver)).open();
    }
}