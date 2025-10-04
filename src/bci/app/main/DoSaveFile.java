package bci.app.main;

import bci.core.LibraryManager;
import bci.core.exception.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;

import java.io.IOException;

/**
 * Save to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<LibraryManager> {

    DoSaveFile(LibraryManager receiver) {
        super(Label.SAVE_FILE, receiver);
    }

    @Override
    protected final void execute() {
        if (_receiver.hasAssociatedFile()) {
            try {
                _receiver.save();
            } catch (MissingFileAssociationException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            String filename = Form.requestString(Prompt.newSaveAs());
            try {
                _receiver.saveAs(filename);
            } catch (MissingFileAssociationException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
