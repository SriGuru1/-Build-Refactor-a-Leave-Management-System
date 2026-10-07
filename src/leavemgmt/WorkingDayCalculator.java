package leavemgmt;

import java.time.DayOfWeek;
import java.time.LocalDate;

/** Pure date logic, moved out of Employee so Employee only deals with leave rules. */
public final class WorkingDayCalculator {

    private WorkingDayCalculator() { }

    /** Counts Monday-Friday days from 'from' to 'to', both inclusive. */
    public static int countWorkingDays(LocalDate from, LocalDate to) throws InvalidLeaveRequestException {
        if (from == null || to == null) {
            throw new InvalidLeaveRequestException("Both start and end dates are required.");
        }
        if (to.isBefore(from)) {
            throw new InvalidLeaveRequestException("End date " + to + " is before start date " + from + ".");
        }
        int days = 0;
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            DayOfWeek day = d.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                days++;
            }
        }
        return days;
    }
}
