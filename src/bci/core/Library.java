package bci.core;

import java.io.*;
import java.util.*;

import bci.core.date.Date;
import bci.core.exception.*;
import bci.core.user.User;
import bci.core.work.Book;
import bci.core.work.Dvd;
import bci.core.work.Work;

/**
 * Class that represents the library as a whole.
 * This class manages the library's state, including users, works (books and DVDs), and creators.
 * It provides methods for registering users, works, and creators, as well as retrieving and managing them.
 * Implements Serializable for persistence.
 */
public class Library implements Serializable {

    /**
     * Serial number for serialization.
     */
    @Serial
    private static final long serialVersionUID = 202501101348L;

    // The current date of the library system.
    private final Date _currentDate;

    // A set of all registered users.
    private final Set<User> _users;

    // A map of user IDs to their corresponding User objects.
    private final Map<Integer, User> _usersById;

    // A map of work IDs to their corresponding Work objects.
    private final Map<Integer, Work> _works;

    // A map of creator names to their corresponding Creator objects.
    private final Map<String, Creator> _creators;

    // A flag indicating whether the library's state has been modified.
    private transient boolean _modified = false;

    /**
     * Constructs a new Library instance with default values.
     * Initializes the current date, user set, user map, work map, and creator map.
     */
    Library() {
        _currentDate = new Date();
        _users = new TreeSet<>();
        _usersById = new HashMap<>();
        _works = new LinkedHashMap<>();
        _creators = new HashMap<>();
    }

    /**
     * Gets the current date of the library system.
     *
     * @return the current date.
     */
    public Date getCurrentDate() {
        return _currentDate;
    }

    /**
     * Advances the current date by a specified number of days.
     * If the number of days is non-positive, the method does nothing.
     *
     * @param days the number of days to advance.
     */
    public void advanceDate(int days) {
        if (days <= 0) return;
        _currentDate.advanceDate(days);
        // updateUsersStates(); FIXME so para a entrega final
        _modified = true;
    }

    /**
     * Registers a new user in the library.
     *
     * @param name  the name of the user.
     * @param email the email of the user.
     * @return the newly registered User object.
     * @throws InvalidArgumentsException if the name or email is null or empty.
     */
    public User registerUser(String name, String email) throws InvalidArgumentsException {
        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            throw new InvalidArgumentsException("Name and email must be non-empty.");
        }

        User newUser = new User(name, email);
        _users.add(newUser);
        _usersById.put(newUser.getId(), newUser);
        _modified = true;
        return newUser;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user.
     * @return the User object with the specified ID.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     */
    public User getUserById(int id) throws NoSuchUserWithIdException {
        User user = _usersById.get(id);

        if (user == null) {
            throw new NoSuchUserWithIdException(id);
        }

        return user;
    }

    /**
     * Gets an unmodifiable view of all registered users.
     *
     * @return a set of all users.
     */
    public Set<User> getUsers() {
        return Collections.unmodifiableSet(_users);
    }

    /**
     * Retrieves a work by its ID.
     *
     * @param id the ID of the work.
     * @return the Work object with the specified ID.
     * @throws NoSuchWorkWithIdException if no work with the given ID exists.
     */
    public Work getWorkById(int id) throws NoSuchWorkWithIdException {
        Work work = _works.get(id);

        if (work == null) {
            throw new NoSuchWorkWithIdException(id);
        }

        return work;
    }

    /**
     * Gets an unmodifiable view of all works in the library.
     *
     * @return a map of work IDs to Work objects.
     */
    public Map<Integer, Work> getWorks() {
        return Collections.unmodifiableMap(_works);
    }

    /**
     * Retrieves a creator by their name.
     *
     * @param name the name of the creator.
     * @return the Creator object with the specified name.
     * @throws NoSuchCreatorWithIdException if no creator with the given name exists.
     */
    public Creator getCreatorByName(String name) throws NoSuchCreatorWithIdException {
        Creator creator = _creators.get(name);

        if (creator == null) {
            throw new NoSuchCreatorWithIdException(name);
        }

        return creator;
    }

    /**
     * Registers a new book in the library.
     *
     * @param bookBuilder the builder for creating the Book object.
     * @return the newly registered Book object.
     */
    Book registerBook(Book.Builder bookBuilder) {
        Book newBook = bookBuilder.build();
        _works.put(newBook.getId(), newBook);
        return newBook;
    }

    /**
     * Registers a new DVD in the library.
     *
     * @param dvdBuilder the builder for creating the Dvd object.
     * @return the newly registered Dvd object.
     */
    Dvd registerDvd(Dvd.Builder dvdBuilder) {
        Dvd newDvd = dvdBuilder.build();
        _works.put(newDvd.getId(), newDvd);
        return newDvd;
    }

    /**
     * Registers a new creator in the library.
     *
     * @param name the name of the creator.
     * @return the newly registered Creator object.
     * @throws InvalidArgumentsException if the name is null or empty.
     */
    Creator registerCreator(String name) throws InvalidArgumentsException {
        if (name == null || name.isBlank()) {
            throw new InvalidArgumentsException("Creator name must be non-empty.");
        }
        return _creators.computeIfAbsent(name, Creator::new);
    }

    /**
     * Checks if the library's state has been modified.
     *
     * @return true if the state has been modified, false otherwise.
     */
    public boolean isModified() {
        return _modified;
    }

    /**
     * Sets the library's state to unmodified.
     * This method is used to reset the modification flag, indicating that
     * the library's state has not been changed since the last save, only
     * used by {@link LibraryManager#save()}.
     */
    void setToUnmodified() {
        _modified = false;
    }

    /**
     * Reads a text input file at the beginning of the program and populates the
     * state of this library with the domain entities represented in the text file.
     *
     * @param filename the name of the text input file to process.
     * @throws UnrecognizedEntryException if some entry is not correct.
     * @throws IOException                if there is an IO error while processing the text file.
     */
    void importFile(String filename) throws UnrecognizedEntryException, IOException {
        ImportFileParser parser = new ImportFileParser(this);
        parser.parseFile(filename);
    }
}