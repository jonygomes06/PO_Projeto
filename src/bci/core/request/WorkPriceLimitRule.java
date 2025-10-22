package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

/**
 * Rule that checks if the price of a work request exceeds the user's maximum allowed price.
 */
public class WorkPriceLimitRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111119L;

    /**
     * Unique identifier for this rule.
     */
    public static final int RULE_ID = 6;

    /**
     * Constructs a new WorkPriceLimitRule.
     */
    public WorkPriceLimitRule() {
        super(RULE_ID);
    }

    /**
     * Checks if the work price exceeds the user's maximum allowed work request price.
     *
     * @param user the user making the request
     * @param work the work being requested
     * @throws RequestRuleFailedException if the work price exceeds the user's limit
     */
    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (work.getPrice() > user.getMaxWorkRequestPrice()) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
