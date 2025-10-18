package bci.core.user;

import bci.core.request.Request;
import bci.core.work.Work;
import bci.core.work.WorkObserver;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

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

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public boolean isActive() {
        return _isActive;
    }

    public int getMaxSimultaneousRequests() {
        return _classification.getMaxSimultaneousRequests();
    }

    public int getMaxWorkRequestPrice() {
        return _classification.getMaxWorkRequestPrice();
    }

    public int getRequestDuration(Work work) {
        return _classification.getRequestDuration(work);
    }

    public Collection<Request> getActiveRequests() {
        return Collections.unmodifiableList(_activeRequests);
    }

    public Collection<Notification> getNotifications() {
        List<Notification> notificationsCopy = new ArrayList<>(_notifications);
        _notifications.clear();
        return notificationsCopy;
    }

    public void updateState(int currentDate) {
        calculateTotalFines(currentDate);
        _isActive = _totalFines == 0;
        _classification = _classification.updateState(this, currentDate);
    }

    public void payFine(int currentDate) {
        List<Request> toRemove = new ArrayList<>();
        boolean hasOverdueReturnsLeft = false;
        for (Request request : _activeRequests) {
            if (request.hasBeenReturned()) {
                _totalFines -= request.calculateFine(currentDate);
                request.liquidateFine();
                toRemove.add(request);
            } else if (!_isActive && !hasOverdueReturnsLeft && request.shouldPayFine(currentDate)) {
                hasOverdueReturnsLeft = true;
            }
        }
        _activeRequests.removeAll(toRemove);

        if (!hasOverdueReturnsLeft) {
            _isActive = true;
        }
    }

    public void requestWork(Request request) {
        _activeRequests.add(request);
        _allRequests.addFirst(request);
    }

    public void returnWork(Request request, int currentDate) {
        request.markAsReturned(currentDate);

        if (!request.shouldPayFine(currentDate)) {
            _activeRequests.remove(request);
        }

        updateState(currentDate);
    }

    public void disposeRequest(Request request) {
        _activeRequests.remove(request);
        _allRequests.remove(request);
    }

    @Override
    public void update(Notification notification) {
        _notifications.add(notification);
    }

    @Override
    public int compareTo(User other) {
        int nameComparison = _name.compareTo(other.getName());
        if (nameComparison != 0) {
            return nameComparison;
        }
        return Integer.compare(_id, other.getId());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User user && _id == user._id;
    }

    @Override
    public String toString() {
        if (_isActive)
            return String.format("%d - %s - %s - %s - ACTIVO", _id, _name, _email, _classification);
        else
            return String.format("%d - %s - %s - %s - SUSPENSO - EUR %d", _id, _name, _email, _classification, _totalFines);
    }

    private void calculateTotalFines(int currentDate) {
        _totalFines = 0;
        for (Request request : _activeRequests) {
            _totalFines += request.calculateFine(currentDate);
        }
    }

    int countConsecutiveOnTimeReturns(int n, int currentDate) {
        int count = 0;
        for (Request request : _allRequests) {
            if (request.hasBeenReturned() && !request.shouldPayFine(currentDate) && count < n) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    int countRecentLateReturns(int n, int currentDate) {
        int count = 0;
        int checked = 0;
        for (Request request : _allRequests) {
            if (checked == n) break;
            if (request.shouldPayFine(currentDate)) {
                count++;
            }
            checked++;
        }
        return count;
    }
}
