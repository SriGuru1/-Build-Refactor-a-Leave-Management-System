package leavemgmt;

/** Type-safe replacement for the old "Full-Time" / "Intern" strings. */
public enum EmployeeType {
    FULL_TIME("Full-Time"),
    PART_TIME("Part-Time"),
    INTERN("Intern"),
    MANAGER("Manager");

    private final String displayName;

    EmployeeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
