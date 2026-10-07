package leavemgmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Self-contained test runner (no libraries needed). Run: java -cp out leavemgmt.TestRunner
 * Groups: A construction/validation, B employee-type rules, C boundaries, D invalid input,
 * E cancellation, F date ranges, G year rollover, H polymorphism/encapsulation.
 */
public class TestRunner {

    private static int total = 0;
    private static int passed = 0;

    @FunctionalInterface
    private interface Action { void run() throws Exception; }

    private static void report(String name, boolean ok, String detail) {
        total++;
        if (ok) passed++;
        System.out.printf("[%s] T%02d %-58s %s%n", ok ? "PASS" : "FAIL", total, name, detail);
    }

    private static void eq(String name, Object expected, Object actual) {
        report(name, expected.equals(actual), "expected " + expected + ", got " + actual);
    }

    private static void yes(String name, boolean cond) {
        report(name, cond, cond ? "holds" : "violated");
    }

    private static void throwsEx(String name, Class<? extends Exception> type, Action a) {
        try {
            a.run();
            report(name, false, "no exception thrown");
        } catch (Exception e) {
            boolean ok = type.isInstance(e);
            report(name, ok, ok ? "threw " + e.getClass().getSimpleName() : "got " + e.getClass().getSimpleName());
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=========== Leave Management System - Automated Tests ===========");

        System.out.println("\n--- A. Construction and validation ---");
        Employee ft = new FullTimeEmployee("E1", "Asha", "Eng");
        Employee pt = new PartTimeEmployee("E2", "Ravi", "Fin");
        Employee in = new Intern("E3", "Meera", "HR");
        Employee mg = new Manager("E4", "Kiran", "Ops");
        eq("Full-time starts with 24 days", 24, ft.checkLeaveBalance());
        eq("Part-time starts with 12 days", 12, pt.checkLeaveBalance());
        eq("Intern starts with 6 days", 6, in.checkLeaveBalance());
        eq("Manager starts with 30 days (24 + 6 bonus)", 30, mg.checkLeaveBalance());
        throwsEx("Blank name rejected", IllegalArgumentException.class, () -> new Intern("E9", "  ", "HR"));
        throwsEx("Null ID rejected", IllegalArgumentException.class, () -> new Intern(null, "X", "HR"));
        throwsEx("Blank department rejected", IllegalArgumentException.class, () -> new Intern("E9", "X", ""));
        eq("Name is trimmed", "Neha", new Intern("E9", "  Neha ", "HR").getName());

        System.out.println("\n--- B. Rules differ by employee type (polymorphism) ---");
        eq("Full-time max per request = 10", 10, ft.getMaxDaysPerRequest());
        eq("Part-time max per request = 5", 5, pt.getMaxDaysPerRequest());
        eq("Intern max per request = 2", 2, in.getMaxDaysPerRequest());
        eq("Manager max per request = 15", 15, mg.getMaxDaysPerRequest());
        eq("Carry-forward limits (FT/PT/Intern/Mgr)", "5/2/0/10",
                ft.getCarryForwardLimit() + "/" + pt.getCarryForwardLimit() + "/" + in.getCarryForwardLimit() + "/" + mg.getCarryForwardLimit());
        yes("Manager IS-A FullTimeEmployee (reuse by inheritance)", mg instanceof FullTimeEmployee);
        yes("Manager details contain the benefit line", mg.getDetails().contains("Manager benefit"));
        yes("Full-time details have no benefit line", !ft.getDetails().contains("benefit"));

        System.out.println("\n--- C. Boundary values ---");
        Employee b1 = new FullTimeEmployee("B1", "B", "D");
        b1.applyLeave(10, "Exactly the limit");
        eq("Full-time: 10 days (= limit) accepted", 14, b1.checkLeaveBalance());
        throwsEx("Full-time: 11 days (limit + 1) rejected", InvalidLeaveRequestException.class,
                () -> new FullTimeEmployee("B2", "B", "D").applyLeave(11));
        Employee b3 = new Intern("B3", "B", "D");
        b3.applyLeave(2); b3.applyLeave(2); b3.applyLeave(2);
        eq("Intern: balance can reach exactly 0", 0, b3.checkLeaveBalance());
        throwsEx("Intern: 1 day with balance 0 rejected", InsufficientLeaveBalanceException.class, () -> b3.applyLeave(1));
        Employee b4 = new Manager("B4", "B", "D");
        b4.applyLeave(15, "Max for manager");
        eq("Manager: 15 days accepted (full-time limit would reject)", 15, b4.checkLeaveBalance());
        Employee b5 = new Intern("B5", "B", "D");
        b5.applyLeave(2); b5.applyLeave(2);
        try { b5.applyLeave(3); } catch (InvalidLeaveRequestException expected) { /* limit checked first */ }
        eq("Rejected request leaves balance unchanged", 2, b5.checkLeaveBalance());
        eq("Rejected request is not stored in history", 2, b5.getHistory().size());

        System.out.println("\n--- D. Invalid input ---");
        Employee d = new FullTimeEmployee("D1", "D", "D");
        throwsEx("0 days rejected", InvalidLeaveRequestException.class, () -> d.applyLeave(0));
        throwsEx("Negative days rejected", InvalidLeaveRequestException.class, () -> d.applyLeave(-3));
        throwsEx("Null reason rejected", InvalidLeaveRequestException.class, () -> d.applyLeave(1, null));
        throwsEx("Blank reason rejected", InvalidLeaveRequestException.class, () -> d.applyLeave(1, "   "));
        try {
            new Intern("D2", "D", "D").applyLeave(2); // fine
            Employee low = new PartTimeEmployee("D3", "D", "D");
            low.applyLeave(5); low.applyLeave(5);      // 2 left
            low.applyLeave(4);
        } catch (InsufficientLeaveBalanceException e) {
            eq("Exception reports shortfall (asked 4, had 2)", 2, e.getShortfall());
        }
        eq("applyLeave(days) uses the default reason", Employee.DEFAULT_REASON, d.applyLeave(1).getReason());
        eq("Reason is trimmed", "Trip", d.applyLeave(1, "  Trip ").getReason());
        eq("Rejected requests consume no id; ids are sequential", 2, d.getHistory().get(1).getRequestId());

        System.out.println("\n--- E. Cancellation ---");
        Employee e = new FullTimeEmployee("F1", "F", "D");
        LeaveRequest r1 = e.applyLeave(5, "Trip");
        e.cancelLeave(r1.getRequestId());
        eq("Cancel restores exactly the days taken", 24, e.checkLeaveBalance());
        eq("Cancelled request has status CANCELLED", LeaveStatus.CANCELLED, r1.getStatus());
        throwsEx("Cancelling twice rejected", InvalidLeaveRequestException.class, () -> e.cancelLeave(1));
        throwsEx("Cancelling unknown id rejected", LeaveRequestNotFoundException.class, () -> e.cancelLeave(99));
        eq("Failed cancels did not change balance", 24, e.checkLeaveBalance());

        System.out.println("\n--- F. Date-range overload ---");
        Employee f = new FullTimeEmployee("G1", "G", "D");
        eq("Fri 9 Oct to Tue 13 Oct 2026 = 3 working days", 3,
                f.applyLeave(LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 13), "Festival").getDays());
        eq("Single weekday = 1 day", 1,
                f.applyLeave(LocalDate.of(2026, 10, 14), LocalDate.of(2026, 10, 14), "Day off").getDays());
        throwsEx("Weekend-only range rejected", InvalidLeaveRequestException.class,
                () -> f.applyLeave(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 11), "Weekend"));
        throwsEx("End before start rejected", InvalidLeaveRequestException.class,
                () -> f.applyLeave(LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 19), "Backwards"));
        throwsEx("Null date rejected", InvalidLeaveRequestException.class, () -> f.applyLeave(null, LocalDate.now(), "x"));
        throwsEx("Date range over the type limit rejected (Intern, 3 days)", InvalidLeaveRequestException.class,
                () -> new Intern("G2", "G", "D").applyLeave(LocalDate.of(2026, 10, 12), LocalDate.of(2026, 10, 14), "x"));

        System.out.println("\n--- G. New leave year ---");
        Employee g1 = new FullTimeEmployee("H1", "H", "D");   // 24 -> use 4 -> 20 left, carry only 5
        g1.applyLeave(4);
        eq("Full-time carries forward capped at 5 (returned value)", 5, g1.startNewLeaveYear());
        eq("Full-time new balance = 24 + 5", 29, g1.checkLeaveBalance());
        Employee g2 = new Intern("H2", "H", "D");
        g2.applyLeave(2);
        eq("Intern carries forward 0", 0, g2.startNewLeaveYear());
        eq("Intern new balance = 6", 6, g2.checkLeaveBalance());
        Employee g3 = new Manager("H3", "H", "D");
        g3.applyLeave(15);                                     // 15 left -> carry capped at 10
        eq("Manager carries forward capped at 10", 10, g3.startNewLeaveYear());
        eq("Manager new balance = 30 + 10", 40, g3.checkLeaveBalance());
        Employee g4 = new PartTimeEmployee("H4", "H", "D");
        g4.applyLeave(5); g4.applyLeave(5);                    // 2 left, below cap of 2? equals cap
        eq("Part-time carries forward all 2 days (= cap)", 2, g4.startNewLeaveYear());
        Employee g5 = new Intern("H5", "H", "D");
        g5.applyLeave(2);
        g5.startNewLeaveYear();
        throwsEx("BUG FIX: cancelling an old-year request is rejected", InvalidLeaveRequestException.class, () -> g5.cancelLeave(1));
        eq("BUG FIX: balance stays at entitlement (6), not 8", 6, g5.checkLeaveBalance());
        eq("Leave year counter increments", 2, g5.getLeaveYear());

        System.out.println("\n--- H. Polymorphism and encapsulation ---");
        List<Employee> all = new ArrayList<>();
        all.add(ft); all.add(pt); all.add(in); all.add(mg);
        int totalMax = 0;
        for (Employee x : all) totalMax += x.getMaxDaysPerRequest();     // same call, 4 behaviours
        eq("Sum of max-days over mixed list (10+5+2+15)", 32, totalMax);
        throwsEx("History list is read-only", UnsupportedOperationException.class,
                () -> ft.getHistory().add(null));
        Employee c1 = new FullTimeEmployee("K1", "K", "D");
        c1.applyLeave(3);
        eq("Balance changes only through applyLeave/cancelLeave", 21, c1.checkLeaveBalance());

        System.out.println("\n=================================================================");
        System.out.printf("RESULT: %d of %d tests passed.%n", passed, total);
        System.exit(passed == total ? 0 : 1);
    }
}
