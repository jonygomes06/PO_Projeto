package bci.app.work;

import bci.core.LibraryManager;
import bci.core.work.Work;
import pt.tecnico.uilib.menus.Command;

/**
* Command to display all works in the library system.
* This class is part of the user interface layer and interacts with the `LibraryManager`
* to retrieve and display a list of all works.
*/
class DoDisplayWorks extends Command<LibraryManager> {

    /**
    * Constructs the command to display all works.
    * Initializes the command with the label for displaying all works.
    *
    * @param receiver the `LibraryManager` instance that handles the library operations.
    */
    DoDisplayWorks(LibraryManager receiver) {
        super(Label.SHOW_WORKS, receiver);
    }

    /**
    * Executes the command to display all works.
    * Retrieves the collection of works from the `LibraryManager`, converts each work
    * to its string representation, and displays the list in a popup.
    */
    @Override
    protected final void execute() {
        _display.popup(_receiver.getLibrary()
                                .getWorks()
                                .values()
                                .stream()
                                .map(Work::toString)
                                .toList());
    }
}