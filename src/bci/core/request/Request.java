package bci.core.request;

import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a request for a work by a user, including deadline and fine management.
 */
public class Request implements Serializable {

    @Serial
    private static final long serialVersionUID = 7147111111111111111L;

    /** Fine amount per overdue day in euros. */
    private static final byte INCREMENTAL_FINE_EUROS = 5;

    private final int _id;
    private final User _user;
    private final Work _work;
    private final int _deadline;
    private int _returnDate;
    private boolean _fineLiquidated;

    /**
     * Constructs a new Request.
     *
     * @param id       the unique identifier of the request
     * @param user     the user making the request
     * @param work     the work being requested
     * @param deadline the deadline for returning the work
     */
    public Request(int id, User user, Work work, int deadline) {
        _id = id;
        _user = user;
        _work = work;
        _deadline = deadline;
        _returnDate = -1;       // -1 means not yet returned
        _fineLiquidated = false;
    }

    /**
     * Gets the request ID.
     *
     * @return the request ID
     */
    public int getId() {
        return _id;
    }

    /**
     * Gets the user who made the request.
     *
     * @return the user
     */
    public User getUser() {
        return _user;
    }

    /**
     * Gets the work associated with the request.
     *
     * @return the work
     */
    public Work getWork() {
        return _work;
    }

    /**
     * Checks if the work has been returned.
     *
     * @return true if returned, false otherwise
     */
    public boolean hasBeenReturned() {
        return _returnDate != -1;
    }

    /**
     * Marks the work as returned on the given date.
     *
     * @param returnDate the date the work was returned
     */
    public void markAsReturned(int returnDate) {
        _returnDate = returnDate;
    }

    /**
     * Marks the fine as liquidated.
     */
    public void liquidateFine() {
        _fineLiquidated = true;
    }

    /**
     * Checks if the request is overdue on the given date.
     *
     * @param currentDate the current date
     * @return true if overdue, false otherwise
     */
    public boolean isOverdue(int currentDate) {
        return !hasBeenReturned() && currentDate > _deadline;
    }

    /**
     * Checks if the work was returned overdue.
     *
     * @return true if returned after the deadline, false otherwise
     */
    public boolean wasOverdue() {
        return hasBeenReturned() && _returnDate > _deadline;
    }

    /**
     * Checks if the fine should be paid on the given date.
     *
     * @param currentDate the current date
     * @return true if fine should be paid, false otherwise
     */
    public boolean shouldPayFine(int currentDate) {
        return hasBeenReturned() && currentDate > _deadline && !_fineLiquidated;
    }

    /**
     * Calculates the fine amount based on the current date.
     *
     * @param currentDate the current date
     * @return the fine amount in euros
     */
    public int calculateFine(int currentDate) {
        if (shouldPayFine(currentDate)) {
            int daysOverdue = _returnDate - _deadline;
            return daysOverdue * INCREMENTAL_FINE_EUROS;
        }
        return 0;
    }
}