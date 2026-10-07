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
