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
 * <p>
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

    /**
     * The current date of the library system.
     */
    private final Date _currentDate;

    /**
     * Next user ID to be assigned.
     */
    private int _nextUserId = 1;

    /**
     * Next work ID to be assigned.
     */
    private int _nextWorkId = 1;

    /**
     * Next request ID to be assigned.
     */
    private int _nextRequestId = 1;

    /**
     * A set of all registered users. Using TreeSet to automatically sort users by name and ID.
     */
    private final Set<User> _users;

    /**
     * A map of user IDs to their corresponding User objects. Used for efficient lookup by ID.
     */
    private final Map<Integer, User> _usersById;

    /**
     * A map of work IDs to their corresponding Work objects. Using LinkedHashMap to preserve insertion order.
     */
    private final Map<Integer, Work> _works;

    /**
     * A map of creator names to their corresponding Creator objects. Used for efficient lookup by name.
     */
    private final Map<String, Creator> _creators;

    /**
     * A map of active request IDs to their corresponding Request objects.
     */
    private final Map<Integer, Request> _activeRequests;

    /**
     * A list of archived requests.
     */
    private final List<Request> _archivedRequests;

    /**
     * List of rules to validate requests.
     */
    private final List<RequestRule> _requestRules;

    /**
     * A flag indicating whether the library's state has been modified.
     */
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

    /**
     * Gets the notifications for a user.
     *
     * @param userId the ID of the user.
     * @return a collection of notifications for the user.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     */
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

    public Collection<User> getUsersWithEqOrMoreFines(int fine) {
        return _users.stream()
                .filter(u -> u.getTotalFines() >= fine)
                .sorted(Comparator.comparing(User::getTotalFinesNegative))
                .toList();
    }

    public User getUserWithHighestValue() throws IllegalArgumentException {
        if (_activeRequests.isEmpty()) {
            throw new IllegalArgumentException("Sem obras");
        }

        User highestValueUser = _activeRequests.values().stream().findFirst().get().getUser();
        int currentHighestValue = highestValueUser.getTotalValueOfRequests();
        int currentNameSize = highestValueUser.getName().length();

        for (User user : _users) {
            int value = user.getTotalValueOfRequests();
            if (value > currentHighestValue) {
                highestValueUser = user;
                currentHighestValue = value;
                currentNameSize = user.getName().length();
                continue;
            }

            if (value == currentHighestValue && user.getName().length() <= currentNameSize) {
                highestValueUser = user;
                currentNameSize = user.getName().length();
            }
        }

        return highestValueUser;
    }

    /**
     * Pays the fine for a suspended user.
     *
     * @param userId the ID of the user.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     * @throws UserNotSuspendedException if the user is not suspended.
     */
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
     * @return a collection of all works.
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

    /**
     * Changes the inventory of a work.
     *
     * @param workId the ID of the work.
     * @param amount the amount to change.
     * @throws NoSuchWorkWithIdException if no work with the given ID exists.
     * @throws NotEnoughInventoryException if there is not enough inventory.
     */
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

    /**
     * Searches works by a term.
     *
     * @param term the search term.
     * @return a collection of works matching the term.
     */
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

    /**
     * Subscribes a user to notifications for a work.
     *
     * @param userId the ID of the user.
     * @param workId the ID of the work.
     * @param type the type of notification.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     * @throws NoSuchWorkWithIdException if no work with the given ID exists.
     */
    public void subscribeUserToWorkNotifications(int userId, int workId, NotificationType type)
            throws NoSuchUserWithIdException, NoSuchWorkWithIdException {
        User user = getUserById(userId);
        Work work = getWorkById(workId);
        user.subscribeToWorkForNotification(workId, type);
        work.subscribe(user);
        _modified = true;
    }

    /**
     * Unsubscribes a user from notifications for a work.
     *
     * @param userId the ID of the user.
     * @param workId the ID of the work.
     * @param type the type of notification.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     * @throws NoSuchWorkWithIdException if no work with the given ID exists.
     */
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

    /**
     * Requests a work for a user.
     *
     * @param userId the ID of the user.
     * @param workId the ID of the work.
     * @return the deadline for the request.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     * @throws NoSuchWorkWithIdException if no work with the given ID exists.
     * @throws RequestRuleFailedException if any request rule fails.
     */
    public int requestWork(int userId, int workId)
            throws NoSuchUserWithIdException, NoSuchWorkWithIdException, RequestRuleFailedException, IllegalArgumentException {
        if (_activeRequests.size() == 12) {
            throw new IllegalArgumentException("Limite de requisições simultâneas da biblioteca atingido");
        }


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

    /**
     * Returns a work borrowed by a user.
     *
     * @param userId the ID of the user.
     * @param workId the ID of the work.
     * @return the returned Request object.
     * @throws NoSuchUserWithIdException if no user with the given ID exists.
     * @throws NoSuchWorkWithIdException if no work with the given ID exists.
     * @throws WorkNotBorrowedByUserException if the work was not borrowed by the user.
     */
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
     * @throws InvalidArgumentsException if the builder arguments are invalid.
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

    /**
     * Updates the states of all users based on the current date.
     */
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