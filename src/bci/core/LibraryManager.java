package bci.core;

import bci.core.exception.*;

import java.io.*;

/**
 * The façade class. Represents the manager of this application. It manages the current
 * library and works as the interface between the core and user interaction layers.
 */
public class LibraryManager {

    /**
     * The object doing all the actual work.
     */
    /* The current library */
    private Library _library;
    private String associatedFile;

    /* To allow very first association with a file with no modifications */
    private boolean _firstSave = true;

    /**
     * Constructor. Creates a new LibraryManager with an empty Library.
     */
    public LibraryManager() {
        _library = new Library();
    }

    public Library getLibrary() {
        return _library;
    }

    public boolean hasAssociatedFile() {
        return associatedFile != null && !associatedFile.isEmpty();
    }

    /**
     * Saves the serialized application's state into the file associated to the current library
     *
     * @throws FileNotFoundException           if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current library does not have a file.
     * @throws IOException                     if there is some error while serializing the state of the network to disk.
     **/
    public void save() throws MissingFileAssociationException, FileNotFoundException, IOException {
        if (!_library.isModified() && !_firstSave)
            return;

        if (associatedFile == null || associatedFile.isEmpty())
            throw new MissingFileAssociationException();

        try (ObjectOutputStream obOut =new ObjectOutputStream(new FileOutputStream(associatedFile))) {
            obOut.writeObject(_library);
            _firstSave = false;
        }
    }

    /**
     * Saves the serialized application's state into the specified file. The current library is
     * associated to this file.
     *
     * @param filename the name of the file.
     * @throws FileNotFoundException           if for some reason the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current library does not have a file.
     * @throws IOException                     if there is some error while serializing the state of the network to disk.
     **/
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        associatedFile = filename;
        save();
    }

    /**
     * Loads the previously serialized application's state as set it as the current library.
     *
     * @param filename name of the file containing the serialized application's state
     *                 to load.
     * @throws UnavailableFileException if the specified file does not exist or there is
     *                                  an error while processing this file.
     **/
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream obIn = new ObjectInputStream(new FileInputStream(filename))) {
            _library = (Library) obIn.readObject();
            associatedFile = filename;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Read text input file and initializes the current library (which should be empty)
     * with the domain entities representeed in the import file.
     *
     * @param datafile name of the text input file
     * @throws ImportFileException if some error happens during the processing of the
     *                             import file.
     **/
    public void importFile(String datafile) throws ImportFileException {
        try {
            if (datafile != null && !datafile.isEmpty())
                _library.importFile(datafile);
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(datafile, e);
        }
    }
}
