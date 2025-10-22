package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;

/**
 * Represents the normal classification state of a user.
 * Implements singleton pattern.
 */
class NormalState extends UserClassificationState {
    @Serial
    private static final long serialVersionUID = 7147111111111111122L;

    /** Singleton instance of NormalState. */
    private static final NormalState NORMAL = new NormalState();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private NormalState() {}

    /**
     * Returns the singleton instance of NormalState.
     *
     * @return NormalState instance
     */
    public static NormalState getInstance() {
        return NORMAL;
    }

    /**
     * Updates the user's classification state based on their recent activity.
     *
     * @param user the user whose state is being updated
     * @param currentDate the current date
     * @return the new UserClassificationState
     */
    @Override
    public UserClassificationState updateState(User user, int currentDate) {
        if (user.countRecentLateReturns(3) == 3) {
            return FaltosoState.getInstance();
        } else if (user.countConsecutiveOnTimeReturns(5, currentDate) == 5) {
            return CumpridorState.getInstance();
        }

        return this;
    }

    /**
     * Returns the maximum number of simultaneous requests allowed for a user in the normal state.
     *
     * @return maximum simultaneous requests
     */
    @Override
    public int getMaxSimultaneousRequests() {
        return 3;
    }

    /**
     * Returns the maximum price for a work request for a user in the normal state.
     *
     * @return maximum work request price
     */
    @Override
    public int getMaxWorkRequestPrice() {
        return 25;
    }

    /**
     * Returns the duration of a work request based on the total number of copies.
     *
     * @param work the work being requested
     * @return request duration in days
     */
    @Override
    public int getRequestDuration(Work work) {
        int totalCopies = work.getTotalCopies();
        if (totalCopies == 1) {
            return 3;
        } else if (totalCopies <= 5) {
            return 8;
        } else {
            return 15;
        }
    }

    /**
     * Returns a string representation of the state.
     *
     * @return "NORMAL"
     */
    @Override
    public String toString() {
        return "NORMAL";
    }
}
