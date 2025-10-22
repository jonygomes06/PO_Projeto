package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

/**
 * Rule that checks if a user is active before allowing a request.
 */
public class UserIsActiveRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111115L;

    /**
     * Unique identifier for this rule.
     */
    public static final int RULE_ID = 2;

    /**
     * Constructs a new UserIsActiveRule.
     */
    public UserIsActiveRule() {
        super(RULE_ID);
    }

    /**
     * Checks if the given user is active.
     *
     * @param user the user to check
     * @param work the work context
     * @throws RequestRuleFailedException if the user is not active
     */
    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (!user.isActive()) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
