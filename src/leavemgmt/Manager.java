package leavemgmt;

/**
 * Change request: a Manager is a full-time employee with extra leave benefits.
 * Only the differences are written here; everything else is inherited.
 */
public class Manager extends FullTimeEmployee {

    public static final int BONUS_LEAVE_DAYS = 6;
    public static final int MAX_DAYS_PER_REQUEST = 15;
    public static final int CARRY_FORWARD_LIMIT = 10;

    public Manager(String employeeId, String name, String department) {
        super(employeeId, name, department, EmployeeType.MANAGER, BONUS_LEAVE_DAYS);
    }

    @Override
    public int getMaxDaysPerRequest() {
        return MAX_DAYS_PER_REQUEST;
    }

    @Override
    public int getCarryForwardLimit() {
        return CARRY_FORWARD_LIMIT;
    }

    @Override
    protected String getBenefitNote() {
        return "Manager benefit: +" + BONUS_LEAVE_DAYS + " bonus days on top of the full-time entitlement";
    }
}
