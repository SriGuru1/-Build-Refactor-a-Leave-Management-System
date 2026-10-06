package leavemgmt;

/** Intern employee: 6 days a year, at most 2 days per request. */
public class Intern extends Employee {

    public static final int ANNUAL_ENTITLEMENT = 6;
    public static final int MAX_DAYS_PER_REQUEST = 2;

    public Intern(String employeeId, String name, String department) {
        super(employeeId, name, department, ANNUAL_ENTITLEMENT, "Intern");
    }

    @Override
    public boolean applyLeave(int days, String reason) {
        if (days > MAX_DAYS_PER_REQUEST) {
            System.out.println(getName() + ": Intern employees can take at most " + MAX_DAYS_PER_REQUEST
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
