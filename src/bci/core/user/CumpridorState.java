package bci.core.user;

import bci.core.work.Work;

class CumpridorState implements UserClassificationState {
    private static final CumpridorState CUMPRIDOR = new CumpridorState();

    private CumpridorState() {}

    public static CumpridorState getInstance() {
        return CUMPRIDOR;
    }

    @Override
    public UserClassificationState updateStateOnDateChange(User user, int currentDate) {
        // FIXME: implement state change logic
        return null;
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
}
