package bci.core.user;

import bci.core.work.Work;

class NormalState implements UserClassificationState {
    private static final NormalState NORMAL = new NormalState();

    private NormalState() {}

    public static NormalState getInstance() {
        return NORMAL;
    }

    @Override
    public UserClassificationState updateStateOnDateChange(User user, int currentDate) {
        // FIXME: implement state change logic
        return null;
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
}
