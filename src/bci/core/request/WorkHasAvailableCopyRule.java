package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

public class WorkHasAvailableCopyRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111116L;

    public static final int RULE_ID = 3;

    public WorkHasAvailableCopyRule() {
        super(RULE_ID);
    }

    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (work.getAvailableCopies() == 0) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
