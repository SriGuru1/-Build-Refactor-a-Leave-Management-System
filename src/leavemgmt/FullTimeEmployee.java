package leavemgmt;

/** Full-time employee: 24 days a year, at most 10 days per request, 5 days carry-forward. */
public class FullTimeEmployee extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 24;
    public static final int MAX_DAYS_PER_REQUEST = 10;
    public static final int CARRY_FORWARD_LIMIT = 5;

    public FullTimeEmployee(String employeeId, String name, String department) {
        this(employeeId, name, department, EmployeeType.FULL_TIME, 0);
    }

    /** Protected: lets Manager reuse this class and only add a bonus. */
    protected FullTimeEmployee(String employeeId, String name, String department, EmployeeType type, int bonusDays) {
        super(employeeId, name, department, type, ANNUAL_ENTITLEMENT + bonusDays);
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
