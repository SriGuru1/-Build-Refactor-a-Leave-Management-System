package leavemgmt;

/** First version: a single concrete Employee class that owns a leave balance. */
public class Employee {

    public static final int DEFAULT_ANNUAL_LEAVE = 20;

    private String employeeId;
    private String name;
    private String department;
    private int leaveBalance;

    public Employee(String employeeId, String name, String department) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.leaveBalance = DEFAULT_ANNUAL_LEAVE;
    }

    public boolean applyLeave(int days) {
        if (days <= 0) {
            System.out.println(name + ": invalid number of days.");
            return false;
        }
        if (days > leaveBalance) {
            System.out.println(name + ": not enough leave (balance " + leaveBalance + ", requested " + days + ").");
            return false;
        }
        leaveBalance -= days;
        System.out.println(name + ": " + days + " day(s) of leave approved. Balance = " + leaveBalance);
        return true;
    }

    public boolean cancelLeave(int days) {
        if (days <= 0) {
            System.out.println(name + ": invalid number of days.");
            return false;
        }
        leaveBalance += days;
        System.out.println(name + ": " + days + " day(s) restored. Balance = " + leaveBalance);
        return true;
    }

    public int checkLeaveBalance() {
        return leaveBalance;
    }

    public void displayDetails() {
        System.out.println("ID: " + employeeId + " | Name: " + name + " | Dept: " + department
                + " | Leave balance: " + leaveBalance);
    }

    public String getEmployeeId() { return employeeId; }
    public String getName()       { return name; }
    public String getDepartment() { return department; }
}
