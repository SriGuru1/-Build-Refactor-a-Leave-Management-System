package leavemgmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract base class of every employee.
 *
 * Template Method pattern: applyLeave() contains the ONE shared workflow
 * (validate -> check limit -> check balance -> deduct -> record). The rules that
 * differ by employee type are supplied by subclasses through two small hooks:
 * getMaxDaysPerRequest() and getCarryForwardLimit().
 *
 * This class never prints business results; it returns values or throws LeaveException.
 */
public abstract class Employee {

    public static final String DEFAULT_REASON = "Personal leave";

    private final String employeeId;
    private final String name;
    private final String department;
    private final EmployeeType type;
    private final int annualEntitlement;

    private int leaveBalance;
    private int leaveYear = 1;
    private final List<LeaveRequest> history = new ArrayList<>();

    protected Employee(String employeeId, String name, String department, EmployeeType type, int annualEntitlement) {
        this.employeeId = requireText(employeeId, "Employee ID");
        this.name = requireText(name, "Employee name");
        this.department = requireText(department, "Department");
        if (type == null) {
            throw new IllegalArgumentException("Employee type is required.");
        }
        if (annualEntitlement <= 0) {
            throw new IllegalArgumentException("Annual entitlement must be positive.");
        }
        this.type = type;
        this.annualEntitlement = annualEntitlement;
        this.leaveBalance = annualEntitlement;
    }

    // ---------------- hooks: the rules that differ per employee type ----------------

    /** Largest number of days allowed in ONE request. */
    public abstract int getMaxDaysPerRequest();

    /** Largest number of unused days that may roll over into the next leave year. */
    public abstract int getCarryForwardLimit();

    /** Optional extra line shown in displayDetails() (e.g. manager benefits). Empty by default. */
    protected String getBenefitNote() {
        return "";
    }

    // ---------------- leave operations ----------------

    /** Overload 1: reason not given. */
    public LeaveRequest applyLeave(int days) throws LeaveException {
        return applyLeave(days, DEFAULT_REASON);
    }

    /** The shared workflow (template method). Subclasses cannot change the order of the steps. */
    public final LeaveRequest applyLeave(int days, String reason) throws LeaveException {
        if (days <= 0) {
            throw new InvalidLeaveRequestException("Number of days must be at least 1 (got " + days + ").");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidLeaveRequestException("A reason is required.");
        }
        if (days > getMaxDaysPerRequest()) {
            throw new InvalidLeaveRequestException(type.getDisplayName() + " employees can take at most "
                    + getMaxDaysPerRequest() + " days per request (requested " + days + ").");
        }
        if (days > leaveBalance) {
            throw new InsufficientLeaveBalanceException(days, leaveBalance);
        }
        leaveBalance -= days;
        LeaveRequest request = new LeaveRequest(history.size() + 1, leaveYear, days, reason.trim());
        history.add(request);
        return request;
    }

    /** Overload 2: dates instead of a day count; weekends are not counted. */
    public LeaveRequest applyLeave(LocalDate from, LocalDate to, String reason) throws LeaveException {
        int days = WorkingDayCalculator.countWorkingDays(from, to);
        if (days == 0) {
            throw new InvalidLeaveRequestException("The selected dates contain no working days.");
        }
        return applyLeave(days, reason);
    }

    public LeaveRequest cancelLeave(int requestId) throws LeaveException {
        LeaveRequest request = findRequest(requestId);
        if (request.getStatus() == LeaveStatus.CANCELLED) {
            throw new InvalidLeaveRequestException("Request #" + requestId + " is already cancelled.");
        }
        if (request.getLeaveYear() != leaveYear) {
            throw new InvalidLeaveRequestException("Request #" + requestId + " belongs to a closed leave year.");
        }
        request.cancel();
        leaveBalance += request.getDays();
        return request;
    }

    /** Starts a new leave year and returns how many days were carried forward. */
    public int startNewLeaveYear() {
        int carried = Math.min(leaveBalance, getCarryForwardLimit());
        leaveBalance = annualEntitlement + carried;
        leaveYear++;
        return carried;
    }

    public int checkLeaveBalance() {
        return leaveBalance;
    }

    // ---------------- display ----------------

    /** Builds the text shown by displayDetails(); also easy to unit-test. */
    public String getDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ID: %s | Name: %s | Dept: %s | Type: %s | Leave balance: %d",
                employeeId, name, department, type.getDisplayName(), leaveBalance));
        sb.append(String.format("%n   Rule: max %d days per request, entitlement %d days/year, carry-forward up to %d days",
                getMaxDaysPerRequest(), annualEntitlement, getCarryForwardLimit()));
        String note = getBenefitNote();
        if (!note.isEmpty()) {
            sb.append(String.format("%n   %s", note));
        }
        return sb.toString();
    }

    public void displayDetails() {
        System.out.println(getDetails());
    }

    // ---------------- getters ----------------

    public String getEmployeeId()        { return employeeId; }
    public String getName()              { return name; }
    public String getDepartment()        { return department; }
    public EmployeeType getType()        { return type; }
    public int getAnnualEntitlement()    { return annualEntitlement; }
    public int getLeaveYear()            { return leaveYear; }

    /** Read-only view: callers cannot add or remove requests behind Employee's back. */
    public List<LeaveRequest> getHistory() {
        return Collections.unmodifiableList(history);
    }

    // ---------------- private helpers ----------------

    private LeaveRequest findRequest(int requestId) throws LeaveRequestNotFoundException {
        for (LeaveRequest request : history) {
            if (request.getRequestId() == requestId) {
                return request;
            }
        }
        throw new LeaveRequestNotFoundException(requestId);
    }

    private static String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " cannot be blank.");
        }
        return value.trim();
    }
}
