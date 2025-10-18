package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = -2335739609784493516L;

    private final NotificationType type;
    private final String _message;

    public Notification(NotificationType type, Work associatedWork) {
        this.type = type;
        _message = String.format("%s: %s", type.toString(), associatedWork.toString());
    }

    public NotificationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return _message;
    }
}
