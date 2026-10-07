package leavemgmt;

import java.time.LocalDate;

/** One leave application. Everything is fixed at creation except the status. */
public class LeaveRequest {

    private final int requestId;
    private final int leaveYear;
    private final int days;
    private final String reason;
    private final LocalDate requestedOn;
    private LeaveStatus status;

    LeaveRequest(int requestId, int leaveYear, int days, String reason) {   // package-private: only Employee creates requests
        this.requestId = requestId;
        this.leaveYear = leaveYear;
        this.days = days;
        this.reason = reason;
        this.requestedOn = LocalDate.now();
        this.status = LeaveStatus.APPROVED;
    }

    public int getRequestId()         { return requestId; }
    public int getLeaveYear()         { return leaveYear; }
    public int getDays()              { return days; }
    public String getReason()         { return reason; }
    public LocalDate getRequestedOn() { return requestedOn; }
    public LeaveStatus getStatus()    { return status; }

    void cancel() {                                                          // package-private: only Employee may cancel
        status = LeaveStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "#" + requestId + " | " + days + " day(s) | " + reason + " | " + status;
    }
}
