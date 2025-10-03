package bci.app.work;

import bci.app.exception.NoSuchWorkException;
import bci.core.LibraryManager;
import bci.core.exception.NoSuchWorkWithIdException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to display a work.
 */
class DoDisplayWork extends Command<LibraryManager> {

    DoDisplayWork(LibraryManager receiver) {
        super(Label.SHOW_WORK, receiver);
        addIntegerField("workId", Prompt.workId());
    }

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
