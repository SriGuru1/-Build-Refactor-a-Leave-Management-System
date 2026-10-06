package leavemgmt;

/** Part-time employee: 12 days a year, at most 5 days per request. */
public class PartTimeEmployee extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 12;
    public static final int CARRY_FORWARD_LIMIT = 2;
    public static final int MAX_DAYS_PER_REQUEST = 5;

    public PartTimeEmployee(String employeeId, String name, String department) {
        super(employeeId, name, department, ANNUAL_ENTITLEMENT, "Part-Time");
    }

    @Override
    public boolean applyLeave(int days, String reason) {
        if (days > MAX_DAYS_PER_REQUEST) {
            System.out.println(getName() + ": Part-time employees can take at most " + MAX_DAYS_PER_REQUEST
                    + " days per request.");
            return false;
        }
        return super.applyLeave(days, reason);
    }

    @Override
    public int getCarryForwardLimit() {
        return CARRY_FORWARD_LIMIT;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Rule: max " + MAX_DAYS_PER_REQUEST + " days per request, entitlement "
                + ANNUAL_ENTITLEMENT + " days/year");
    }
}
