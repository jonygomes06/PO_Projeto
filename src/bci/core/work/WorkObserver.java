package bci.core.work;

import bci.core.user.Notification;

import java.io.Serial;
import java.io.Serializable;

public abstract class WorkObserver implements Serializable {
    @Serial
    private static final long serialVersionUID = 7147111111111111124L;

    public abstract void update(Notification notification);
}
