package bci.app.work;

import bci.core.LibraryManager;
import bci.core.work.Work;
import pt.tecnico.uilib.menus.Command;

/**
 * Perform search according to miscellaneous criteria.
 */
class DoPerformSearch extends Command<LibraryManager> {

    DoPerformSearch(LibraryManager receiver) {
        super(Label.PERFORM_SEARCH, receiver);
        addStringField("searchTerm", Prompt.searchTerm());
    }

    /**
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
