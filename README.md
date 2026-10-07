# 🏢 Leave Management System (LMS)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20OOP%20%26%20SOLID-blue?style=for-the-badge)](https://en.wikipedia.org/wiki/SOLID)
[![Version Control](https://img.shields.io/badge/Git-8%20Progressive%20Commits-F05032?style=for-the-badge&logo=git&logoColor=white)](https://github.com/SriGuru1/-Build-Refactor-a-Leave-Management-System)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)](#-compilation--execution)

> A modular Java application designed and progressively refactored using core **Object-Oriented Programming (OOP)** paradigms, **SOLID** design principles, custom domain exception safety, and clean software architecture.

**GitHub Repository:** [https://github.com/SriGuru1/-Build-Refactor-a-Leave-Management-System](https://github.com/SriGuru1/-Build-Refactor-a-Leave-Management-System)

---

## 📋 Table of Contents
1. [Project Overview & Scenario](#-project-overview--scenario)
2. [System Architecture & UML Diagram](#-system-architecture--uml-diagram)
3. [Employee Class Hierarchy & Leave Policies](#-employee-class-hierarchy--leave-policies)
4. [Core OOP Concepts Applied](#-core-oop-concepts-applied)
5. [Change Request: Manager Role](#-change-request-manager-role)
6. [Debugging & Refactoring Notes](#-debugging--refactoring-notes)
7. [Git Version Control & Commit Progression](#-git-version-control--commit-progression)
8. [Directory Hierarchy](#-directory-hierarchy)
9. [Compilation & Execution](#-compilation--execution)
10. [Test Suite Execution & Sample Output](#-test-suite-execution--sample-output)

---

## 📌 Project Overview & Scenario

### Problem Statement
Organizations require a structured system to track employee leave entitlements, manage leave requests and cancellations, and apply distinct leave policies tailored to different employment categories (Full-Time, Part-Time, Interns, and Managers).

### Solution
This application provides an enterprise-ready, modular system that:
- Encapsulates employee identity, departments, and balances.
- Provides atomic leave operations (`applyLeave`, `cancelLeave`, `checkLeaveBalance`, `displayDetails`).
- Implements role-specific leave constraints and pro-rata calculations.
- Seamlessly accommodates change requests (adding `Manager`) following the **Open-Closed Principle (OCP)**.
- Replaces silent failures with robust custom domain exceptions.
- Separates presentation and directory tracking into a dedicated Service Layer (**Single Responsibility Principle**).

---

## 🏛️ System Architecture & UML Diagram

### Class Hierarchy Architecture

```
                               ┌─────────────────────────┐
                               │   <<abstract entity>>   │
                               │        Employee         │
                               └────────────┬────────────┘
                                            │ (Generalization)
            ┌───────────────────────────────┼───────────────────────────────┐
            │                               │                               │
┌───────────┴───────────┐       ┌───────────┴───────────┐       ┌───────────┴───────────┐
│   FullTimeEmployee    │       │   PartTimeEmployee    │       │        Intern         │
│ • 25d Annual Vacation │       │ • Pro-rata Allowance  │       │ • 5d Tenure Allowance │
│ • 10d Paid Sick Leave │       │ • Max 5 continuous d  │       │ • Max 2 continuous d  │
│ • Health Insured      │       │ • Hourly rate track   │       │ • Mentor assignment   │
└───────────┬───────────┘       └───────────────────────┘       └───────────────────────┘
            │ (Multi-Level Inheritance)
┌───────────┴───────────┐
│        Manager        │  <<Change Request (CR)>>
│ • +8d Executive Bonus │  • Sabbatical Leave Eligibility
│ • Team Approval Hook  │  • Open-Closed Principle (OCP)
└───────────────────────┘
```

### UML Class Details
```
+-----------------------------------------------------------------------------------+
| <<abstract>> Employee                                                             |
+-----------------------------------------------------------------------------------+
| - employeeId: String                                                              |
| - employeeName: String                                                            |
| - department: String                                                              |
| # leaveBalance: double                                                            |
| # totalLeavesTaken: double                                                        |
| - leaveHistory: List<LeaveRequest>                                                |
+-----------------------------------------------------------------------------------+
| + applyLeave(days: int): LeaveRequest                                             |
| + applyLeave(days: int, type: LeaveType, reason: String): LeaveRequest            |
| + cancelLeave(days: int): void                                                    |
| + cancelLeave(requestId: String): boolean                                         |
| + checkLeaveBalance(): double                                                     |
| + displayDetails(): void                                                          |
| # {abstract} validateLeavePolicy(days: int, type: LeaveType): void                |
| + {abstract} getEmployeeRole(): String                                            |
| + {abstract} calculateAnnualEntitlement(): double                                 |
+-----------------------------------------------------------------------------------+
```

---

## 🏢 Employee Class Hierarchy & Leave Policies

| Employee Type | Base Leave Allowance | Special Benefits & Entitlements | Key Policy Restrictions |
| :--- | :---: | :--- | :--- |
| **FullTimeEmployee** | **25 Days** | +10 Days Paid Sick Leave; Health Insurance Coverage | Continuous vacation cannot exceed **20 days**; Ineligible for Sabbatical. |
| **PartTimeEmployee** | **Pro-Rated (10–19 Days)** | Derived dynamically based on weekly hours (`(hours / 40.0) * 20`) | Single request cannot exceed **5 consecutive days**; Ineligible for Sabbatical. |
| **Intern** | **5 Days** | Fixed stipend tenure allowance; Mentor oversight | Single request cannot exceed **2 consecutive days**; Academic training compliance. |
| **Manager** | **33 Days** | +8 Days Executive Bonus (+10 Sick Days = 43d Total); Team Approval Hook | Single leave up to **30 continuous days**; Eligible for **Executive Sabbatical**. |

---

## 🧠 Core OOP Concepts Applied

### 1. Encapsulation (Data Hiding & State Invariants)
- All entity fields (`employeeId`, `employeeName`, `department`, `leaveHistory`) are declared `private` or `protected`.
- Mutation is strictly controlled via validated business methods (`applyLeave`, `cancelLeave`).
- `getLeaveHistory()` returns an unmodifiable collection (`Collections.unmodifiableList`) preventing external tampering.

### 2. Abstraction (Contract-Driven Design & Template Method Pattern)
- `Employee` is declared `abstract`, defining the universal contract while deferring role-specific rules to concrete subclasses via `validateLeavePolicy()`.
- `applyLeave()` implements the **Template Method Pattern**: it validates universal bounds (positive days, overdraft check) and delegates role validation to the subclass hook.

### 3. Inheritance (Code Reuse & Specialization)
- `FullTimeEmployee`, `PartTimeEmployee`, and `Intern` inherit common properties from `Employee`, eliminating duplicate identity and balance logic.
- `Manager` extends `FullTimeEmployee` via **multi-level inheritance**, reusing salaried benefits while adding executive capabilities.

### 4. Polymorphism & Dynamic Method Dispatch
- Collections of heterogeneous employees (`List<Employee>`) are processed uniformly in `LeaveManagementService`.
- Java dynamically resolves overridden methods (`calculateAnnualEntitlement()`, `validateLeavePolicy()`) at runtime based on the actual object instance.

### 5. Method Overriding vs. Method Overloading
- **Method Overriding (Runtime Polymorphism):** Subclasses override `validateLeavePolicy()` and `displayDetails()` to provide specialized behavior.
- **Method Overloading (Compile-Time Polymorphism):**
  - `applyLeave(int days)` vs `applyLeave(int days, LeaveType type, String reason)`
  - `cancelLeave(int days)` vs `cancelLeave(String requestId)`

### 6. Access Modifiers Rationale
- `private`: Class-internal fields (`employeeId`, `leaveHistory`) for strict encapsulation.
- `protected`: Subclass hooks and balance fields (`leaveBalance`, `validateLeavePolicy()`).
- `public`: Service and API boundaries (`applyLeave()`, `displayDetails()`, `registerEmployee()`).

---

## 🔄 Change Request: Manager Role

- **Requirement:** Add a `Manager` employee type with executive perks (+8 bonus days, Sabbatical leave, team approval hook).
- **Architecture Solution:** Implemented `Manager extends FullTimeEmployee` without modifying the base `Employee` class.
- **Principle Followed:** **Open-Closed Principle (OCP)** — open for extension, closed for modification.

```java
public class Manager extends FullTimeEmployee {
    private static final double EXECUTIVE_LEAVE_BONUS = 8.0;
    private int teamSize;
    private final double departmentBudget;

    public Manager(String id, String name, String dept, int teamSize, double budget) {
        super(id, name, dept, 25.0 + EXECUTIVE_LEAVE_BONUS); // Initial balance = 33.0
        this.teamSize = teamSize;
        this.departmentBudget = budget;
    }

    public void approveSubordinateLeave(Employee subordinate, LeaveRequest request) {
        request.setStatus(LeaveStatus.APPROVED);
    }
}
```

---

## 🛠️ Debugging & Refactoring Notes

| # | Code Smell / Issue | Before Refactoring | After Refactoring (Solution) | Software Benefit |
| :-: | :--- | :--- | :--- | :--- |
| **1** | **Duplicate Code** | Validation checks (duration > 0, bounds) scattered across multiple methods. | Centralized in `Employee.applyLeave()` using Template Method pattern. | DRY code; eliminates balance corruption risks. |
| **2** | **Single Responsibility (SRP)** | `Employee` class handled entity state, directory registry, and report printing. | Extracted `LeaveManagementService` to handle multi-employee workflows. | Decoupled architecture; high cohesion. |
| **3** | **Primitive Obsession** | Leave categories and statuses passed as loose strings (`"sick"`, `"approved"`). | Introduced `LeaveType` & `LeaveStatus` Enums and `LeaveRequest` Value Object. | Strong type safety; prevents typo bugs. |
| **4** | **Error Blindness** | Methods returned `false` on failure without explanatory context. | Built custom domain exception hierarchy (`LeaveException`, `InsufficientLeaveBalanceException`, `InvalidLeaveRequestException`). | Robust error recovery and diagnostics. |

---

## 📜 Git Version Control & Commit Progression

The repository follows a clean, 8-stage semantic commit progression:

```bash
* 6d20b58 (HEAD -> master) Final version
* 4bfd90b Refactored code
* 3d84f43 Added Manager
* 1d39906 Implemented polymorphism
* c9e2a1f Added inheritance
* 797fd0a Implemented leave management
* 7dc94d3 Added Employee class
* ce6cace Initial employee design
```

### Commit Milestones
1. `ce6cace` - **Initial employee design**: Project setup, `.gitignore`, and initial design notes.
2. `7dc94d3` - **Added Employee class**: Abstract base class with ID, Name, Department, and Balance.
3. `797fd0a` - **Implemented leave management**: Leave operations, `LeaveRequest` DTO, and enums.
4. `c9e2a1f` - **Added inheritance**: `FullTimeEmployee`, `PartTimeEmployee`, and `Intern` subclasses.
5. `1d39906` - **Implemented polymorphism**: `LeaveManagementService` coordinating polymorphic batch iterations.
6. `3d84f43` - **Added Manager**: Change Request implementation extending `FullTimeEmployee`.
7. `4bfd90b` - **Refactored code**: Cleaned architecture, custom exception hierarchy, and SRP separation.
8. `6d20b58` - **Final version**: Test suite runner, documentation, diagrams, and verification transcripts.

---

## 📁 Directory Hierarchy

```
CAITlms/
├── README.md                                      # Project documentation
├── Leave_Management_System_Assignment_Report.pdf   # Complete coursework report (13 pages)
├── assets/
│   ├── uml_class_diagram.png                      # High-resolution UML Class Diagram
│   └── system_architecture.png                    # System Workflow & Service Architecture
├── bin/                                           # Compiled Java .class bytecode
├── docs/
│   ├── execution_output.txt                       # Raw terminal execution transcript
│   └── git_log.txt                                # Git commit log history
└── src/
    └── com/cait/lms/
        ├── LeaveManagementApp.java                # Main application entry & test runner
        ├── exception/
        │   ├── LeaveException.java                # Base domain exception
        │   ├── InsufficientLeaveBalanceException.java
        │   └── InvalidLeaveRequestException.java
        ├── model/
        │   ├── Employee.java                      # Abstract base class
        │   ├── FullTimeEmployee.java              # Full-time salaried specialization
        │   ├── PartTimeEmployee.java              # Part-time pro-rata specialization
        │   ├── Intern.java                        # Intern tenure-capped specialization
        │   ├── Manager.java                       # Manager executive specialization
        │   ├── LeaveRequest.java                  # Value Object / DTO
        │   ├── LeaveStatus.java                   # Lifecycle enum
        │   └── LeaveType.java                     # Leave category enum
        └── service/
            └── LeaveManagementService.java        # Business controller service layer
```

---

## 🚀 Compilation & Execution

### Prerequisites
- **JDK 17** or higher
- **Git 2.30+**

### Step-by-Step Commands

```bash
# 1. Clone the repository
git clone https://github.com/SriGuru1/-Build-Refactor-a-Leave-Management-System.git
cd -Build-Refactor-a-Leave-Management-System

# 2. Compile all Java source files into bytecode output directory
javac -d bin src/com/cait/lms/model/*.java src/com/cait/lms/exception/*.java src/com/cait/lms/service/*.java src/com/cait/lms/LeaveManagementApp.java

# 3. Run the application demonstration test suite
java -cp bin com.cait.lms.LeaveManagementApp

# 4. View git commit log
git log --oneline --graph --decorate
```

---

## 🧪 Test Suite Execution & Sample Output

```
******************************************************************
   ENTERPRISE LEAVE MANAGEMENT SYSTEM (LMS) - DEMONSTRATION SUITE  
   Built with Core Java & Advanced Object-Oriented Principles     
******************************************************************

>>> STEP 1: Instantiating Employees of Diverse Subtypes...
  [REGISTRY] Registered Alice Johnson (EMP-101) - Full-Time Employee (Salaried)
  [REGISTRY] Registered Bob Smith (EMP-102) - Part-Time Employee (24 hrs/week)
  [REGISTRY] Registered Charlie Brown (EMP-103) - Intern (6 Months | Mentor: Dr. Evelyn Reed)
  [REGISTRY] Registered Diana Prince (MGR-201) - Manager (Lead of 12 Personnel)

==================================================================
               ORGANIZATIONAL EMPLOYEE DIRECTORY                 
==================================================================
------------------------------------------------------------------
Employee ID   : EMP-101
Name          : Alice Johnson
Role / Type   : Full-Time Employee (Salaried)
Department    : Engineering
Leave Balance : 25.0 Days
Leaves Taken  : 0.0 Days
History Count : 0 Record(s)
Health Insured: Yes (Comprehensive)
Sick Balance  : 10.0 Days
------------------------------------------------------------------
------------------------------------------------------------------
Employee ID   : EMP-102
Name          : Bob Smith
Role / Type   : Part-Time Employee (24 hrs/week)
Department    : Technical Support
Leave Balance : 12.0 Days
Leaves Taken  : 0.0 Days
History Count : 0 Record(s)
Weekly Hours  : 24 Hours
Hourly Rate   : $28.50 / hr
------------------------------------------------------------------
------------------------------------------------------------------
Employee ID   : EMP-103
Name          : Charlie Brown
Role / Type   : Intern (6 Months | Mentor: Dr. Evelyn Reed)
Department    : Quality Assurance
Leave Balance : 5.0 Days
Leaves Taken  : 0.0 Days
History Count : 0 Record(s)
Duration      : 6 Months
Mentor        : Dr. Evelyn Reed
------------------------------------------------------------------
------------------------------------------------------------------
Employee ID   : MGR-201
Name          : Diana Prince
Role / Type   : Manager (Lead of 12 Personnel)
Department    : Product Engineering
Leave Balance : 33.0 Days
Leaves Taken  : 0.0 Days
History Count : 0 Record(s)
Health Insured: Yes (Comprehensive)
Sick Balance  : 10.0 Days
Managed Team  : 12 Members
Dept Budget   : $450,000.00
------------------------------------------------------------------

>>> STEP 2: Testing FullTimeEmployee Leave Application & Balance Deduction...
Checking Initial Balance for Alice Johnson: 25.0 Days
  [SUCCESS] Leave Approved for Alice Johnson (EMP-101). Deducted: 5 day(s). New Balance: 20.0
After 5 days leave, Balance: 20.0 Days
  [SUCCESS] Leave Approved for Alice Johnson (EMP-101). Deducted: 3 day(s). New Balance: 17.0
After 3 days sick leave, Balance: 17.0 Days

>>> Testing Leave Cancellation (Reversal)...
  [CANCELLED] Request LR-1001 cancelled. Restored 5.0 days to Alice Johnson. New Balance: 22.0
After cancelling request LR-1001, Restored Balance: 22.0 Days

>>> STEP 3: Testing Robust Exception Handling (Edge Cases & Safety)...
Attempt A: Requesting 25 continuous days (exceeds 20-day full-time policy)...
  [CAUGHT EXPECTED POLICY EXCEPTION]: Full-time policy: Continuous annual leave cannot exceed 20 days.
  [SUCCESS] Leave Approved for Alice Johnson (EMP-101). Deducted: 15 day(s). New Balance: 7.0
Current Balance after 15 days: 7.0 Days
Attempt B: Requesting 10 days when balance is only 7.0 days...
  [CAUGHT EXPECTED BALANCE EXCEPTION]: Leave rejected for [EMP-101]: Requested 10.0 days, but available balance is only 7.0 days.

>>> STEP 4: Testing Part-Time Pro-Rata Rules & Policy Enforcement...
Part-time Employee Bob Smith has pro-rated balance: 12.0 Days
  [SUCCESS] Leave Approved for Bob Smith (EMP-102). Deducted: 3 day(s). New Balance: 9.0
Attempting to request 6 consecutive days (violates 5-day part-time policy)...
  [CAUGHT EXPECTED POLICY EXCEPTION]: Part-time policy restriction: Maximum 5 consecutive days allowed.

>>> STEP 5: Testing Intern Leave Constraints (Max 2 Consecutive Days)...
Intern Charlie Brown initial balance: 5.0 Days
  [SUCCESS] Leave Approved for Charlie Brown (EMP-103). Deducted: 1 day(s). New Balance: 4.0
Attempting to request 4 days for intern (violates 2-day policy)...
  [CAUGHT EXPECTED INTERN POLICY EXCEPTION]: Intern policy restriction: Interns may only take max 2 consecutive days.

>>> STEP 6: Testing Change Request - Manager Role & Executive Perks...
Manager Diana Prince initial enhanced balance: 33.0 Days (Includes +8 Executive Bonus)
  [SUCCESS] Leave Approved for Diana Prince (MGR-201). Deducted: 15 day(s). New Balance: 18.0
Manager Sabbatical approved! Remaining Balance: 18.0 Days

>>> Manager Approving Subordinate Leave...
  [MANAGER APPROVAL] Manager Diana Prince approved leave request [LR-SPECIAL] for Charlie Brown (Quality Assurance).

>>> STEP 7: Demonstrating Polymorphism via Heterogeneous Collection Processing...
Polymorphically iterating over List<Employee>:
  -> [EMP-101   ] Alice Johnson    | Subclass: FullTimeEmployee   | Annual Entitlement: 35.0 Days
  -> [EMP-102   ] Bob Smith        | Subclass: PartTimeEmployee   | Annual Entitlement: 12.0 Days
  -> [EMP-103   ] Charlie Brown    | Subclass: Intern             | Annual Entitlement: 5.0 Days
  -> [MGR-201   ] Diana Prince     | Subclass: Manager            | Annual Entitlement: 43.0 Days

==================================================================
           EXECUTIVE LEAVE UTILIZATION AUDIT REPORT              
==================================================================
EMP ID     | NAME               | ROLE               | BALANCE    | TAKEN     
------------------------------------------------------------------
EMP-101    | Alice Johnson      | FullTimeEmployee   |        7.0 |       18.0
EMP-102    | Bob Smith          | PartTimeEmployee   |        9.0 |        3.0
EMP-103    | Charlie Brown      | Intern             |        4.0 |        1.0
MGR-201    | Diana Prince       | Manager            |       18.0 |       15.0
------------------------------------------------------------------
TOTAL REMAINING BALANCE :       38.0 Days
TOTAL LEAVES UTILIZED   :       37.0 Days
==================================================================
******************************************************************
       ALL TEST SUITE SCENARIOS EXECUTED SUCCESSFULLY!            
******************************************************************
```

---

## 👨‍💻 Author
- **Repository:** [https://github.com/SriGuru1/-Build-Refactor-a-Leave-Management-System](https://github.com/SriGuru1/-Build-Refactor-a-Leave-Management-System)
