package bci.core.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class User implements Comparable<User>, Serializable {

    @Serial
    private static final long serialVersionUID = 7847658452022990947L;

    private static int _nextId = 0;

    private final int _id;
    private final String _name;
    private final String _email;
    private boolean _isActive;
    private UserClassification _classification;
    private List<Notification> _notifications;

    public User(String name, String email) {
        _id = _nextId++;
        _name = name;
        _email = email;
        _isActive = true;
        _classification = UserClassification.NORMAL;
        _notifications = new ArrayList<>();
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

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(_notifications);
    }

    @Override
    public int compareTo(User other) {
        int nameComparison = _name.compareTo(other.getName());
        if (nameComparison != 0) {
            return nameComparison;
        }
        return Integer.compare(_id, other.getId());
    }
}
