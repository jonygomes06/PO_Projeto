package bci.app.main;

import bci.core.LibraryManager;
import bci.app.exception.FileOpenFailedException;
import bci.core.exception.UnavailableFileException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

class DoOpenFile extends Command<LibraryManager> {

  DoOpenFile(LibraryManager receiver) {
    super(Label.OPEN_FILE, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
  /*
    try {
    //FIXME implement command
    } catch (UnavailableFileException efe) {
    throw new FileOpenFailedException(efe);
    }
  */
  }
}
