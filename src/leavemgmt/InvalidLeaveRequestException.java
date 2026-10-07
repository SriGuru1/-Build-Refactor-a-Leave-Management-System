package leavemgmt;

/** The request itself is wrong: bad days, blank reason, over the type's limit, already cancelled... */
public class InvalidLeaveRequestException extends LeaveException {

    private static final long serialVersionUID = 1L;

    public InvalidLeaveRequestException(String message) {
        super(message);
    }
}
