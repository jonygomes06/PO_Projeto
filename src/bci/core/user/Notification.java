package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a notification related to a specific work item and type.
 * Implements Serializable for object serialization.
 */
public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = -2335739609784493516L;

    /**
     * The type of the notification.
     */
    private final NotificationType type;

    /**
     * The message describing the notification.
     */
    private final String _message;

    /**
     * Constructs a Notification with the specified type and associated work.
     *
     * @param type the type of the notification
     * @param associatedWork the work item associated with the notification
     */
    public Notification(NotificationType type, Work associatedWork) {
        this.type = type;
        _message = String.format("%s: %s", type.toString(), associatedWork.toString());
    }

    /**
     * Returns the type of the notification.
     *
     * @return the notification type
     */
    public NotificationType getType() {
        return type;
    }

    /**
     * Returns the string representation of the notification message.
     *
     * @return the notification message
     */
    @Override
    public String toString() {
        return _message;
    }
}
