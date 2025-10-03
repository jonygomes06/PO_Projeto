package bci.app.main;

import bci.core.LibraryManager;
import bci.core.date.Date;
import pt.tecnico.uilib.menus.Command;

/**
 * 4.1.2. Display the current date.
 */
class DoDisplayDate extends Command<LibraryManager> {

    DoDisplayDate(LibraryManager receiver) {
        super(Label.DISPLAY_DATE, receiver);
    }

    @Override
    protected final void execute() {
        Date currentDate = _receiver.getLibrary().getCurrentDate();
        _display.popup(Message.currentDate(currentDate.getCurrentDate()));
    }
}
