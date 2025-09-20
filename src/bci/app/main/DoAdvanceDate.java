package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * 4.1.3. Advance the current date.
 */
class DoAdvanceDate extends Command<LibraryManager> {

  DoAdvanceDate(LibraryManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    //FIXME add command fields
  }

  @Override
  protected final void execute() {
    //FIXME implement command
  }
}
