package leavemgmt;

/** Full-time employee: 24 days a year, at most 10 days per request. */
public class FullTimeEmployee extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 24;
    public static final int MAX_DAYS_PER_REQUEST = 10;

    public FullTimeEmployee(String employeeId, String name, String department) {
        super(employeeId, name, department, ANNUAL_ENTITLEMENT, "Full-Time");
    }

    @Override
    public boolean applyLeave(int days, String reason) {
        if (days > MAX_DAYS_PER_REQUEST) {
            System.out.println(getName() + ": Full-time employees can take at most " + MAX_DAYS_PER_REQUEST
                    + " days per request.");
            return false;
        }
        return super.applyLeave(days, reason);
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Rule: max " + MAX_DAYS_PER_REQUEST + " days per request, entitlement "
                + ANNUAL_ENTITLEMENT + " days/year");
    }
}
