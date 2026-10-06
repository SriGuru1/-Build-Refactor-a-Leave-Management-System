package leavemgmt;

public class Main {
    public static void main(String[] args) {
        Employee a = new FullTimeEmployee("E101", "Asha Rao", "Engineering");
        Employee b = new PartTimeEmployee("E102", "Ravi Kumar", "Finance");
        Employee c = new Intern("E103", "Meera Nair", "HR");

        a.displayDetails();
        b.displayDetails();
        c.displayDetails();

        a.applyLeave(8, "Family function");
        a.applyLeave(11, "Long trip");      // full-time max is 10
        b.applyLeave(6, "Exam");            // part-time max is 5
        c.applyLeave(3, "Workshop");        // intern max is 2
        c.applyLeave(2, "Workshop");
        c.applyLeave(2, "Fever");
        c.applyLeave(2, "Fever");
        c.applyLeave(1, "Fever");           // balance exhausted
    }
}
