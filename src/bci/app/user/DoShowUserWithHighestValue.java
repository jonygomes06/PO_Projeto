package bci.app.user;

import bci.core.LibraryManager;
import bci.core.user.User;
import pt.tecnico.uilib.menus.Command;

import java.util.Collection;

class DoShowUserWithHighestValue extends Command<LibraryManager> {

    DoShowUserWithHighestValue(LibraryManager receiver) {
        super("Utente com maior valor", receiver);
    }

    @Override
    protected final void execute() {
        try {
            User highestValueUser = _receiver.getLibrary().getUserWithHighestValue();
            _display.popup(String.format("%d - %s - %d", highestValueUser.getId(), highestValueUser.getName(), highestValueUser.getTotalValueOfRequests()));
        } catch (IllegalArgumentException e) {
            _display.popup("Sem obras");
        }
    }
}