package leavemgmt;

/** Intern: 6 days a year, at most 2 days per request, no carry-forward. */
public class Intern extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 6;
    public static final int MAX_DAYS_PER_REQUEST = 2;
    public static final int CARRY_FORWARD_LIMIT = 0;

    public Intern(String employeeId, String name, String department) {
        super(employeeId, name, department, EmployeeType.INTERN, ANNUAL_ENTITLEMENT);
    }

    @Override
    public int getMaxDaysPerRequest() {
        return MAX_DAYS_PER_REQUEST;
    }

    @Override
    public int getCarryForwardLimit() {
        return CARRY_FORWARD_LIMIT;
    }
}
