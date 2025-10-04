package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchCreatorException;
import bci.core.exception.NoSuchCreatorWithIdException;
import bci.core.work.Work;
import pt.tecnico.uilib.menus.Command;

/**
 * Command to display all works by a specific creator in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to retrieve and display works associated with a specific creator.
 */
class DoDisplayWorksByCreator extends Command<LibraryManager> {

    /**
     * Constructs the command to display works by a specific creator.
     * Initializes the command with the label and input field for the creator ID.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoDisplayWorksByCreator(LibraryManager receiver) {
        super(Label.SHOW_WORKS_BY_CREATOR, receiver);
        addStringField("creatorId", Prompt.creatorId());
    }

    /**
     * Executes the command to display works by a specific creator.
     * Reads the user-provided creator ID, retrieves the creator from the `LibraryManager`,
     * and displays the works associated with the creator. If the creator does not exist,
     * throws a `NoSuchCreatorException`.
     *
     * @throws NoSuchCreatorException if the creator ID is invalid or the creator does not exist.
     */
    @Override
    protected final void execute() throws NoSuchCreatorException {
        String creatorId = stringField("creatorId");
        try {
            _display.popup(_receiver.getLibrary()
                                    .getCreatorByName(creatorId)
                                    .getWorks()
                                    .stream()
                                    .map(Work::toString)
                                    .toList());
        } catch (NoSuchCreatorWithIdException e) {
            throw new NoSuchCreatorException(creatorId);
        }
    }
}