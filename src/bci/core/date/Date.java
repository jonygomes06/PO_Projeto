package bci.core.date;

import java.io.Serial;
import java.io.Serializable;

public class Date implements Serializable {
    @Serial
    private static final long serialVersionUID = 600671250616539002L;

    private int currentDate;

    public Date() {
        this.currentDate = 1;
    }

    public int getCurrentDate() {
        return currentDate;
    }

    public void advanceDate(int days) {
        if (days <= 0) return;
        currentDate += days;
    }
}