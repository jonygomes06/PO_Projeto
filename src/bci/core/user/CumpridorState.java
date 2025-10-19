package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;

class CumpridorState extends UserClassificationState {
    @Serial
    private static final long serialVersionUID = 7147111111111111120L;

    private static final CumpridorState CUMPRIDOR = new CumpridorState();

    private CumpridorState() {}

    public static CumpridorState getInstance() {
        return CUMPRIDOR;
    }

    @Override
    public UserClassificationState updateState(User user, int currentDate) {
        if (user.countRecentLateReturns(5) > 0) {
            return NormalState.getInstance().updateState(user, currentDate);
        }

        return this;
    }

    @Override
    public int getMaxSimultaneousRequests() {
        return 5;
    }

    @Override
    public int getMaxWorkRequestPrice() {
        return Integer.MAX_VALUE;
    }

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

    @Override
    public String toString() {
        return "CUMPRIDOR";
    }
}
