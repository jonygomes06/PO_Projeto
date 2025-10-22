package bci.app.request;

import bci.app.exception.BorrowingRuleFailedException;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;
import bci.core.Library;
import bci.core.LibraryManager;
import bci.core.exception.NoSuchUserWithIdException;
import bci.core.exception.NoSuchWorkWithIdException;
import bci.core.exception.RequestRuleFailedException;
import bci.core.request.WorkHasAvailableCopyRule;
import bci.core.user.NotificationType;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * UI command for requesting a work (requirement 4.4.1).
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Collect user and work identifiers.</li>
 *   <li>Request the work via the {@link Library}.</li>
 *   <li>Display the expected return day on success.</li>
 *   <li>Handle (un)subscription to availability notifications when no copy is available.</li>
 *   <li>Map domain exceptions to application-level exceptions.</li>
 * </ul>
 */
class DoRequestWork extends Command<LibraryManager> {

    /**
     * Creates the command and registers input fields for user and work identifiers.
     *
     * @param receiver the {@link LibraryManager} that provides access to the {@link Library}
     */
    DoRequestWork(LibraryManager receiver) {
        super(Label.REQUEST_WORK, receiver);
        addIntegerField("userId", bci.app.user.Prompt.userId());
        addIntegerField("workId", bci.app.work.Prompt.workId());
    }

    /**
     * Executes the request workflow:
     * <ol>
     *   <li>Reads the user and work identifiers from the form.</li>
     *   <li>Attempts to request the work in the library.</li>
     *   <li>On success, shows the expected return day and unsubscribes the user
     *       from availability notifications.</li>
     *   <li>If no copy is available, optionally subscribes the user to availability
     *       notifications based on confirmation.</li>
     * </ol>
     * Exception mapping:
     * <ul>
     *   <li>{@link NoSuchUserWithIdException} → {@link NoSuchUserException}</li>
     *   <li>{@link NoSuchWorkWithIdException} → {@link NoSuchWorkException}</li>
     *   <li>Other {@link RequestRuleFailedException} → {@link BorrowingRuleFailedException}</li>
     * </ul>
     *
     * @throws CommandException if an application-level error must be reported to the UI
     */
    @Override
    protected final void execute() throws CommandException {
        Library lib = _receiver.getLibrary();
        int userId = integerField("userId");
        int workId = integerField("workId");

        try {
            _display.popup(Message.workReturnDay(workId, lib.requestWork(userId, workId)));
            lib.unsubscribeUserToWorkNotifications(userId, workId, NotificationType.DISPONIBILIDADE);
        } catch (NoSuchUserWithIdException e) {
            throw new NoSuchUserException(userId);
        } catch (NoSuchWorkWithIdException e) {
            throw new NoSuchWorkException(workId);
        } catch (RequestRuleFailedException e) {
            if (e.getRuleId() == WorkHasAvailableCopyRule.RULE_ID) {
                if (Form.confirm(Prompt.returnNotificationPreference())) {
                    try {
                        lib.subscribeUserToWorkNotifications(userId, workId, NotificationType.DISPONIBILIDADE);
                    } catch (NoSuchUserWithIdException | NoSuchWorkWithIdException ex) {
                        throw new RuntimeException();
                    }
                }
            } else {
                throw new BorrowingRuleFailedException(userId, workId, e.getRuleId());
            }
        }
    }
}