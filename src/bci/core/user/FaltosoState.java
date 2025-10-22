package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;

/**
 * Represents the "Faltoso" state in user classification.
 * Users in this state have limited privileges due to previous infractions.
 */
class FaltosoState extends UserClassificationState {
    @Serial
    private static final long serialVersionUID = 7147111111111111121L;

    /** Singleton instance of FaltosoState. */
    private static final FaltosoState FALTOSO = new FaltosoState();

    /** Private constructor to enforce singleton pattern. */
    private FaltosoState() {}

    /**
     * Returns the singleton instance of FaltosoState.
     * @return FaltosoState instance
     */
    public static FaltosoState getInstance() {
        return FALTOSO;
    }

    /**
     * Updates the user's classification state based on their recent activity.
     * If the user has 3 consecutive on-time returns, transitions to NormalState.
     * @param user the user to evaluate
     * @param currentDate the current date
     * @return the updated UserClassificationState
     */
    @Override
    public UserClassificationState updateState(User user, int currentDate) {
        if (user.countConsecutiveOnTimeReturns(3, currentDate) == 3) {
            return NormalState.getInstance().updateState(user, currentDate);
        }

        return this;
    }

    /**
     * Returns the maximum number of simultaneous requests allowed for users in this state.
     * @return maximum simultaneous requests (1)
     */
    @Override
    public int getMaxSimultaneousRequests() {
        return 1;
    }

    /**
     * Returns the maximum price for a work request for users in this state.
     * @return maximum work request price (25)
     */
    @Override
    public int getMaxWorkRequestPrice() {
        return 25;
    }

    /**
     * Returns the duration of a work request for users in this state.
     * @param work the work being requested
     * @return request duration (2)
     */
    @Override
    public int getRequestDuration(Work work) {
        return 2;
    }

    /**
     * Returns the string representation of this state.
     * @return "FALTOSO"
     */
    @Override
    public String toString() {
        return "FALTOSO";
    }
}
