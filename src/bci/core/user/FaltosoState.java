package bci.core.user;

import bci.core.work.Work;

class FaltosoState implements UserClassificationState {
    private static final FaltosoState FALTOSO = new FaltosoState();

    private FaltosoState() {}

    public static FaltosoState getInstance() {
        return FALTOSO;
    }

    @Override
    public UserClassificationState updateStateOnDateChange(User user, int currentDate) {
        // FIXME: implement state change logic
        return null;
    }

    @Override
    public int getMaxSimultaneousRequests() {
        return 1;
    }

    @Override
    public int getMaxWorkRequestPrice() {
        return 25;
    }

    @Override
    public int getRequestDuration(Work work) {
        return 2;
    }
}
