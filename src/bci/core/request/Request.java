package bci.core.request;

import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 7147111111111111111L;

    private static final byte INCREMENTAL_FINE_EUROS = 5;

    private final int _id;
    private final User _user;
    private final Work _work;
    private final int _deadline;
    private int _returnDate;
    private boolean _fineLiquidated;

    public Request(int id, User user, Work work, int deadline) {
        _id = id;
        _user = user;
        _work = work;
        _deadline = deadline;
        _returnDate = -1;       // -1 means not yet returned
        _fineLiquidated = false;
    }

    public int getId() {
        return _id;
    }

    public User getUser() {
        return _user;
    }

    public Work getWork() {
        return _work;
    }

    public int calculateFine(int currentDate) {
        if (!shouldPayFine(currentDate)) {
            return 0;
        }
        int daysOverdue = _returnDate == -1 ? currentDate - _deadline : _returnDate - _deadline;
        return daysOverdue * INCREMENTAL_FINE_EUROS;
    }

    private boolean shouldPayFine(int currentDate) {
        return currentDate > _deadline && !_fineLiquidated;
    }

    private boolean isActive(int currentDate) {
        return _returnDate == -1 || shouldPayFine(currentDate);
    }
}
