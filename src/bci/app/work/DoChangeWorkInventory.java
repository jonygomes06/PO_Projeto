package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import bci.core.exception.NoSuchWorkWithIdException;
import bci.core.exception.NotEnoughInventoryException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Change the number of exemplars of a work.
 */
class DoChangeWorkInventory extends Command<LibraryManager> {
    DoChangeWorkInventory(LibraryManager receiver) {
        super(Label.CHANGE_WORK_INVENTORY, receiver);
        addIntegerField("workId", Prompt.workId());
        addIntegerField("amountToUpdate", Prompt.amountToUpdate());
    }

    @Override
    protected final void execute() throws CommandException {
        int workId = integerField("workId");
        int amount = integerField("amountToUpdate");

        try {
            _receiver.getLibrary().changeWorkInventory(workId, amount);
        } catch (NoSuchWorkWithIdException e) {
            throw new NoSuchWorkException(workId);
        } catch (NotEnoughInventoryException e) {
            _display.popup(Message.notEnoughInventory(workId, amount));
        }

    }
}
