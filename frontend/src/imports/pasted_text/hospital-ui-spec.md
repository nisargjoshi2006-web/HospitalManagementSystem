Design a modern, professional **Hospital Management System desktop application UI** based on an existing Java Swing + MySQL hospital management project.

The UI should feel like a polished real-world hospital operations platform rather than an academic CRUD application. Prioritize clarity, speed, accessibility, information density, and a clean medical aesthetic.

## Overall Design Direction

Create a **desktop-first hospital administration dashboard** with:

* Clean modern healthcare SaaS aesthetic
* White / very light neutral background
* Deep navy or dark blue primary text
* Teal / cyan medical accent color
* Subtle green for success states
* Amber for warnings
* Red for emergencies/errors
* Soft borders and restrained shadows
* Rounded cards, approximately 10–14px radius
* Modern typography such as Inter
* Consistent 8px spacing system
* Professional icons from Lucide / Material Symbols
* Avoid excessive gradients, glassmorphism, oversized illustrations, or flashy effects
* Dense enough for hospital staff to work efficiently, but not visually cluttered

## Main Application Structure

Use a persistent **left sidebar navigation** and a top header.

Sidebar:

* Hospital logo + name: "CarePoint Hospital"
* Dashboard
* Patients
* Doctors
* Doctor Schedule
* Appointments
* Prescriptions
* Billing
* Emergency
* Lab Tests
* Bed Allocation
* Feedback
* Reports
* Audit Logs
* Settings
* Logout

Clearly show the currently selected navigation item.

Top header:

* Page title and short contextual subtitle
* Global search
* Notifications
* Database/system health indicator
* Logged-in user avatar
* User name and role, e.g. "Admin"
* Date/time where appropriate

## Login Screen

Create a premium hospital login screen:

* Hospital logo
* "CarePoint Hospital" branding
* "Hospital Management System" subtitle
* Username field
* Password field
* Show/hide password control
* Remember me
* Login button
* Forgot password link
* Small security message
* Clean medical illustration or subtle healthcare visual on one side
* Desktop split-screen composition

Also create a receptionist-role login state visually consistent with the admin login.

## Dashboard

Create the primary hospital operations dashboard.

Header:
"Good morning, Admin"
"Here's today's hospital overview"

Top KPI cards:

* Total Patients
* Doctors
* Today's Appointments
* Emergency Cases
* Occupied Beds
* Pending Lab Tests
* Outstanding Bills
* Total Revenue

Each KPI card should contain:

* Large value
* Short label
* Small trend / comparison indicator
* Relevant icon
* Optional mini sparkline

Main dashboard content:

1. **Today's Appointments**

   * Patient
   * Doctor
   * Specialization
   * Time
   * Room
   * Status

2. **Emergency Cases**

   * Patient
   * Priority
   * Assigned doctor
   * Time
   * Status
   * Critical cases should be visually prominent

3. **Bed Occupancy**

   * ICU
   * Private
   * General
   * Occupied vs available visualization

4. **Revenue Overview**

   * Clean weekly/monthly revenue chart

5. **Pending Lab Tests**

   * Test
   * Patient
   * Status
   * Ordered date

6. **Quick Actions**

   * Add Patient
   * Book Appointment
   * Add Prescription
   * Create Bill
   * Admit Patient
   * Emergency Case

Include a subtle "Refresh Dashboard" action and a system/database health indicator.

## Patients Screen

Design a professional patient management page.

Header:
"Patients"
"Manage patient records and registration"

Actions:

* Add Patient
* Import / Export if appropriate

Search and filtering:

* Search by Patient ID or Name
* Blood group filter
* Registration date filter
* Clear filters

Main table:

* Patient ID
* Patient Name
* Age
* Gender
* Blood Group
* Contact
* Registration Date
* Actions

Row actions:

* View
* Edit
* Delete

Create an attractive **patient profile/details page or side drawer** showing:

* Basic information
* Contact information
* Registration date
* Appointment history
* Prescription history
* Billing history
* Clinical history

## Doctors Screen

Create a doctors management page.

Doctor cards/table should show:

* Doctor ID
* Name
* Specialization
* Consultation Fee
* Availability
* Contact
* Actions

Include:

* Search
* Filter by specialization
* Add Doctor
* Edit Doctor
* Delete confirmation modal

Use specialization chips such as:
Cardiology, Neurology, Orthopedics, Pediatrics, Dermatology.

## Doctor Schedule

Create a weekly doctor schedule interface.

Features:

* Doctor selector
* Week/day selector
* Calendar-style time slots
* Available / booked indicators
* Start and end time
* Add schedule
* Edit schedule
* Clear visual distinction between available and occupied slots

## Appointments

Make appointments one of the strongest screens.

Top controls:

* Book Appointment
* Search
* Filter by doctor
* Filter by date
* Filter by status

Appointment table/calendar hybrid:

* Appointment ID
* Patient
* Doctor
* Specialization
* Date
* Time
* Room
* Status

Status chips:
Scheduled
Confirmed
Completed
Cancelled

Include a booking modal with:

* Patient selection
* Doctor selection
* Date
* Time
* Room
* Validation states
* Doctor availability indicator

## Prescriptions

Create a prescription management interface.

Table:

* Prescription ID
* Patient
* Doctor
* Diagnosis
* Medicine
* Next Visit
* Remarks
* Date
* Actions

Include:

* Database-powered search
* Add Prescription modal
* Edit/Delete
* Patient prescription history

Add a visually polished prescription detail view resembling a real clinical document.

## Billing

Create a billing dashboard with:

* Total Revenue
* Paid Bills
* Pending Bills
* Today's Revenue

Billing table:

* Bill ID
* Patient
* Doctor
* Appointment
* Consultation Fee
* Payment Method
* Payment Status
* Date
* Actions

Payment status:
Paid
Pending
Failed

Payment methods:
Cash
Card
UPI
Online

Include:

* Create Bill modal
* Automatically calculated doctor fee
* Update payment status
* Delete confirmation
* Invoice/detail view

## Emergency

This page should feel operationally urgent without becoming visually chaotic.

Top summary:

* Critical
* High
* Medium
* Low

Emergency case table:

* Case ID
* Patient
* Priority
* Assigned Doctor
* Arrival Time
* Status
* Action

Use clear priority indicators:
Critical
High
Medium
Low

Create an emergency case modal with:

* Patient
* Priority
* Assigned doctor
* Symptoms/notes
* Status

Critical cases should stand out immediately.

## Lab Tests

Create a diagnostic/laboratory management page.

Summary cards:

* Total Tests
* Pending
* Completed
* Today's Tests

Table:

* Test ID
* Patient
* Test Type
* Doctor
* Ordered Date
* Result
* Cost
* Status
* Actions

Test types:
CBC
MRI
X-Ray
and other diagnostic tests.

Create result-entry and update workflows.

## Bed Allocation

Create a highly visual bed management screen.

Show hospital wards:

* ICU
* Private
* General

Display beds as an interactive grid with states:

* Available
* Occupied
* Reserved / unavailable

Each bed should show:

* Bed number
* Ward
* Patient if occupied
* Admission date
* Status

Actions:

* Admit Patient
* Discharge Patient
* View admission details

Make bed conflicts and duplicate admissions impossible to miss through clear validation and confirmation dialogs.

## Feedback

Create a patient feedback page.

Show:

* Average rating
* Rating distribution
* Recent feedback
* Number of responses

Feedback table:

* Patient
* Date
* Rating
* Comment
* Actions

Use 1–5 star ratings while maintaining a professional medical dashboard appearance.

## Reports

Create a polished analytics/reporting section.

Reports available:

* Active Appointments
* Hospital Revenue Summary
* Patient History
* Pending Bills
* Revenue by Doctor
* Monthly Patient Trends
* Bed Occupancy History
* Doctor Workload Analysis

Layout:

* Report selector/sidebar
* Large data visualization/report area
* Filters
* Date range
* Export to CSV button

Use charts where appropriate:

* Line charts
* Bar charts
* Donut charts
* Area charts
* Tables

Do not overuse charts; prioritize actionable information.

## Audit Logs

Create an admin-only audit log page.

Table:

* Timestamp
* User
* Action
* Module
* Record ID
* Description
* Severity

Include search and filters.

## Modals and States

Design reusable components for:

* Add
* Edit
* Delete confirmation
* Success confirmation
* Error state
* Validation error
* Empty state
* Loading state
* Search results
* Notification popover
* Logout confirmation

Delete dialogs should clearly explain consequences when cascade deletion can affect related records.

## Role-Based UI

Create two navigation variants:

ADMIN:
Full access to all modules.

RECEPTIONIST:
Dashboard
Patients
Appointments
Billing
Lab Tests
Bed Allocation

Clearly hide inaccessible modules rather than simply disabling them.

## Responsive Behavior

Although this is primarily a desktop application, design the system so the layout can adapt to smaller desktop/tablet widths.

Desktop target:
1440 × 1024

Also provide:
1280 × 800 variant

Use responsive tables, collapsible sidebar behavior, and sensible content wrapping.

## Component System

Create a reusable design system in Figma:

* Color variables
* Typography styles
* Spacing variables
* Buttons
* Inputs
* Selects
* Search bars
* Tables
* Cards
* Badges/chips
* Tabs
* Modals
* Toasts
* Dropdowns
* Avatars
* Navigation items
* Status indicators
* Charts
* Calendar elements

Maintain consistent components across every page.

## Visual Quality

The final design should look like a **production-ready hospital enterprise application** comparable to modern healthcare SaaS software.

Focus on:

* Strong information hierarchy
* Excellent alignment
* Consistent spacing
* Clear typography
* Accessible contrast
* Fast scanning
* Minimal unnecessary decoration
* Clear distinction between normal operations and emergency states

Create a cohesive Figma file with:

1. Design system
2. Login
3. Dashboard
4. Patients
5. Patient details
6. Doctors
7. Doctor schedule
8. Appointments
9. Prescriptions
10. Billing
11. Emergency
12. Lab Tests
13. Bed Allocation
14. Feedback
15. Reports
16. Audit Logs
17. Relevant modals and interaction states

Use realistic sample hospital data throughout the mockups so the interface feels like a functioning product, not a wireframe.
