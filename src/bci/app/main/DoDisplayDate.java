package bci.app.main;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * 4.1.2. Display the current date.
 */
class DoDisplayDate extends Command<LibraryManager> {

  DoDisplayDate(LibraryManager receiver) {
    super(Label.DISPLAY_DATE, receiver);
  }

  @Override
  protected final void execute() {
    //FIXME implement command
  }

}
