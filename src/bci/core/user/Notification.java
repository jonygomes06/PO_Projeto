package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = -2335739609784493516L;

    private final NotificationType _type;
    private final Work _associatedWork;

    public Notification(NotificationType type, Work associatedWork) {
        _type = type;
        _associatedWork = associatedWork;
    }

    @Override
    public String toString() {
        return _type + ":" + _associatedWork.toString();
    }
}
