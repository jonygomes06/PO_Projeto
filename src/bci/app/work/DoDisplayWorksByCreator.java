package bci.app.work;

import bci.core.LibraryManager;
import bci.app.exception.NoSuchCreatorException;
import bci.core.exception.NoSuchCreatorWithIdException;
import bci.core.work.Work;
import pt.tecnico.uilib.menus.Command;

/**
 * Display all works by a specific creator.
 */
class DoDisplayWorksByCreator extends Command<LibraryManager> {
    DoDisplayWorksByCreator(LibraryManager receiver) {
        super(Label.SHOW_WORKS_BY_CREATOR, receiver);
        addStringField("creatorId", Prompt.creatorId());
    }

    @Override
    protected final void execute() throws NoSuchCreatorException {
        String creatorId = stringField("creatorId");
        try {
            _display.popup(_receiver.getLibrary()
                                    .getCreatorByName(creatorId)
                                    .getWorks()
                                    .stream()
                                    .map(Work::toString)
                                    .toList());
        } catch (NoSuchCreatorWithIdException e) {
            throw new NoSuchCreatorException(creatorId);
        }
    }
}
