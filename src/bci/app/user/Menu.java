package bci.app.user;

import bci.core.LibraryManager;

/**
 * Menu for user-related operations.
 * <p>
 * Provides commands to:
 * <ul>
 *   <li>Register a new user</li>
 *   <li>Show a specific user</li>
 *   <li>List all users</li>
 *   <li>Show user notifications</li>
 *   <li>Pay user fines</li>
 * </ul>
 */
public class Menu extends pt.tecnico.uilib.menus.Menu {

    /**
     * Creates the user menu and registers all user-related commands.
     *
     * @param receiver the library manager used as the command receiver
     */
    public Menu(LibraryManager receiver) {
        super(Label.TITLE,
                new DoRegisterUser(receiver),
                new DoShowUser(receiver),
                new DoShowUsers(receiver),
                new DoShowUserNotifications(receiver),
                new DoPayFine(receiver),
                new DoShowUsersWithEqOrMoreFines(receiver),
                new DoShowUserWithHighestValue(receiver)
        );
    }
}
