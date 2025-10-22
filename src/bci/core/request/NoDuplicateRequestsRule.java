package bci.core.request;

import bci.core.exception.RequestRuleFailedException;
import bci.core.user.User;
import bci.core.work.Work;

import java.io.Serial;

/**
 * Rule that prevents users from making duplicate requests for the same work.
 * <p>
 * This rule checks if the user already has an active request for the specified work.
 * If a duplicate is found, a {@link RequestRuleFailedException} is thrown.
 */
public class NoDuplicateRequestsRule extends RequestRule {
    @Serial
    private static final long serialVersionUID = 7147111111111111114L;

    /**
     * Unique identifier for this rule.
     */
    public static final int RULE_ID = 1;

    /**
     * Constructs a new NoDuplicateRequestsRule.
     */
    public NoDuplicateRequestsRule() {
        super(RULE_ID);
    }

    /**
     * Checks if the user has already made a request for the given work.
     *
     * @param user the user making the request
     * @param work the work being requested
     * @throws RequestRuleFailedException if a duplicate request is found
     */
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
