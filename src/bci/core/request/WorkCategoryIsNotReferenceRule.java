package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;
import bci.core.work.WorkCategory;

import java.io.Serial;

/**
 * Rule that checks if the category of a {@link Work} is not {@link WorkCategory#REFERENCE}.
 * Throws {@link RequestRuleFailedException} if the category is REFERENCE.
 */
public class WorkCategoryIsNotReferenceRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111118L;

    /**
     * Unique identifier for this rule.
     */
    public static final int RULE_ID = 5;

    /**
     * Constructs the rule with its unique identifier.
     */
    public WorkCategoryIsNotReferenceRule() {
        super(RULE_ID);
    }

    /**
     * Checks if the {@link Work} category is not REFERENCE.
     *
     * @param user the user making the request
     * @param work the work to check
     * @throws RequestRuleFailedException if the work category is REFERENCE
     */
    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (work.getCategory() == WorkCategory.REFERENCE) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
