package bci.app.work;

import bci.app.exception.NoSuchWorkException;
import bci.core.LibraryManager;
import bci.core.exception.NoSuchWorkWithIdException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to display details of a specific work in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to retrieve and display information about a work.
 */
class DoDisplayWork extends Command<LibraryManager> {

    /**
     * Constructs the command to display a specific work.
     * Initializes the command with the label and input field for the work ID.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoDisplayWork(LibraryManager receiver) {
        super(Label.SHOW_WORK, receiver);
        addIntegerField("workId", Prompt.workId());
    }

    /**
     * Executes the command to display details of a specific work.
     * Reads the user-provided work ID, retrieves the work from the `LibraryManager`,
     * and displays the work's details. If the work does not exist, throws a
     * `NoSuchWorkException`.
     *
     * @throws CommandException if the work ID is invalid or the work does not exist.
     */
    @Override
    protected final void execute() throws CommandException {
        int workId = integerField("workId");
        try {
            _display.popup(_receiver.getLibrary().getWorkById(workId).toString());
        } catch (NoSuchWorkWithIdException e) {
            throw new NoSuchWorkException(workId);
        }
    }
}
