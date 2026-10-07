package leavemgmt;

/** Part-time employee: 12 days a year, at most 5 days per request, 2 days carry-forward. */
public class PartTimeEmployee extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 12;
    public static final int MAX_DAYS_PER_REQUEST = 5;
    public static final int CARRY_FORWARD_LIMIT = 2;

    public PartTimeEmployee(String employeeId, String name, String department) {
        super(employeeId, name, department, EmployeeType.PART_TIME, ANNUAL_ENTITLEMENT);
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
