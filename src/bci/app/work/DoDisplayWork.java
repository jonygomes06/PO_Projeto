package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchWorkException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command to display a work.
 */
class DoDisplayWork extends Command<LibraryManager> {

  DoDisplayWork(LibraryManager receiver) {
    super(Label.SHOW_WORK, receiver);
    //FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException {
     //FIXME implement command
  }
}
