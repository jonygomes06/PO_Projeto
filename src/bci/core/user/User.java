package bci.core.user;

import bci.core.request.Request;
import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class User implements Comparable<User>, Serializable {

    @Serial
    private static final long serialVersionUID = 7147658457022990947L;

    private final int _id;
    private final String _name;
    private final String _email;
    private boolean _isActive;
    private UserClassificationState _classification;
    private final List<Request> _activeRequests;
    private final List<Request> _archivedRequests;
    private final List<Notification> _notifications;
    private int _totalFines;

    public User(int id, String name, String email) {
        _id = id;
        _name = name;
        _email = email;
        _isActive = true;
        _classification = NormalState.getInstance();
        _activeRequests = new ArrayList<>();
        _archivedRequests = new LinkedList<>();
        _notifications = new ArrayList<>();
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

    public void requestWork(Request request) {
        _activeRequests.add(request);
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
}
