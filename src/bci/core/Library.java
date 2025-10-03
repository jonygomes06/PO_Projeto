package bci.core;

import java.io.*;
import java.util.*;

import bci.core.date.Date;
import bci.core.exception.InvalidArgumentsException;
import bci.core.exception.NoSuchUserWithIdException;
import bci.core.exception.NoSuchWorkWithIdException;
import bci.core.exception.UnrecognizedEntryException;
import bci.core.user.User;
import bci.core.work.Book;
import bci.core.work.Dvd;
import bci.core.work.Work;

/**
 * Class that represents the library as a whole.
 */
public class Library implements Serializable {

    /**
     * Serial number for serialization.
     */
    @Serial
    private static final long serialVersionUID = 202501101348L;

    private final Date _currentDate;
    private final Set<User> _users;
    private final Map<Integer, User> _usersById;
    private final Map<Integer, Work> _works;
    private final Map<String, Creator> _creators;

    Library() {
        _currentDate = new Date();
        _users = new TreeSet<>();
        _usersById = new HashMap<>();
        _works = new LinkedHashMap<>();
        _creators = new HashMap<>();
    }

    public Date getCurrentDate() {
        return _currentDate;
    }

    public void advanceDate(int days) {
        if (days <= 0) return;
        _currentDate.advanceDate(days);
//        updateUsersStates(); FIXME so para a entrega final
    }

    public User registerUser(String name, String email) throws InvalidArgumentsException {
        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            throw new InvalidArgumentsException("Name and email must be non-empty.");
        }

        User newUser = new User(name, email);
        _users.add(newUser);
        _usersById.put(newUser.getId(), newUser);
        return newUser;
    }

    public User getUserById(int id) throws NoSuchUserWithIdException {
        User user = _usersById.get(id);

        if (user == null) {
            throw new NoSuchUserWithIdException(id);
        }

        return user;
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(_users);
    }

    public Work getWorkById(int id) throws NoSuchWorkWithIdException {
        Work work = _works.get(id);

        if (work == null) {
            throw new NoSuchWorkWithIdException(id);
        }

        return work;
    }

    public Map<Integer, Work> getWorks() {
        return Collections.unmodifiableMap(_works);
    }

    Book registerBook(Book.Builder bookBuilder) {
        Book newBook = bookBuilder.build();
        _works.put(newBook.getId(), newBook);
        return newBook;
    }

    Dvd registerDvd(Dvd.Builder dvdBuilder) {
        Dvd newDvd = dvdBuilder.build();
        _works.put(newDvd.getId(), newDvd);
        return newDvd;
    }

    Creator registerCreator(String name) throws InvalidArgumentsException {
        if (name == null || name.isBlank()) {
            throw new InvalidArgumentsException("Creator name must be non-empty.");
        }

        return _creators.computeIfAbsent(name, Creator::new);
    }

    /**
     * Read text input file at the beginning of the program and populates the
     * state of this library with the domain entities represented in the text file.
     *
     * @param filename name of the text input file to process
     * @throws UnrecognizedEntryException if some entry is not correct
     * @throws IOException                if there is an IO erro while processing the text file
     **/
    void importFile(String filename) throws UnrecognizedEntryException, IOException {
        ImportFileParser parser = new ImportFileParser(this);
        parser.parseFile(filename);
    }
}
