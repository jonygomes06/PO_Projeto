package bci.core;

import java.io.*;
import java.util.*;

import bci.core.exception.*;
import bci.core.request.*;
import bci.core.user.Notification;
import bci.core.user.NotificationType;
import bci.core.user.User;
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

    private int _nextUserId = 1;
    private int _nextWorkId = 1;
    private int _nextRequestId = 1;

    // A set of all registered users. Using TreeSet to automatically sort users by name and ID.
    private final Set<User> _users;

    // A map of user IDs to their corresponding User objects. Used for efficient lookup by ID.
    private final Map<Integer, User> _usersById;

    // A map of work IDs to their corresponding Work objects. Using LinkedHashMap to preserve insertion order.
    private final Map<Integer, Work> _works;

    // A map of creator names to their corresponding Creator objects. Used for efficient lookup by name.
    private final Map<String, Creator> _creators;

    private final Map<Integer, Request> _activeRequests;

    private final List<Request> _archivedRequests;

    private final List<RequestRule> _requestRules;

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
        _activeRequests = new HashMap<>();
        _archivedRequests = new LinkedList<>();

        _requestRules = List.of(
                new NoDuplicateRequestsRule(),
                new UserIsActiveRule(),
                new WorkHasAvailableCopyRule(),
                new SimultaneousRequestsLimitRule(),
                new WorkCategoryIsNotReferenceRule(),
                new WorkPriceLimitRule()
        );
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
        updateUsersStates();
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

        User newUser = new User(_nextUserId++, name, email);
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

    public Collection<Notification> getUserNotifications(int userId) throws NoSuchUserWithIdException {
        User user = getUserById(userId);
        return user.getNotifications();
    }

    /**
     * Gets an unmodifiable view of all registered users.
     *
     * @return a set of all users.
     */
    public Collection<User> getUsers() {
        return Collections.unmodifiableSet(_users);
    }

    public void payFine(int userId) throws NoSuchUserWithIdException, UserNotSuspendedException {
        User user = getUserById(userId);
        if (user.isActive()) {
            throw new UserNotSuspendedException(userId);
        }
        user.payFine(_currentDate.getCurrentDate());
        _modified = true;
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
    public Collection<Work> getWorks() {
        return Collections.unmodifiableCollection(_works.values());
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

    public void changeWorkInventory(int workId, int amount) throws NoSuchWorkWithIdException, NotEnoughInventoryException {
        Work work = getWorkById(workId);
        work.changeInventory(amount);

        if (work.shouldBeRemovedFromSystem()) {
            Collection<Request> requestsToDispose = work.getRequests();

            for (Request request : requestsToDispose) {
                _activeRequests.remove(request.getId());
                request.getUser().disposeRequest(request);
                _archivedRequests.remove(request);
            }


            Collection<Creator> creatorsToDispose = work.dispose();

            for (Creator creator : creatorsToDispose) {
                _creators.remove(creator.getName());
            }

            _works.remove(workId);
        }

        _modified = true;
    }

    public Collection<Work> searchWorksByTerm(String term) {
        if (term == null || term.isBlank()) {
            return Collections.emptyList();
        }

        String lowerCaseTerm = term.toLowerCase();

        return _works.values()
                .stream()
                .filter(work -> work.hasTerm(lowerCaseTerm))
                .toList();
    }

    public void subscribeUserToWorkNotifications(int userId, int workId, NotificationType type)
            throws NoSuchUserWithIdException, NoSuchWorkWithIdException {
        User user = getUserById(userId);
        Work work = getWorkById(workId);
        user.subscribeToWorkForNotification(workId, type);
        work.subscribe(user);
        _modified = true;
    }

    public void unsubscribeUserToWorkNotifications(int userId, int workId, NotificationType type)
            throws NoSuchUserWithIdException, NoSuchWorkWithIdException {
        User user = getUserById(userId);
        Work work = getWorkById(workId);
        user.unsubscribeFromWorkForNotification(workId, type);
        if (user.getSubscribedTypesOfWork(workId).isEmpty()) {
            work.unsubscribe(user);
        }
        _modified = true;
    }

    public int requestWork(int userId, int workId)
            throws NoSuchUserWithIdException, NoSuchWorkWithIdException, RequestRuleFailedException {
        User user = getUserById(userId);
        Work work = getWorkById(workId);

        for (RequestRule rule : _requestRules) {
            rule.check(user, work);
        }

        int deadline = _currentDate.getCurrentDate() + user.getRequestDuration(work);

        Request newRequest = new Request(_nextRequestId++, user, work, deadline);

        _activeRequests.put(newRequest.getId(), newRequest);
        user.requestWork(newRequest);
        work.requestWork(newRequest);
        _modified = true;

        return deadline;
    }

    public Request returnWork(int userId, int workId)
            throws NoSuchUserWithIdException, NoSuchWorkWithIdException, WorkNotBorrowedByUserException {
        User user = getUserById(userId);
        Work work = getWorkById(workId);

        Request requestToReturn = user.getActiveRequests()
                                      .stream()
                                      .filter(request -> request.getWork().equals(work))
                                      .findFirst()
                                      .orElse(null);

        if (requestToReturn == null) {
            throw new WorkNotBorrowedByUserException(workId, userId);
        }

        int currentDate = _currentDate.getCurrentDate();
        _activeRequests.remove(requestToReturn.getId());
        _archivedRequests.add(requestToReturn);
        user.returnWork(requestToReturn, currentDate);
        requestToReturn.getWork().returnWork();
        _modified = true;

        return requestToReturn;
    }

    /**
     * Registers a new work in the library.
     *
     * @param workBuilder the builder for creating the Work object.
     * @return the newly registered Work object.
     */
    <T extends Work, B extends Work.Builder<T, B>> T registerWork(B workBuilder) throws InvalidArgumentsException {
        T newWork = workBuilder.id(_nextWorkId++).build();
        _works.put(newWork.getId(), newWork);
        return newWork;
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

    private void updateUsersStates() {
        int currentDate = _currentDate.getCurrentDate();
        for (Request request : _activeRequests.values()) {
            request.getUser().updateState(currentDate);
        }
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