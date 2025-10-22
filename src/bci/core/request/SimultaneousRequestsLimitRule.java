package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

/**
 * Rule that enforces the maximum number of simultaneous requests a user can have.
 * Throws {@link RequestRuleFailedException} if the user has reached the limit.
 */
public class SimultaneousRequestsLimitRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111117L;

    /** Unique identifier for this rule. */
    public static final int RULE_ID = 4;

    /**
     * Constructs a new SimultaneousRequestsLimitRule.
     */
    public SimultaneousRequestsLimitRule() {
        super(RULE_ID);
    }

    /**
     * Checks if the user has reached the maximum number of simultaneous requests.
     *
     * @param user the user to check
     * @param work the work associated with the request
     * @throws RequestRuleFailedException if the user has reached the limit
     */
    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (user.getActiveRequests().size() == user.getMaxSimultaneousRequests()) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
