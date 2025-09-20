package bci.app.work;

import bci.core.LibraryManager;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Command to display all works.
 */
class DoDisplayWorks extends Command<LibraryManager> {

  DoDisplayWorks(LibraryManager receiver) {
    super(Label.SHOW_WORKS, receiver);
  }

  @Override
  protected final void execute() {
    //FIXME implement command
  }
}
