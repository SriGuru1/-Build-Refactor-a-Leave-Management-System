package leavemgmt;

/** The employee asked for more days than are left. Carries the numbers so callers can react. */
public class InsufficientLeaveBalanceException extends LeaveException {

    private static final long serialVersionUID = 1L;

    private final int requested;
    private final int available;

    public InsufficientLeaveBalanceException(int requested, int available) {
        super("Not enough leave: requested " + requested + " day(s) but only " + available + " available.");
        this.requested = requested;
        this.available = available;
    }

    public int getRequested() { return requested; }
    public int getAvailable() { return available; }
    public int getShortfall() { return requested - available; }
}
