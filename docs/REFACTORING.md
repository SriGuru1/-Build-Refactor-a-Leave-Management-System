# Refactoring Notes

Code before refactoring = commit `Added Manager`. Code after = commit `Refactored code`.
Verified by `TestRunner` (55 tests, all passing).

## 1. Duplicate code -> Template Method
**Problem.** FullTimeEmployee, PartTimeEmployee, Intern (and the Manager change) each overrode
`applyLeave()` with the same "if days > MAX ... print ... return false" block, and each overrode
`displayDetails()` with the same "print Rule: ..." block. Every new employee type would copy it again.
**Fix.** The workflow now exists once in `Employee.applyLeave(int, String)` (declared `final`).
Subclasses only supply two one-line hooks: `getMaxDaysPerRequest()` and `getCarryForwardLimit()`.
`displayDetails()` is built once from `getDetails()`, with an optional `getBenefitNote()` hook.
**Result.** FullTimeEmployee 44 -> 28 lines, PartTimeEmployee 35 -> 23, Intern 35 -> 23.
Zero copies of validation logic remain in subclasses.

## 2. Class responsibilities: model classes no longer print
**Problem.** `Employee` and every subclass called `System.out.println` (15 calls in the model),
mixing business rules with user-interface output; results could not be tested or reused (GUI, web).
**Fix.** Model methods now return values (`LeaveRequest`, `int`) or throw exceptions.
All printing moved to `Main`. Date arithmetic moved out of `Employee` into `WorkingDayCalculator`.
**Result.** `System.out` in model classes: 15 -> 1 (only the required `displayDetails()` method).

## 3. Exception handling instead of boolean + printed message
**Problem.** `applyLeave()` returned `false` after printing; callers could not tell *why* it failed.
**Fix.** Checked exception hierarchy: `LeaveException` -> `InvalidLeaveRequestException`,
`InsufficientLeaveBalanceException` (carries requested / available / shortfall),
`LeaveRequestNotFoundException`. Callers are forced by the compiler to handle failures.

## 4. Naming and type safety
**Problem.** Employee type was a free `String` ("Full-Time") passed to the constructor - typos compile.
Console message read "... APPROVED approved".
**Fix.** `EmployeeType` enum with a display name; messages rebuilt from `LeaveRequest.toString()`.

## 5. Bug found while testing: cancelling last year's request inflated the balance
Reproduced on the pre-refactor code:
```
Test Intern: request #1 | 2 day(s) | Fever | APPROVED approved. Balance = 4
Test Intern: new leave year. Carried forward 0 day(s). Balance = 6
Test Intern: request #1 cancelled. Balance = 8
Balance after cancelling an old-year request: 8 (entitlement is 6)
```
**Fix.** Every `LeaveRequest` records its `leaveYear`; `cancelLeave()` rejects requests from a closed year.
Covered by tests T50-T51.

## 6. Encapsulation hardening (maintainability)
- Identity fields (`employeeId`, `name`, `department`, `type`, `annualEntitlement`) are now `final`.
- Constructor rejects null/blank values (`IllegalArgumentException`) and trims names.
- `getHistory()` returns an unmodifiable view; `LeaveRequest`'s constructor and `cancel()` are package-private,
  so only `Employee` can create or cancel requests.
