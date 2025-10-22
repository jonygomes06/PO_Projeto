package bci.core;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents a simple date tracker.
 * Stores the current date as an integer and allows advancing the date.
 */
public class Date implements Serializable {
    @Serial
    private static final long serialVersionUID = 600671250616539002L;

    /**
     * The current date represented as an integer.
     */
    private int currentDate;

    /**
     * Constructs a new Date with the initial value set to 1.
     */
    public Date() {
        this.currentDate = 1;
    }

    /**
     * Returns the current date.
     *
     * @return the current date as an integer
     */
    public int getCurrentDate() {
        return currentDate;
    }

    /**
     * Advances the current date by the specified number of days.
     *
     * @param days the number of days to advance
     */
    public void advanceDate(int days) {
        currentDate += days;
    }
}