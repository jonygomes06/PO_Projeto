package bci.core;

import bci.core.exception.*;
import java.io.*;

// FIXME import classes

/**
 * The façade class. Represents the manager of this application. It manages the current
 * library and works as the interface between the core and user interaction layers.
 */
public class LibraryManager {


  /** The object doing all the actual work. */
  /* The current library */
  // FIXME: initialize this field
  private Library _library;

  // FIXME: add more fields if needed
  // FIXME: add constructor if needed
  // FIXME: add more methods if needed

  /**
   * Saves the serialized application's state into the file associated to the current library
   *
   * @throws FileNotFoundException if for some reason the file cannot be created or opened. 
   * @throws MissingFileAssociationException if the current library does not have a file.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   **/
  public void save() throws MissingFileAssociationException, FileNotFoundException, IOException {
    // FIXME implement serialization method
  }

  /**
   * Saves the serialized application's state into the specified file. The current library is
   * associated to this file.
   *
   * @param filename the name of the file.
   * @throws FileNotFoundException if for some reason the file cannot be created or opened.
   * @throws MissingFileAssociationException if the current library does not have a file.
   * @throws IOException if there is some error while serializing the state of the network to disk.
   **/
  public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
    // FIXME implement serialization method
  }

  /**
   * Loads the previously serialized application's state as set it as the current library.
   *
   * @param filename name of the file containing the serialized application's state
   *        to load.
   * @throws UnavailableFileException if the specified file does not exist or there is
   *         an error while processing this file.
   **/
  public void load(String filename) throws UnavailableFileException {
    // FIXME implement serialization method
  }

  /**
   * Read text input file and initializes the current library (which should be empty)
   * with the domain entities representeed in the import file.
   *
   * @param datafile name of the text input file
   * @throws ImportFileException if some error happens during the processing of the
   * import file.
   **/
  public void importFile(String datafile) throws ImportFileException {
    try {
      if (datafile != null && !datafile.isEmpty())
        _library.importFile(datafile);
    } catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ e) {
      throw new ImportFileException(datafile, e);
    }
  } 
}
