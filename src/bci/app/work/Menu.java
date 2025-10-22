package bci.app.work;

import bci.core.LibraryManager;

/**
 * Menu class that extends pt.tecnico.uilib.menus.Menu.
 * Provides commands for interacting with the LibraryManager.
 */
public class Menu extends pt.tecnico.uilib.menus.Menu {
    /**
     * Constructs a Menu with commands related to work management.
     *
     * @param receiver the LibraryManager instance to operate on
     */
    public Menu(LibraryManager receiver) {
        super(Label.TITLE,
                new DoDisplayWork(receiver),
                new DoDisplayWorks(receiver),
                new DoPerformSearch(receiver),
                new DoDisplayWorksByCreator(receiver),
                new DoChangeWorkInventory(receiver)
        );
    }
}
