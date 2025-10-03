package bci.app.work;

import bci.core.LibraryManager;
import bci.core.user.User;
import bci.core.work.Work;
import pt.tecnico.uilib.menus.Command;

/**
 * Command to display all works.
 */
class DoDisplayWorks extends Command<LibraryManager> {

    DoDisplayWorks(LibraryManager receiver) {
        super(Label.SHOW_WORKS, receiver);
    }

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
