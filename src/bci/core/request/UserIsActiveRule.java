package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

public class UserIsActiveRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111115L;

    public static final int RULE_ID = 2;

    public UserIsActiveRule() {
        super(RULE_ID);
    }

    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (!user.isActive()) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
