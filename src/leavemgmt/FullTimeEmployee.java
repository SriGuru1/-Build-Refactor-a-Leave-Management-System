package leavemgmt;

/** Full-time employee: 24 days a year, at most 10 days per request. */
public class FullTimeEmployee extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 24;
    public static final int CARRY_FORWARD_LIMIT = 5;
    public static final int MAX_DAYS_PER_REQUEST = 10;

    public FullTimeEmployee(String employeeId, String name, String department) {
        this(employeeId, name, department, 0, "Full-Time");
    }

    /** Protected: lets Manager reuse everything and only add a bonus. */
    protected FullTimeEmployee(String employeeId, String name, String department, int bonusDays, String employeeType) {
        super(employeeId, name, department, ANNUAL_ENTITLEMENT + bonusDays, employeeType);
    }

    protected int getMaxDaysPerRequest() {
        return MAX_DAYS_PER_REQUEST;
    }

    @Override
    public int getCarryForwardLimit() {
        return CARRY_FORWARD_LIMIT;
    }

    @Override
    public boolean applyLeave(int days, String reason) {
        if (days > getMaxDaysPerRequest()) {
            System.out.println(getName() + ": " + getEmployeeType() + " employees can take at most "
                    + getMaxDaysPerRequest() + " days per request.");
            return false;
        }
        return super.applyLeave(days, reason);
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Rule: max " + getMaxDaysPerRequest() + " days per request, entitlement "
                + getAnnualEntitlement() + " days/year");
    }
}
