package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.core.user.Notification;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to show notifications of a specific user.
 * Invokes the library to fetch notifications for the provided user id.
 */
class DoShowUserNotifications extends Command<LibraryManager> {
    /**
     * Creates the command and registers input fields.
     *
     * @param receiver the `LibraryManager` that executes the action
     */
    DoShowUserNotifications(LibraryManager receiver) {
        super(Label.SHOW_USER_NOTIFICATIONS, receiver);
        addIntegerField("userId", Prompt.userId());
    }
    /**
     * Executes the command: reads the user id and displays the user's notifications.
     *
     * @throws CommandException if the user does not exist
     */
    @Override
    protected final void execute() throws CommandException {
        int userId = integerField("userId");

        try {
            _display.popup(_receiver.getLibrary()
                                    .getUserNotifications(userId)
                                    .stream()
                                    .map(Notification::toString)
                                    .toList());

        } catch (bci.core.exception.NoSuchUserWithIdException e) {
            throw new NoSuchUserException(userId);
        }
    }
}
