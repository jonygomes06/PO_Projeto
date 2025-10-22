package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

/**
 * Rule that checks if a {@link Work} has at least one available copy.
 * Throws {@link RequestRuleFailedException} if no copies are available.
 */
public class WorkHasAvailableCopyRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111116L;

    /**
     * Unique identifier for this rule.
     */
    public static final int RULE_ID = 3;

    /**
     * Constructs the rule with its unique identifier.
     */
    public WorkHasAvailableCopyRule() {
        super(RULE_ID);
    }

    /**
     * Checks if the given work has available copies.
     *
     * @param user the user making the request
     * @param work the work to check for available copies
     * @throws RequestRuleFailedException if no copies are available
     */
    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (work.getAvailableCopies() == 0) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
