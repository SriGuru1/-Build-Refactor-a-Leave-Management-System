package leavemgmt;

/**
 * Change request: Manager = Full-time employee + extra leave benefits.
 * Only the differences are written here; everything else is inherited.
 */
public class Manager extends FullTimeEmployee {

    public static final int BONUS_LEAVE_DAYS = 6;
    public static final int CARRY_FORWARD_LIMIT = 10;
    public static final int MAX_DAYS_PER_REQUEST = 15;

    public Manager(String employeeId, String name, String department) {
        super(employeeId, name, department, BONUS_LEAVE_DAYS, "Manager");
    }

    @Override
    protected int getMaxDaysPerRequest() {
        return MAX_DAYS_PER_REQUEST;
    }

    @Override
    public int getCarryForwardLimit() {
        return CARRY_FORWARD_LIMIT;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("   Manager benefit: +" + BONUS_LEAVE_DAYS + " bonus days, carry-forward up to "
                + CARRY_FORWARD_LIMIT + " days");
    }
}
