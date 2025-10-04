package bci.app.main;

import bci.core.LibraryManager;
import bci.app.exception.FileOpenFailedException;
import bci.core.exception.UnavailableFileException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to open a file in the library system.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to load a file into the system. If the library has unsaved changes, the user is prompted
 * to save them before proceeding.
 */
class DoOpenFile extends Command<LibraryManager> {

    /**
     * Constructs the command to open a file.
     * Initializes the command with the label and input field for the filename.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoOpenFile(LibraryManager receiver) {
        super(Label.OPEN_FILE, receiver);
        addStringField("filename", Prompt.openFile());
    }

    /**
     * Executes the command to open a file.
     * Prompts the user to save changes if the library has been modified, then attempts
     * to load the specified file. If the file is unavailable, throws a `FileOpenFailedException`.
     *
     * @throws CommandException if an error occurs while opening the file.
     */
    @Override
    protected final void execute() throws CommandException {
        String filename = stringField("filename");

        if (_receiver.getLibrary().isModified()) {
            boolean saveBeforeExit = Form.confirm(Prompt.saveBeforeExit());
            if (saveBeforeExit) {
                new DoSaveFile(_receiver).execute();
            }
        }

        try {
            _receiver.load(filename);
        } catch (UnavailableFileException efe) {
            throw new FileOpenFailedException(efe);
        }
    }
}