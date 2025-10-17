package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;
import bci.core.work.WorkCategory;

import java.io.Serial;

public class WorkCategoryIsNotReferenceRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111118L;

    public static final int RULE_ID = 5;

    public WorkCategoryIsNotReferenceRule() {
        super(RULE_ID);
    }

    @Override
    public void check(User user, Work work) throws RequestRuleFailedException {
        if (work.getCategory() == WorkCategory.REFERENCE) {
            throw new RequestRuleFailedException(user.getId(), work.getId(), this.getId());
        }
    }
}
