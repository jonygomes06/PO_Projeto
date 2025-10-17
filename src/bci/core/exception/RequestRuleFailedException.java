package bci.core.exception;

import java.io.Serial;

public class RequestRuleFailedException extends Exception {
    @Serial
    private static final long serialVersionUID = 7147111111111111112L;

    public RequestRuleFailedException(int userId, int workId, int ruleId) {
        super("Borrowing rule " + ruleId + " failing for user " + userId + " and work " + workId);
    }
}
