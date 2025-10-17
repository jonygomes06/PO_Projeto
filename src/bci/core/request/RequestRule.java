package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

public abstract class RequestRule implements Serializable {
    @Serial
    private static final long serialVersionUID = 7147111111111111113L;

    private final int _id;

    protected RequestRule(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public abstract void check(User user, Work work) throws RequestRuleFailedException;
}
