package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

public class Notification implements Serializable {
    @Serial
    private static final long serialVersionUID = -2335739609784493516L;

    private final String _message;

    public Notification(NotificationType type, Work associatedWork) {
        _message = String.format("%s: %s", type.toString(), associatedWork.toString());
    }

    @Override
    public String toString() {
        return _message;
    }
}
