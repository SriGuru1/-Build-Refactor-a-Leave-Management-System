package leavemgmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Polymorphism: one list, three different kinds of employee
        List<Employee> staff = new ArrayList<>();
        staff.add(new FullTimeEmployee("E101", "Asha Rao", "Engineering"));
        staff.add(new PartTimeEmployee("E102", "Ravi Kumar", "Finance"));
        staff.add(new Intern("E103", "Meera Nair", "HR"));
        staff.add(new Manager("E104", "Kiran Das", "Operations"));   // added by the change request

        for (Employee e : staff) {
            e.displayDetails();                 // overridden in every subclass
        }
        for (Employee e : staff) {
            e.applyLeave(3);                    // overload 1 (days only)
        }
        // overload 2: Fri 2026-10-09 to Tue 2026-10-13 = Fri, Mon, Tue = 3 working days
        staff.get(0).applyLeave(LocalDate.of(2026, 10, 9), LocalDate.of(2026, 10, 13), "Festival");
        // weekend only -> 0 working days -> rejected
        staff.get(0).applyLeave(LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 11), "Weekend");

        staff.get(3).applyLeave(14, "Sabbatical");   // allowed only for managers (max 15)

        for (Employee e : staff) {
            e.startNewLeaveYear();              // carry-forward rule differs by type
        }
    }
}
