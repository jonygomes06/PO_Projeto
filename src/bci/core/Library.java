package bci.core;

import java.io.*;
import java.util.*;

import bci.core.date.Date;
import bci.core.exception.UnrecognizedEntryException;
import bci.core.user.User;
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

    private Date _currentDate;
    private Set<User> _users;
    private Map<Integer, User> _usersById;
    private Map<Integer, Work> _works;

    public Library() {
        _currentDate = new Date();
        _users = new TreeSet<>();
        _usersById = new HashMap<>();
        _works = new LinkedHashMap<>();
    }

    public Date getCurrentDate() {
        return _currentDate;
    }

    public void advanceDate(int days) {
        if (days <= 0) return;
        _currentDate.advanceDate(days);
        updateUsersStates();
    }

    public void updateUsersStates() {
        //FIXME implement method
    }

    public User registerUser(String name, String email) {
        User newUser = new User(name, email);
        _users.add(newUser);
        _usersById.put(newUser.getId(), newUser);

        return newUser;
    }

    public User getUserById(int id) {
        return _usersById.get(id);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(_users);
    }

    public Work getWorkById(int id) {
        return _works.get(id);
    }

    public Map<Integer, Work> getWorks() {
        return Collections.unmodifiableMap(_works);
    }

    /**
     * Read text input file at the beginning of the program and populates the
     * state of this library with the domain entities represented in the text file.
     *
     * @param filename name of the text input file to process
     * @throws UnrecognizedEntryException if some entry is not correct
     * @throws IOException                if there is an IO erro while processing the text file
     **/
    void importFile(String filename) throws UnrecognizedEntryException, IOException /* FIXME maybe other exceptions */ {
        //FIXME implement method
    }
}
