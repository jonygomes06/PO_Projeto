package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to open the works menu in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to navigate to the works menu.
 */
class DoOpenMenuWorks extends Command<LibraryManager> {

    /**
     * Constructs the command to open the works menu.
     * Initializes the command with the label for opening the works menu.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoOpenMenuWorks(LibraryManager receiver) {
        super(Label.OPEN_MENU_WORKS, receiver);
    }

    /**
     * Executes the command to open the works menu.
     * Creates and opens the works menu, allowing the user to interact with work-related operations.
     *
     * @throws CommandException if an error occurs while opening the menu.
     */
    @Override
    protected final void execute() throws CommandException {
        (new bci.app.work.Menu(_receiver)).open();
    }
}