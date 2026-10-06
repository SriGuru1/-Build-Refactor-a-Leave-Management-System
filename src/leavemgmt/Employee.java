package leavemgmt;

import java.util.ArrayList;
import java.util.List;

/** Second version: requests are stored, so a leave can be cancelled exactly. */
public class Employee {

    public static final int DEFAULT_ANNUAL_LEAVE = 20;

    private String employeeId;
    private String name;
    private String department;
    private int leaveBalance;
    private List<LeaveRequest> history = new ArrayList<>();

    public Employee(String employeeId, String name, String department) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.leaveBalance = DEFAULT_ANNUAL_LEAVE;
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
                + " | Leave balance: " + leaveBalance);
    }

    public List<LeaveRequest> getHistory() {
        return new ArrayList<>(history);
    }

    public String getEmployeeId() { return employeeId; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
}
