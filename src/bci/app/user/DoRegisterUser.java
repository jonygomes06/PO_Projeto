package bci.app.user;

import bci.core.LibraryManager;
import bci.app.exception.UserRegistrationFailedException;
import bci.core.exception.InvalidArgumentsException;
import bci.core.user.User;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to register a new user in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to perform the user registration operation.
 */
class DoRegisterUser extends Command<LibraryManager> {

    /**
     * Constructs the command to register a new user.
     * Initializes the command with the label and input fields for username and email.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoRegisterUser(LibraryManager receiver) {
        super(Label.REGISTER_USER, receiver);
        addStringField("name", Prompt.userName());
        addStringField("email", Prompt.userEMail());
    }

    /**
     * Executes the command to register a new user.
     * Reads the user-provided name and email, and delegates the registration
     * to the `LibraryManager`. Displays a success message on successful registration
     * or throws a `UserRegistrationFailedException` if the registration fails.
     *
     * @throws CommandException if the user registration fails due to invalid arguments.
     */
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