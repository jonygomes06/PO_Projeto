package bci.app.main;

import bci.core.LibraryManager;

/**
 * Main menu of the application.
 * Provides navigation to file operations, date management, and sub-menus
 * for users, works, and requests.
 */
public class Menu extends pt.tecnico.uilib.menus.Menu {

    /**
     * Creates the main menu bound to the given library manager.
     *
     * @param receiver the library manager that handles application operations
     */
    public Menu(LibraryManager receiver) {
        super(Label.TITLE, //
                new DoOpenFile(receiver), //
                new DoSaveFile(receiver), //
                new DoDisplayDate(receiver), //
                new DoAdvanceDate(receiver), //
                new DoOpenMenuUsers(receiver), //
                new DoOpenMenuWorks(receiver), //
                new DoOpenMenuRequests(receiver) //
        );
    }
}
