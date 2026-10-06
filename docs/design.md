# Leave Management System - Initial Design (before coding)

## 1. Problem in one line
A company needs to store employee data, let employees apply for / cancel leave, and apply
different leave rules depending on the type of employee.

## 2. Main objects (nouns in the scenario)
| Object | Why it exists |
|---|---|
| Employee | The person who owns a leave balance and requests leave |
| FullTimeEmployee, PartTimeEmployee, Intern | Employee types with different leave rules |
| LeaveRequest | One application for leave (days, reason, status) |

## 3. Data (attributes)
- Employee: employeeId, name, department, leaveBalance, list of LeaveRequests
- LeaveRequest: requestId, days, reason, status (APPROVED / CANCELLED)

## 4. Behaviours (verbs in the scenario)
- applyLeave(), cancelLeave(), checkLeaveBalance(), displayDetails()
- Rules that differ by type: yearly entitlement, maximum days per request, carry-forward

## 5. Relationships
- FullTimeEmployee, PartTimeEmployee and Intern **are-a** Employee (inheritance)
- An Employee **has many** LeaveRequests (composition: a request cannot exist without its employee)

## 6. Design decisions made up front
- All fields private (encapsulation); behaviour exposed only through methods.
- Employee will become abstract: a "generic" employee does not exist in this company.
- Type-specific rules live in the subclasses; the shared workflow lives once in Employee.
