package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;

/**
 * Command to advance the current date in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to update the system's current date by a specified number of days.
 */
class DoAdvanceDate extends Command<LibraryManager> {

    /**
     * Constructs the command to advance the current date.
     * Initializes the command with the label and input field for the number of days to advance.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoAdvanceDate(LibraryManager receiver) {
        super(Label.ADVANCE_DATE, receiver);
        addIntegerField("days", Prompt.daysToAdvance());
    }

    /**
     * Executes the command to advance the current date.
     * Reads the user-provided number of days, updates the library's current date,
     * and reflects the change in the system.
     */
    @Override
    protected final void execute() {
        int days = integerField("days");
        _receiver.getLibrary().advanceDate(days);
    }
}