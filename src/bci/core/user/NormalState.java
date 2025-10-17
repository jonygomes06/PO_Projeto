package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;

class NormalState extends UserClassificationState {
    @Serial
    private static final long serialVersionUID = 7147111111111111122L;

    private static final NormalState NORMAL = new NormalState();

    private NormalState() {}

    public static NormalState getInstance() {
        return NORMAL;
    }

    @Override
    public UserClassificationState updateState(User user, int currentDate) {
        if (user.countRecentLateReturns(3, currentDate) == 3) {
            return FaltosoState.getInstance();
        } else if (user.countConsecutiveOnTimeReturns(5, currentDate) == 5) {
            return CumpridorState.getInstance();
        }

        return this;
    }

    @Override
    public int getMaxSimultaneousRequests() {
        return 3;
    }

    @Override
    public int getMaxWorkRequestPrice() {
        return 25;
    }

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

    @Override
    public String toString() {
        return "NORMAL";
    }
}
