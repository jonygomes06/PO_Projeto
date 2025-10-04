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
    private Library _library;

    /**
     * The file associated to the current library, if any.
     */
    private String associatedFile;

    /**
     * A flag indicating whether this is the first time the library is being saved.
     */
    private boolean _firstSave = true;

    /**
     * Constructor. Creates a new LibraryManager with an empty Library.
     */
    public LibraryManager() {
        _library = new Library();
    }

    /**
     * Retrieves the current library instance.
     *
     * @return the current Library object.
     */
    public Library getLibrary() {
        return _library;
    }

    /**
     * Checks if there is an associated file for the current library.
     *
     * @return true if there is an associated file, false otherwise.
     */
    public boolean hasAssociatedFile() {
        return associatedFile != null && !associatedFile.isEmpty();
    }

    /**
     * Saves the serialized application's state into the file associated to the current library.
     *
     * @throws FileNotFoundException           if the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current library does not have an associated file.
     * @throws IOException                     if an error occurs while serializing the state to disk.
     */
    public void save() throws MissingFileAssociationException, FileNotFoundException, IOException {
        if (!_library.isModified() && !_firstSave)
            return;

        if (associatedFile == null || associatedFile.isEmpty())
            throw new MissingFileAssociationException();

        try (ObjectOutputStream obOut = new ObjectOutputStream(new FileOutputStream(associatedFile))) {
            obOut.writeObject(_library);
            _firstSave = false;
            _library.setToUnmodified();
        }
    }

    /**
     * Saves the serialized application's state into the specified file and associates the current library with it.
     *
     * @param filename the name of the file to save the library state.
     * @throws FileNotFoundException           if the file cannot be created or opened.
     * @throws MissingFileAssociationException if the current library does not have an associated file.
     * @throws IOException                     if an error occurs while serializing the state to disk.
     */
    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        associatedFile = filename;
        save();
    }

    /**
     * Loads the previously serialized application's state and sets it as the current library.
     *
     * @param filename the name of the file containing the serialized application's state.
     * @throws UnavailableFileException if the file does not exist or an error occurs while processing it.
     */
    public void load(String filename) throws UnavailableFileException {
        try (ObjectInputStream obIn = new ObjectInputStream(new FileInputStream(filename))) {
            _library = (Library) obIn.readObject();
            associatedFile = filename;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new UnavailableFileException(filename);
        }
    }

    /**
     * Reads a text input file and initializes the current library with the domain entities represented in the file.
     *
     * @param datafile the name of the text input file to import.
     * @throws ImportFileException if an error occurs during the processing of the import file.
     */
    public void importFile(String datafile) throws ImportFileException {
        try {
            if (datafile != null && !datafile.isEmpty())
                _library.importFile(datafile);
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(datafile, e);
        }
    }
}
