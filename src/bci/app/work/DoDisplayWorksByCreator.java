package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchCreatorException;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed

/**
 * Display all works by a specific creator.
 */
class DoDisplayWorksByCreator extends Command<LibraryManager> {
  DoDisplayWorksByCreator(LibraryManager receiver) {
    super(Label.SHOW_WORKS_BY_CREATOR, receiver);
    //FIXME add command fields
  }

  @Override
  protected final void execute() throws NoSuchCreatorException {
    //FIXME implement command
  }
}
