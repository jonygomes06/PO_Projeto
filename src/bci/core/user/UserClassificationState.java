package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

abstract class UserClassificationState implements Serializable {
    @Serial
    private static final long serialVersionUID = 7147111111111111123L;

    public abstract UserClassificationState updateState(User user, int currentDate);
    public abstract int getMaxSimultaneousRequests();
    public abstract int getMaxWorkRequestPrice();
    public abstract int getRequestDuration(Work work);
    public abstract String toString();
}
