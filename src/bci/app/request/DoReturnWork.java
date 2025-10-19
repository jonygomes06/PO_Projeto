package bci.app.request;

import bci.core.Library;
import bci.core.LibraryManager;
import bci.app.exception.NoSuchUserException;
import bci.app.exception.NoSuchWorkException;
import bci.app.exception.WorkNotBorrowedByUserException;
import bci.core.exception.NoSuchUserWithIdException;
import bci.core.exception.NoSuchWorkWithIdException;
import bci.core.exception.UserNotSuspendedException;
import bci.core.request.Request;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.4.2. Return a work.
 */
class DoReturnWork extends Command<LibraryManager> {

    DoReturnWork(LibraryManager receiver) {
        super(Label.RETURN_WORK, receiver);
        addIntegerField("userId", bci.app.user.Prompt.userId());
        addIntegerField("workId", bci.app.work.Prompt.workId());
    }

    @Override
    protected final void execute() throws CommandException {
        Library lib = _receiver.getLibrary();
        int userId = integerField("userId");
        int workId = integerField("workId");

        try {
            Request request = lib.returnWork(userId, workId);
            int currentDate = lib.getCurrentDate().getCurrentDate();

            if (!request.shouldPayFine(currentDate)) { return; }

            _display.popup(Message.showFine(userId, request.getUser().getTotalFines()));

            if (Form.confirm(Prompt.finePaymentChoice()))
                lib.payFine(userId);

        } catch (NoSuchUserWithIdException e) {
            throw new NoSuchUserException(userId);
        } catch (NoSuchWorkWithIdException e) {
            throw new NoSuchWorkException(workId);
        } catch (bci.core.exception.WorkNotBorrowedByUserException e) {
            throw new WorkNotBorrowedByUserException(e.getWorkId(), e.getUserId());
        } catch (UserNotSuspendedException e) {
            throw new RuntimeException(e);
        }
    }
}
