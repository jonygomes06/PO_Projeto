package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import bci.core.exception.NoSuchWorkWithIdException;
import bci.core.exception.NotEnoughInventoryException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * UI command to change the inventory (number of exemplars) of a work in the library.
 * Delegates to the underlying library service exposed by the `LibraryManager`.
 *
 * @see LibraryManager
 */
class DoChangeWorkInventory extends Command<LibraryManager> {
    /**
     * Creates the command to change a work's inventory.
     * Registers the required input fields: `workId` and `amountToUpdate`.
     *
     * @param receiver the application `LibraryManager` used to access the library
     */
    DoChangeWorkInventory(LibraryManager receiver) {
        super(Label.CHANGE_WORK_INVENTORY, receiver);
        addIntegerField("workId", Prompt.workId());
        addIntegerField("amountToUpdate", Prompt.amountToUpdate());
    }
    /**
     * Executes the command: reads input fields and updates the work inventory.
     * If the work id is unknown, throws `NoSuchWorkException` (a `CommandException`).
     * If the update resulted in negative inventory, a popup is shown and the command returns.
     *
     * @throws CommandException if the specified work does not exist
     */
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
