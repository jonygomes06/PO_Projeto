package bci.app.main;

import bci.core.LibraryManager;
import bci.core.Date;
import pt.tecnico.uilib.menus.Command;

/**
 * Command to display the current date in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to retrieve and display the system's current date.
 */
class DoDisplayDate extends Command<LibraryManager> {

    /**
     * Constructs the command to display the current date.
     * Initializes the command with the label for displaying the current date.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoDisplayDate(LibraryManager receiver) {
        super(Label.DISPLAY_DATE, receiver);
    }

    /**
     * Executes the command to display the current date.
     * Retrieves the current date from the `LibraryManager` and displays it in a popup.
     */
    @Override
    protected final void execute() {
        Date currentDate = _receiver.getLibrary().getCurrentDate();
        _display.popup(Message.currentDate(currentDate.getCurrentDate()));
    }
}