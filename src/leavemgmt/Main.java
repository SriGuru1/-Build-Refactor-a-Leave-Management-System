package leavemgmt;

public class Main {
    public static void main(String[] args) {
        Employee a = new Employee("E101", "Asha Rao", "Engineering");
        Employee b = new Employee("E102", "Ravi Kumar", "Finance");
        Employee c = new Employee("E103", "Meera Nair", "HR");

        a.displayDetails();
        a.applyLeave(5);
        b.applyLeave(25);   // more than balance -> rejected
        c.applyLeave(0);    // invalid -> rejected
        a.cancelLeave(2);
        System.out.println("Asha's balance: " + a.checkLeaveBalance());
    }
}
