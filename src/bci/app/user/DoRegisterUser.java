package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.UserRegistrationFailedException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * 4.2.1. Register new user.
 */
class DoRegisterUser extends Command<LibraryManager> {

  DoRegisterUser(LibraryManager receiver) {
    super(Label.REGISTER_USER, receiver);
    //FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException {
    //FIXME implement command
  }
}
