package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.core.user.Notification;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.3. Show notifications of a specific user.
 */
class DoShowUserNotifications extends Command<LibraryManager> {

    DoShowUserNotifications(LibraryManager receiver) {
        super(Label.SHOW_USER_NOTIFICATIONS, receiver);
        addIntegerField("userId", Prompt.userId());
    }

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
