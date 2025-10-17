package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

public class NoDuplicateRequestsRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111114L;

    public static final int RULE_ID = 1;

    public NoDuplicateRequestsRule() {
        super(RULE_ID);
    }

    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        boolean hasActiveRequest = user.getActiveRequests()
                .stream()
                .anyMatch(request -> request.getWork().equals(work));
        if (hasActiveRequest) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
