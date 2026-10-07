package leavemgmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Console demo. All printing lives here; the model classes only return values or throw exceptions. */
public class Main {

    public static void main(String[] args) {
        // Polymorphism: one list, four different kinds of employee
        List<Employee> staff = new ArrayList<>();
        staff.add(new FullTimeEmployee("E101", "Asha Rao", "Engineering"));
        staff.add(new PartTimeEmployee("E102", "Ravi Kumar", "Finance"));
        staff.add(new Intern("E103", "Meera Nair", "HR"));
        staff.add(new Manager("E104", "Kiran Das", "Operations"));

        section("1. Employee details (overridden rules, same method call)");
        for (Employee e : staff) {
            e.displayDetails();
        }

        section("2. applyLeave(days) - same call, different rules per type");
        for (Employee e : staff) {
            apply(e, 3, "Personal leave");
        }

        section("3. Rule violations are rejected with clear messages");
        apply(staff.get(0), 11, "Long trip");           // over full-time limit of 10
        apply(staff.get(1), 6, "Exam");                  // over part-time limit of 5
        apply(staff.get(2), 3, "Workshop");              // over intern limit of 2
        apply(staff.get(3), 14, "Sabbatical");           // allowed: manager limit is 15
        apply(staff.get(0), 0, "Nothing");               // zero days
        apply(staff.get(0), 2, "   ");                  // blank reason
        apply(staff.get(2), 2, "Fever");                 // intern balance: 6 -> 4 -> 2 -> 0
        apply(staff.get(2), 2, "Fever");
        apply(staff.get(2), 2, "Fever");
        apply(staff.get(2), 1, "One more");              // nothing left

        section("4. applyLeave(from, to, reason) - weekends are skipped");
        applyDates(staff.get(0), LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 13), "Festival");   // Fri..Tue
        applyDates(staff.get(0), LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 11), "Weekend");   // Sat..Sun
        applyDates(staff.get(0), LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 19), "Backwards");

        section("5. cancelLeave(requestId)");
        cancel(staff.get(0), 1);
        cancel(staff.get(0), 1);                         // already cancelled
        cancel(staff.get(0), 99);                        // unknown id

        section("6. Request history of Asha Rao");
        for (LeaveRequest r : staff.get(0).getHistory()) {
            System.out.println("  " + r);
        }

        section("7. New leave year - carry-forward differs by type");
        for (Employee e : staff) {
            int before = e.checkLeaveBalance();
            int carried = e.startNewLeaveYear();
            System.out.printf("  %-10s %-10s balance before %2d -> carried %2d -> new balance %2d%n",
                    e.getName(), "(" + e.getType().getDisplayName() + ")", before, carried, e.checkLeaveBalance());
        }
        cancel(staff.get(0), 2);                         // belongs to the closed year

        section("8. Final balances");
        for (Employee e : staff) {
            System.out.printf("  %-10s : %d day(s)%n", e.getName(), e.checkLeaveBalance());
        }
    }

    // ---------- small presentation helpers (the only place that catches LeaveException) ----------

    private static void section(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    private static void apply(Employee e, int days, String reason) {
        try {
            LeaveRequest r = e.applyLeave(days, reason);
            System.out.printf("  OK       %-10s %s -> balance %d%n", e.getName(), r, e.checkLeaveBalance());
        } catch (LeaveException ex) {
            System.out.printf("  REJECTED %-10s %s%n", e.getName(), ex.getMessage());
        }
    }

    private static void applyDates(Employee e, LocalDate from, LocalDate to, String reason) {
        try {
            LeaveRequest r = e.applyLeave(from, to, reason);
            System.out.printf("  OK       %-10s %s to %s = %s -> balance %d%n", e.getName(), from, to, r, e.checkLeaveBalance());
        } catch (LeaveException ex) {
            System.out.printf("  REJECTED %-10s %s to %s: %s%n", e.getName(), from, to, ex.getMessage());
        }
    }

    private static void cancel(Employee e, int requestId) {
        try {
            LeaveRequest r = e.cancelLeave(requestId);
            System.out.printf("  OK       %-10s cancelled %s -> balance %d%n", e.getName(), r, e.checkLeaveBalance());
        } catch (LeaveException ex) {
            System.out.printf("  REJECTED %-10s %s%n", e.getName(), ex.getMessage());
        }
    }
}
