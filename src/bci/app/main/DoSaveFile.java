package bci.app.main;

import bci.core.LibraryManager;
import bci.core.exception.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;

import java.io.IOException;

/**
 * Command to save the library data to a file.
 * This class is part of the user interface layer and interacts with the `LibraryManager`
 * to save the current state of the library to a file. If the library is not associated
 * with a file, the user is prompted to provide a filename.
 */
class DoSaveFile extends Command<LibraryManager> {

    /**
     * Constructs the command to save the library data to a file.
     * Initializes the command with the label for saving the file.
     *
     * @param receiver the `LibraryManager` instance that handles the library operations.
     */
    DoSaveFile(LibraryManager receiver) {
        super(Label.SAVE_FILE, receiver);
    }

    /**
     * Executes the command to save the library data to a file.
     * If the library is associated with a file, it saves the data to that file.
     * Otherwise, prompts the user for a filename and saves the data to the specified file.
     *
     * @throws RuntimeException if an error occurs during the save operation.
     */
    @Override
    protected final void execute() {
        try {
            if (_receiver.hasAssociatedFile()) {
                _receiver.save();
            } else {
                String filename = Form.requestString(Prompt.newSaveAs());
                _receiver.saveAs(filename);
            }
        } catch (MissingFileAssociationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}