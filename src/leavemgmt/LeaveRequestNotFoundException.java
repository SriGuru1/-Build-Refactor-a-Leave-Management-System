package leavemgmt;

/** No request with the given id exists for this employee. */
public class LeaveRequestNotFoundException extends LeaveException {

    private static final long serialVersionUID = 1L;

    public LeaveRequestNotFoundException(int requestId) {
        super("No leave request with id " + requestId + ".");
    }
}
