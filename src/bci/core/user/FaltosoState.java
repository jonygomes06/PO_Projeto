package bci.core.user;

import bci.core.work.Work;

import java.io.Serial;

class FaltosoState extends UserClassificationState {
    @Serial
    private static final long serialVersionUID = 7147111111111111121L;

    private static final FaltosoState FALTOSO = new FaltosoState();

    private FaltosoState() {}

    public static FaltosoState getInstance() {
        return FALTOSO;
    }

    @Override
    public UserClassificationState updateState(User user, int currentDate) {
        if (user.countConsecutiveOnTimeReturns(3, currentDate) == 3) {
            return NormalState.getInstance().updateState(user, currentDate);
        }

        return this;
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

    @Override
    public String toString() {
        return "FALTOSO";
    }
}
