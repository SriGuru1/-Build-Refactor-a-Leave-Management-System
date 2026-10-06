package leavemgmt;

public class Main {
    public static void main(String[] args) {
        Employee a = new Employee("E101", "Asha Rao", "Engineering");
        Employee b = new Employee("E102", "Ravi Kumar", "Finance");
        Employee c = new Employee("E103", "Meera Nair", "HR");

        a.applyLeave(5, "Family function");
        a.applyLeave(3, "Medical");
        b.applyLeave(25, "Long trip");     // more than balance
        c.applyLeave(2, "   ");            // blank reason
        a.cancelLeave(1);
        a.cancelLeave(1);                  // already cancelled
        a.cancelLeave(9);                  // does not exist
        for (LeaveRequest r : a.getHistory()) {
            System.out.println("  " + r);
        }
        a.displayDetails();
    }
}
