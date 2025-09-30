package bci.core.date;

/**
 * A simple class to represent and manipulate a date as an integer value.
 */
public class Date {
    private int currentDate; // Stores the current date as an integer.

    /**
     * Default constructor that initializes the date to 0.
     */
    public Date() {
        this.currentDate = 0;
    }

    /**
     * Retrieves the current date.
     *
     * @return the current date.
     */
    public int getCurrentDate() {
        return currentDate;
    }

    /**
     * Advances the current date by a specified number of days.
     *
     * @param days the number of days to advance the date by.
     */
    public void advanceDate(int days) {
        if (days > 0) currentDate += days;
    }
}