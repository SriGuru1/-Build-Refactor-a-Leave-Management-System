package leavemgmt;

/** Base class of every business-rule failure in the leave system (checked on purpose). */
public class LeaveException extends Exception {

    private static final long serialVersionUID = 1L;

    public LeaveException(String message) {
        super(message);
    }
}
