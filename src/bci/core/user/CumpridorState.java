package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;

/**
 * Represents the state of a user classified as "Cumpridor".
 * This state allows up to 5 simultaneous requests and unlimited request price.
 * The request duration depends on the total number of copies of the work.
 */
class CumpridorState extends UserClassificationState {
    @Serial
    private static final long serialVersionUID = 7147111111111111120L;

    /** Singleton instance of CumpridorState. */
    private static final CumpridorState CUMPRIDOR = new CumpridorState();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private CumpridorState() {}

    /**
     * Returns the singleton instance of CumpridorState.
     *
     * @return the singleton instance
     */
    public static CumpridorState getInstance() {
        return CUMPRIDOR;
    }

    /**
     * Updates the user's classification state based on recent late returns.
     * If the user has any recent late returns, transitions to NormalState.
     *
     * @param user the user to evaluate
     * @param currentDate the current date
     * @return the updated UserClassificationState
     */
    @Override
    public UserClassificationState updateState(User user, int currentDate) {
        if (user.countRecentLateReturns(5) > 0) {
            return NormalState.getInstance().updateState(user, currentDate);
        }

        return this;
    }

    /**
     * Returns the maximum number of simultaneous requests allowed for this state.
     *
     * @return the maximum simultaneous requests (5)
     */
    @Override
    public int getMaxSimultaneousRequests() {
        return 5;
    }

    /**
     * Returns the maximum price allowed for a work request in this state.
     *
     * @return the maximum price (Integer.MAX_VALUE)
     */
    @Override
    public int getMaxWorkRequestPrice() {
        return Integer.MAX_VALUE;
    }

    /**
     * Returns the allowed request duration for a given work, based on its total copies.
     *
     * @param work the work to evaluate
     * @return the request duration in days
     */
    @Override
    public int getRequestDuration(Work work) {
        int totalCopies = work.getTotalCopies();
        if (totalCopies == 1) {
            return 8;
        } else if (totalCopies <= 5) {
            return 15;
        } else {
            return 30;
        }
    }

    /**
     * Returns the string representation of this state.
     *
     * @return "CUMPRIDOR"
     */
    @Override
    public String toString() {
        return "CUMPRIDOR";
    }
}
