package bci.app.request;

import bci.core.LibraryManager;

/**
 * Requests menu.
 */
public class Menu extends pt.tecnico.uilib.menus.Menu {
    /**
     * Creates the requests menu bound to the given library manager.
     *
     * @param receiver the library manager used by the menu commands
     */
    public Menu(LibraryManager receiver) {
        super(Label.TITLE,
                new DoRequestWork(receiver), //
                new DoReturnWork(receiver) //
        );
    }
}
