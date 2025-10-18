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
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.4.1. Request work.
 */
class DoRequestWork extends Command<LibraryManager> {

    DoRequestWork(LibraryManager receiver) {
        super(Label.REQUEST_WORK, receiver);
        addIntegerField("userId", bci.app.user.Prompt.userId());
        addIntegerField("workId", bci.app.work.Prompt.workId());
    }

    @Override
    protected final void execute() throws CommandException {
        Library lib = _receiver.getLibrary();
        int userId = integerField("userId");
        int workId = integerField("workId");

        try {
            _display.popup(Message.workReturnDay(workId, lib.requestWork(userId, workId)));
        } catch (NoSuchUserWithIdException e) {
            throw new NoSuchUserException(userId);
        } catch (NoSuchWorkWithIdException e) {
            throw new NoSuchWorkException(workId);
        } catch (RequestRuleFailedException e) {
            if (e.getRuleId() == WorkHasAvailableCopyRule.RULE_ID) {
                if (Form.confirm(Prompt.returnNotificationPreference())) {
                    try {
                        lib.subscribeUserToWorkNotifications(userId, workId);
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
