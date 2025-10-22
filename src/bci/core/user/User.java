package bci.core.user;

import bci.core.request.Request;
import bci.core.work.Work;
import bci.core.work.WorkObserver;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * Represents a user in the system, capable of making requests for work and receiving notifications.
 * Maintains user state, classification, requests, notifications, and fines.
 */
public class User extends WorkObserver implements Comparable<User>, Serializable {

    @Serial
    private static final long serialVersionUID = 7147658457022990947L;

    private final int _id;
    private final String _name;
    private final String _email;
    private boolean _isActive;
    private UserClassificationState _classification;
    private final List<Request> _activeRequests;
    private final List<Request> _allRequests;
    private final List<Notification> _notifications;
    private int _totalFines;

    /**
     * Constructs a new User.
     * @param id User identifier
     * @param name User name
     * @param email User email
     */
    public User(int id, String name, String email) {
        _id = id;
        _name = name;
        _email = email;
        _isActive = true;
        _classification = NormalState.getInstance();
        _activeRequests = new ArrayList<>();
        _allRequests = new LinkedList<>();
        _notifications = new LinkedList<>();
        _totalFines = 0;
    }

    /**
     * Gets the user identifier.
     * @return user id
     */
    public int getId() {
        return _id;
    }

    /**
     * Gets the username.
     * @return username
     */
    public String getName() {
        return _name;
    }

    /**
     * Checks if the user is active.
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return _isActive;
    }

    /**
     * Gets the maximum number of simultaneous requests allowed for the user.
     * @return max simultaneous requests
     */
    public int getMaxSimultaneousRequests() {
        return _classification.getMaxSimultaneousRequests();
    }

    /**
     * Gets the maximum price for a work request allowed for the user.
     * @return max work request price
     */
    public int getMaxWorkRequestPrice() {
        return _classification.getMaxWorkRequestPrice();
    }

    /**
     * Gets the duration for a work request for the user.
     * @param work the work to request
     * @return request duration
     */
    public int getRequestDuration(Work work) {
        return _classification.getRequestDuration(work);
    }

    /**
     * Gets the collection of active requests.
     * @return unmodifiable list of active requests
     */
    public Collection<Request> getActiveRequests() {
        return Collections.unmodifiableList(_activeRequests);
    }

    /**
     * Gets and clears the notifications for the user.
     * @return list of notifications
     */
    public Collection<Notification> getNotifications() {
        List<Notification> notificationsCopy = new ArrayList<>(_notifications);
        _notifications.clear();
        return notificationsCopy;
    }

    /**
     * Gets the total fines for the user.
     * @return total fines
     */
    public int getTotalFines() {
        return _totalFines;
    }

    /**
     * Updates the user state and classification based on the current date.
     * @param currentDate the current date
     */
    public void updateState(int currentDate) {
        _isActive = hasNoSuspensionFlags(currentDate);

        _classification = _classification.updateState(this, currentDate);
    }

    /**
     * Pays all fines for returned requests and updates user state.
     * @param currentDate the current date
     */
    public void payFine(int currentDate) {
        List<Request> toRemove = new ArrayList<>();
        for (Request request : _activeRequests) {
            if (request.hasBeenReturned()) {
                request.liquidateFine();
                toRemove.add(request);
            }
        }
        _activeRequests.removeAll(toRemove);
        _totalFines = 0;

        _isActive = hasNoSuspensionFlags(currentDate);
    }

    /**
     * Adds a new work request for the user.
     * @param request the request to add
     */
    public void requestWork(Request request) {
        _activeRequests.add(request);
        _allRequests.addFirst(request);
    }

    /**
     * Returns a work request and updates fines and user state.
     * @param request the request to return
     * @param currentDate the current date
     */
    public void returnWork(Request request, int currentDate) {
        request.markAsReturned(currentDate);

        if (!request.shouldPayFine(currentDate)) {
            _activeRequests.remove(request);
        } else {
            int fine = request.calculateFine(currentDate);
            _totalFines += fine;
            _isActive = false;
        }

        updateState(currentDate);
    }

    /**
     * Disposes a request from active and all requests.
     * @param request the request to dispose
     */
    public void disposeRequest(Request request) {
        _activeRequests.remove(request);
        _allRequests.remove(request);
    }

    /**
     * Receives a notification.
     * @param notification the notification to add
     */
    @Override
    public void update(Notification notification) {
        _notifications.add(notification);
    }

    /**
     * Compares this user to another by name, then by id.
     * @param other the other user
     * @return comparison result
     */
    @Override
    public int compareTo(User other) {
        int nameComparison = _name.compareTo(other.getName());
        if (nameComparison != 0) {
            return nameComparison;
        }
        return Integer.compare(_id, other.getId());
    }

    /**
     * Checks equality by user id.
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof User user && _id == user._id;
    }

    /**
     * Returns a string representation of the user.
     * @return string representation
     */
    @Override
    public String toString() {
        if (_isActive)
            return String.format("%d - %s - %s - %s - ACTIVO", _id, _name, _email, _classification);
        else
            return String.format("%d - %s - %s - %s - SUSPENSO - EUR %d", _id, _name, _email, _classification, _totalFines);
    }

    /**
     * Counts the number of consecutive on-time returns up to n.
     * @param n maximum number to count
     * @param currentDate the current date
     * @return count of consecutive on-time returns
     */
    int countConsecutiveOnTimeReturns(int n, int currentDate) {
        int count = 0;
        for (Request request : _allRequests) {
            if (request.hasBeenReturned() && !request.wasOverdue() && !request.isOverdue(currentDate) && !request.shouldPayFine(currentDate) && count < n) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    /**
     * Counts the number of recent late returns up to n.
     * @param n maximum number to check
     * @return count of recent late returns
     */
    int countRecentLateReturns(int n) {
        int count = 0;
        int checked = 0;
        for (Request request : _allRequests) {
            if (checked == n) break;
            if (request.wasOverdue()) {
                count++;
            }
            checked++;
        }
        return count;
    }

    /**
     * Checks if the user has no suspension flags (no fines and no overdue active requests).
     * @param currentDate the current date
     * @return true if no suspension flags, false otherwise
     */
    private boolean hasNoSuspensionFlags(int currentDate) {
        if (_totalFines > 0) return false;

        for (Request request : _activeRequests) {
            if (request.isOverdue(currentDate)) {
                return false;
            }
        }
        return true;
    }
}