# Leave Management System (Java, OOP)

A console application that stores employees, handles leave requests, and applies different
leave rules per employee type. Built step by step (8 commits) and then refactored.

**Author:** Sri Guru H G - RNS Institute of Technology, Bengaluru
**GitHub:** https://github.com/<your-username>/leave-management-system

## Features
- Employee with ID, name, department and leave balance
- `applyLeave()` (3 overloads), `cancelLeave()`, `checkLeaveBalance()`, `displayDetails()`
- Four employee types with different rules; weekend-aware date-range requests
- New leave year with type-specific carry-forward
- Custom checked exceptions with clear messages; 55 automated tests

## Leave rules
| Type | Days / year | Max days per request | Carry-forward |
|---|---|---|---|
| FullTimeEmployee | 24 | 10 | 5 |
| PartTimeEmployee | 12 | 5 | 2 |
| Intern | 6 | 2 | 0 |
| Manager (extends FullTime) | 24 + 6 bonus = 30 | 15 | 10 |

## Project structure
```
src/leavemgmt/   Employee (abstract), FullTimeEmployee, PartTimeEmployee, Intern, Manager,
                 LeaveRequest, LeaveStatus, EmployeeType, WorkingDayCalculator,
                 LeaveException + 3 subclasses, Main (demo), TestRunner (55 tests)
docs/            design.md (initial design), REFACTORING.md, diagrams/ (class diagrams)
sample_output/   demo_output.txt, test_results.txt (real captured output)
```

## Build and run (JDK 8 or later)
```
javac -d out src/leavemgmt/*.java
java -cp out leavemgmt.Main         # demo
java -cp out leavemgmt.TestRunner   # 55 tests
```

## OOP concepts used
Encapsulation (private/final fields, read-only history), Inheritance (3 types + Manager),
Polymorphism (one `List<Employee>`, four behaviours), Abstraction (abstract `Employee`, abstract hooks),
Method overriding (`getMaxDaysPerRequest`, `getCarryForwardLimit`, `getBenefitNote`),
Method overloading (`applyLeave` x3), Access modifiers (public / protected / package-private / private).
Details with code locations: see the PDF report and `docs/REFACTORING.md`.

## Publish to GitHub
```
git remote add origin https://github.com/<your-username>/leave-management-system.git
git push -u origin main
```
