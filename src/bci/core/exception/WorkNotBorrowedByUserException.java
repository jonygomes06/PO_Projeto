package bci.core.exception;

import java.io.Serial;

public class WorkNotBorrowedByUserException extends Exception {
    @Serial
    private static final long serialVersionUID = 209909000003L;

    private final int _userId;
    private final int _workId;

    public WorkNotBorrowedByUserException(int idWork, int idUser) {
        super("Work with id " + idWork + " is not borrowed by user with id " + idUser);
        _workId = idWork;
        _userId = idUser;
    }

    public int getUserId() {
        return _userId;
    }

    public int getWorkId() {
        return _workId;
    }
}
