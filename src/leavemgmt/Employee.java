package leavemgmt;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Abstract base class: shared data and workflow. Each employee type supplies its own carry-forward rule. */
public abstract class Employee {

    private String employeeId;
    private String name;
    private String department;
    private int leaveBalance;
    private int annualEntitlement;
    private String employeeType;
    private List<LeaveRequest> history = new ArrayList<>();

    protected Employee(String employeeId, String name, String department, int annualEntitlement, String employeeType) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.annualEntitlement = annualEntitlement;
        this.leaveBalance = annualEntitlement;
        this.employeeType = employeeType;
    }

    public boolean applyLeave(int days, String reason) {
        if (days <= 0) {
            System.out.println(name + ": invalid number of days.");
            return false;
        }
        if (reason == null || reason.trim().isEmpty()) {
            System.out.println(name + ": a reason is required.");
            return false;
        }
        if (days > leaveBalance) {
            System.out.println(name + ": not enough leave (balance " + leaveBalance + ", requested " + days + ").");
            return false;
        }
        leaveBalance -= days;
        LeaveRequest request = new LeaveRequest(history.size() + 1, days, reason.trim());
        history.add(request);
        System.out.println(name + ": request " + request + " approved. Balance = " + leaveBalance);
        return true;
    }

    /** Overload 1: no reason given. */
    public boolean applyLeave(int days) {
        return applyLeave(days, "Personal leave");
    }

    /** Overload 2: from/to dates; only Monday-Friday are counted. */
    public boolean applyLeave(LocalDate from, LocalDate to, String reason) {
        if (from == null || to == null || to.isBefore(from)) {
            System.out.println(name + ": invalid date range.");
            return false;
        }
        int days = 0;
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            if (d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY) {
                days++;
            }
        }
        return applyLeave(days, reason);
    }

    /** Carry-forward limit differs for every employee type, so each subclass decides. */
    public abstract int getCarryForwardLimit();

    /** New leave year: fresh entitlement plus carried-forward days (up to the type's limit). */
    public void startNewLeaveYear() {
        int carried = Math.min(leaveBalance, getCarryForwardLimit());
        leaveBalance = annualEntitlement + carried;
        System.out.println(name + ": new leave year. Carried forward " + carried + " day(s). Balance = " + leaveBalance);
    }

    public boolean cancelLeave(int requestId) {
        for (LeaveRequest request : history) {
            if (request.getRequestId() == requestId) {
                if (request.getStatus() == LeaveStatus.CANCELLED) {
                    System.out.println(name + ": request #" + requestId + " is already cancelled.");
                    return false;
                }
                request.cancel();
                leaveBalance += request.getDays();
                System.out.println(name + ": request #" + requestId + " cancelled. Balance = " + leaveBalance);
                return true;
            }
        }
        System.out.println(name + ": no request with id " + requestId + ".");
        return false;
    }

    public int checkLeaveBalance() {
        return leaveBalance;
    }

    public void displayDetails() {
        System.out.println("ID: " + employeeId + " | Name: " + name + " | Dept: " + department
                + " | Type: " + employeeType + " | Leave balance: " + leaveBalance);
    }

    public List<LeaveRequest> getHistory() {
        return new ArrayList<>(history);
    }

    public String getEmployeeId() { return employeeId; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
    public String getEmployeeType() { return employeeType; }
    public int getAnnualEntitlement() { return annualEntitlement; }
}
