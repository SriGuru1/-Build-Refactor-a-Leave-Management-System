package leavemgmt;

import java.time.LocalDate;

/** One leave application. Only its status can change after creation. */
public class LeaveRequest {

    private final int requestId;
    private final int days;
    private final String reason;
    private final LocalDate requestedOn;
    private LeaveStatus status;

    public LeaveRequest(int requestId, int days, String reason) {
        this.requestId = requestId;
        this.days = days;
        this.reason = reason;
        this.requestedOn = LocalDate.now();
        this.status = LeaveStatus.APPROVED;
    }

    public int getRequestId()        { return requestId; }
    public int getDays()             { return days; }
    public String getReason()        { return reason; }
    public LocalDate getRequestedOn(){ return requestedOn; }
    public LeaveStatus getStatus()   { return status; }

    public void cancel() {
        status = LeaveStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "#" + requestId + " | " + days + " day(s) | " + reason + " | " + status;
    }
}
