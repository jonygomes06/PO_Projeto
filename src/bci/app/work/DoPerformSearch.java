package bci.app.work;

import bci.core.LibraryManager;
import bci.core.work.Work;
import pt.tecnico.uilib.menus.Command;

/**
 * Command that performs a search for works using a user-provided term.
 * The matching works are retrieved from the receiver's library and displayed in
 * a popup as the string representation of each {@link Work}.
 *
 * @since 1.0
 */
class DoPerformSearch extends Command<LibraryManager> {
    /**
     * Creates the command and registers the input field for the search term.
     *
     * @param receiver the library manager that provides access to the library
     */
    DoPerformSearch(LibraryManager receiver) {
        super(Label.PERFORM_SEARCH, receiver);
        addStringField("searchTerm", Prompt.searchTerm());
    }

    /**
     * Executes the search: reads the search term, queries the library for matching
     * works, and displays the results in a popup.
     *
     * @see Command#execute()
     */
    @Override
    protected final void execute() {
        String searchTerm = stringField("searchTerm");
        _display.popup(_receiver.getLibrary()
                                .searchWorksByTerm(searchTerm)
                                .stream()
                                .map(Work::toString)
                                .toList());
    }
}
