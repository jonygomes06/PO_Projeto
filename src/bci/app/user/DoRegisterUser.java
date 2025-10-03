package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.UserRegistrationFailedException;
import bci.core.exception.InvalidArgumentsException;
import bci.core.user.User;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * 4.2.1. Register new user.
 */
class DoRegisterUser extends Command<LibraryManager> {

    DoRegisterUser(LibraryManager receiver) {
        super(Label.REGISTER_USER, receiver);
        addStringField("name", Prompt.userName());
        addStringField("email", Prompt.userEMail());
    }

    @Override
    protected final void execute() throws CommandException {
        String name = stringField("name");
        String email = stringField("email");
        try {
            User newUser = _receiver.getLibrary().registerUser(name, email);
            _display.popup(Message.registrationSuccessful(newUser.getId()));
        } catch (InvalidArgumentsException e) {
            throw new UserRegistrationFailedException(name, email);
        }
    }
}
