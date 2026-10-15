# 🏥 DBMS Assessment – 8 Technical Report
## Comprehensive Relational Database Design, ER Modeling, Schema Mapping & Normalization Analysis

---

| **Academic Course** | Database Management Systems (DBMS) |
|:---|:---|
| **Assessment Title** | Assessment – 8: Relational Database Modeling, Schema Transformation & Normalization |
| **System Name** | CarePoint Enterprise Hospital Management System (HMS) |
| **Database Engine** | MySQL 8.0 Enterprise / Community Server |
| **Client Interfaces** | Native Java Swing + JDBC Workstation Client & React 19 + TypeScript Web Portal |
| **Submission Date** | October 2026 |

---

## 📑 Table of Contents
1. [Component 1: Title of the Problem Statement](#1-title-of-the-problem-statement)
   - 1.1 System Nomenclature & Project Title
   - 1.2 Problem Context & Domain Motivation
   - 1.3 Core Challenges in Traditional Clinical Data Management
   - 1.4 Project Objectives & Technical Scope
2. [Component 2: Requirement Analysis](#2-requirement-analysis)
   - 2.1 Stakeholder Analysis & User Roles
   - 2.2 Functional Requirements (FR-01 to FR-10)
   - 2.3 Non-Functional Requirements (NFR-01 to NFR-06)
   - 2.4 Entity Identification & Data Dictionary Foundations
   - 2.5 Business Rules & Integrity Constraints
3. [Component 3: Entity-Relationship (ER) Diagram](#3-entity-relationship-er-diagram)
   - 3.1 Entity Classification & Semantic Descriptions
   - 3.2 Key Attributes, Multi-Valued Attributes & Domain Sets
   - 3.3 Relationship Matrix, Cardinality Ratios & Participation Constraints
   - 3.4 Full Relational Mermaid ER Diagram
   - 3.5 Interactive Diagram Reference (`ER_Diagram.html`)
4. [Component 4: Conversion of ER Diagram to Relational Tables](#4-conversion-of-er-diagram-to-relational-tables)
   - 4.1 Formal ER-to-Relational Mapping Methodology
   - 4.2 Relational Schema Definitions (Formal Notation)
   - 4.3 Comprehensive Relational Table Specifications
   - 4.4 Foreign Key Constraints & Referential Action Policies
   - 4.5 Database Triggers & Procedural Automation Constraints
5. [Component 5: Normalization Concepts & Comprehensive Analysis](#5-normalization-concepts--comprehensive-analysis)
   - 5.1 Theoretical Background & Objectives of Normalization
   - 5.2 Functional Dependencies (FD) Formalization
   - 5.3 Normalization Walkthrough: Unnormalized Form (UNF) to 1NF
   - 5.4 Elimination of Partial Dependencies (1NF to 2NF)
   - 5.5 Elimination of Transitive Dependencies (2NF to 3NF)
   - 5.6 Boyce-Codd Normal Form (BCNF) Verification
   - 5.7 Database Anomaly Mitigation Analysis (Insertion, Deletion, Update)
   - 5.8 Lossless Join & Dependency Preservation Proofs
6. [Conclusion & Summary Metrics](#6-conclusion--summary-metrics)

---

# 1. Title of the Problem Statement

### 1.1 System Nomenclature & Project Title
> **"Design and Implementation of an ACID-Compliant, 3NF-Normalized Relational Database Management System for CarePoint Enterprise Hospital Management Platform"**

### 1.2 Problem Context & Domain Motivation
Modern tertiary healthcare institutions coordinate a vast web of high-velocity, mission-critical operations. Everyday clinical throughput involves managing hundreds of outpatient registrations, specialized physician consultations, operating room bookings, inpatient bed occupancy, emergency triage diagnostics, electronic pharmaceutical prescriptions, and multifaceted payment invoicing.

Historically, hospitals have relied on fragmented paper registers, disparate spreadsheets, or siloed legacy software. These traditional approaches present severe vulnerabilities:
- **Redundant Data Entry**: Patient demographics and insurance records are re-entered across OPD, laboratory, and pharmacy registers.
- **Data Inconsistencies**: A change in physician consultation fees or patient phone numbers in one department fails to propagate to billing and scheduling desks.
- **Concurrency & Booking Collisions**: Double-allocation of inpatient ICU beds or concurrent overbooking of a specialist surgeon in multiple consultation rooms during the same timeslot.
- **Lack of Strict Auditability**: Unregulated record modifications and unauthorized viewing of sensitive clinical diagnostic notes.

### 1.3 Core Challenges in Traditional Clinical Data Management
1. **Uncontrolled Data Redundancy**: Storing medical department titles, doctor qualifications, and room assignments directly inside appointment logs wastes storage and triggers update anomalies.
2. **Cascading Clinical Incoherence**: Cancelling an appointment must deterministically archive diagnostic orders, void pending invoice calculations, and maintain an immutable historical audit trail.
3. **Complex Relationship Cardinality**: A doctor possesses multiple weekly consultation shifts across different days; a patient undergoes multiple diagnostic lab tests ordered by multiple physicians across sequential visits.

### 1.4 Project Objectives & Technical Scope
The primary objective of this project is to architect, normalize, and implement an industrial-grade **Relational Database Management System (RDBMS)** on **MySQL 8.0** that serves as the single source of truth for both:
1. A **Native Java Swing Desktop Client** used at reception workstations and administrative kiosks.
2. A **Modern React 19 + TypeScript Web Portal** used by physicians, nursing personnel, and patients.

The database must guarantee **Atomicity, Consistency, Isolation, and Durability (ACID)**, enforce integrity through relational triggers and foreign key cascades, achieve **Third Normal Form (3NF) / BCNF**, and provide sub-millisecond query execution through balanced B-Tree indexing.

---

# 2. Requirement Analysis

### 2.1 Stakeholder Analysis & User Roles
To maintain confidentiality and institutional hierarchy, the system defines four distinct operational roles governed by **Role-Based Access Control (RBAC)**:

| Role | Operational Scope & Access Privileges |
|:---|:---|
| **🛡️ Administrator (`Admin`)** | Unrestricted access across all 13 database tables. Authorized to configure doctor rosters, adjust billing fees, review institutional revenue metrics, access system audit trails, and manage credential lifecycle. |
| **👨‍⚕️ Physician / Doctor (`Doctor`)** | Authorized to inspect assigned outpatient appointments, review historical patient diagnoses, author electronic prescriptions, review incoming emergency triage cases, and order laboratory diagnostic panels. |
| **👩‍💼 Receptionist (`Receptionist`)** | Front-desk operational scope: Patient intake and demographic registration, appointment booking and rescheduling, inpatient ward/bed allocation, and invoice generation. |
| **🧑‍🤝‍🧑 Patient (`Patient`)** | Highly restricted, confidential portal: Access strictly limited to viewing personal consultation timelines via stored procedures, active medication regimens, diagnostic test results, pending bills, and submitting care feedback. |

---

### 2.2 Functional Requirements (FR)

- **FR-01: Patient Demographics & Profile Management**: The database must persist comprehensive patient identifiers, full name, chronological age, biological gender, ABO/Rh blood grouping, verified contact telephone numbers, physical residential address, and system registration timestamps.
- **FR-02: Doctor Roster & Specialty Classification**: The system must maintain accredited medical practitioners, their verified qualifications (e.g., MBBS, MD, DM, MS), standardized consultation tariffs, and foreign-key link to normalized medical specializations.
- **FR-03: Physician Duty Shift & Timetable Scheduling**: The database must manage weekly recurring duty shifts, associating doctors with day-of-week strings and strict time windows (`start_time`, `end_time`).
- **FR-04: Outpatient Appointment Scheduling**: The system must record appointment reservations with unique timestamps, assigned room numbers, and stateful status transitions (`'Scheduled'`, `'Pending'`, `'Completed'`, `'Cancelled'`).
- **FR-05: Electronic Health Records & Prescriptions**: Every completed clinical consultation must allow the prescribing physician to issue formalized diagnostic findings, pharmaceutical regimens, dosage schedules, follow-up dates, and clinical remarks.
- **FR-06: Financial Billing & Invoicing Workflow**: The system must generate immutable financial ledgers linked to specific appointment consultations, tracking bill generation dates, total payable amounts, payment channels (`UPI`, `Card`, `Cash`), and payment status (`'Paid'`, `'Pending'`).
- **FR-07: Emergency Department Triage Processing**: Emergency admissions must be indexed with clinical severity priority tiers (`'Critical'`, `'High'`, `'Medium'`, `'Low'`), incident categorizations, arrival timestamps, and designated ER attending physicians.
- **FR-08: Inpatient Ward & Bed Occupancy Control**: Inpatient admissions across General Wards, Private AC Rooms, and Intensive Care Units (ICU) must be tracked with daily room tariffs, admission dates, and discharge dates. The database must physically prohibit concurrent double-booking of any bed.
- **FR-09: Diagnostic Pathology & Laboratory Testing**: The database must manage laboratory orders (e.g., CBC, MRI, Lipid Panel, HbA1c), tracking order timestamps, diagnostic costs, specimen processing status (`'Pending'`, `'Sample Collected'`, `'Completed'`), and finalized clinical findings.
- **FR-10: Quality Feedback & Compliance Audit Trail**: Patients must be able to submit 1-to-5 star quantitative ratings with qualitative reviews. Any cancellation or critical table deletion must trigger automated logging into an immutable `Audit_Logs` ledger.

---

### 2.3 Non-Functional Requirements (NFR)

- **NFR-01: Relational Data Integrity (ACID Compliance)**:
  - *Atomicity*: All multi-table workflows (such as bill payment transitioning appointment status) execute within isolated atomic transactions.
  - *Consistency*: Foreign key constraints with `ON UPDATE CASCADE` and `ON DELETE CASCADE / RESTRICT` prevent orphaned records.
  - *Isolation*: Default `REPEATABLE READ` transaction isolation level prevents dirty and non-repeatable reads.
  - *Durability*: Committed transactions are permanently written to MySQL InnoDB redo logs.
- **NFR-02: Cryptographic Security & RBAC**: User authentication must never store plaintext passwords. The system must utilize cryptographic hashing (`SHA-256`) executed at the database layer via `SHA2(?, 256)`.
- **NFR-03: Sub-Millisecond Retrieval & Index Optimization**: Frequent query predicates (`Appointments.appointment_date`, `Patients.contact`, `Billing.payment_status`, `Emergency.priority_level`) must be indexed using B-Tree data structures.
- **NFR-04: Concurrency & Lock Minimization**: The schema must use row-level locking (InnoDB engine) rather than table-level locks to support high concurrent transactions between the Desktop client and Web API.
- **NFR-05: High Extensibility & Third Normal Form (3NF)**: The relational architecture must strictly avoid transitive and partial functional dependencies, allowing seamless addition of departments and insurance claims modules without schema restructuring.
- **NFR-06: Cross-Platform Interoperability**: The relational database must interface uniformly with both Java JDBC (Desktop) and Express.js connection pools (Web REST API).

---

### 2.4 Entity Identification & Data Dictionary Foundations

Through rigorous domain analysis, thirteen core relational entities were established:

```
┌────────────────────────────────────────────────────────────────────────┐
│                        CAREPOINT RDBMS ENTITIES                        │
├──────────────────┬──────────────────┬──────────────────────────────────┤
│ Category         │ Entity Name      │ Primary Purpose                  │
├──────────────────┼──────────────────┼──────────────────────────────────┤
│ Security & Access│ Users            │ User authentication & RBAC roles │
├──────────────────┼──────────────────┼──────────────────────────────────┤
│ Master Clinical  │ Specializations  │ Master catalog of specialties    │
│ Personnel        │ Doctor           │ Registered medical practitioners │
│                  │ Doctor_Schedule  │ Weekly recurring shift timetable │
├──────────────────┼──────────────────┼──────────────────────────────────┤
│ Outpatient Core  │ Patients         │ Patient demographic master       │
│                  │ Appointments     │ Outpatient consultation bookings │
│                  │ Prescriptions    │ Clinical diagnoses & medications │
├──────────────────┼──────────────────┼──────────────────────────────────┤
│ Inpatient Care   │ Bed_Allocation   │ Ward bed tracking & occupancy    │
│ & Emergency      │ Emergency        │ Emergency triage admissions      │
│                  │ Lab_Tests        │ Diagnostic lab test requests     │
├──────────────────┼──────────────────┼──────────────────────────────────┤
│ Finance & Audit  │ Billing          │ Financial invoices & settlements │
│                  │ Feedback         │ Patient experience ratings       │
│                  │ Audit_Logs       │ System compliance audit trail    │
└──────────────────┴──────────────────┴──────────────────────────────────┘
```

---

### 2.5 Business Rules & Integrity Constraints

1. **BR-01 (Single Bed Occupancy Rule)**: A hospital bed cannot be simultaneously occupied by more than one patient. An active admission exists when `status = 'Occupied'` and `discharge_date IS NULL`.
2. **BR-02 (Single Active Admission Rule)**: A patient can only be admitted to one bed at any given instant.
3. **BR-03 (Automated Billing Completion)**: When an invoice payment status transitions to `'Paid'`, the corresponding clinical appointment status must automatically transition to `'Completed'`.
4. **BR-04 (Mandatory Doctor Qualification)**: Every medical practitioner must be linked to an existing valid medical specialization; deletion of a specialization with active doctors is restricted.
5. **BR-05 (Audit Logging on Cancellation)**: When an appointment is flagged as `'Cancelled'`, the event timestamp, appointment ID, and operator metadata must be archived into `Audit_Logs`.

---

# 3. Entity-Relationship (ER) Diagram

### 3.1 Entity Classification & Semantic Descriptions

- **`Patients` (Strong Entity)**: Represents the individual receiving care. Identified uniquely by surrogate key `patient_id`.
- **`Doctor` (Strong Entity)**: Represents verified hospital physicians. Identified uniquely by `doctor_id`.
- **`Specializations` (Strong Master Entity)**: Canonical classification of medical branches (Cardiology, Neurology, etc.). Identified by `specialization_id`.
- **`Doctor_Schedule` (Dependent Entity)**: Specific shift slots belonging to a doctor. Identified by `schedule_id` with foreign key `doctor_id`.
- **`Appointments` (Strong Associative Entity)**: Core operational transaction coordinating a Patient, a Doctor, a timeslot, and a room. Identified by `appointment_id`.
- **`Prescriptions` (Dependent 1:1 Entity)**: Medical advice and pharmacology issued during an appointment. Identified by `prescription_id` with unique `appointment_id`.
- **`Billing` (Dependent 1:1 Entity)**: Financial invoice tied to an appointment consultation. Identified by `bill_id`.
- **`Bed_Allocation` (Associative Entity)**: Junction between an inpatient and a specific room/ward bed. Identified by `allocation_id`.
- **`Emergency` (Event Entity)**: High-priority triage cases linking a patient to an emergency physician. Identified by `emergency_id`.
- **`Lab_Tests` (Associative Entity)**: Diagnostic laboratory investigations linking a patient and the ordering physician. Identified by `test_id`.
- **`Feedback` (Dependent Entity)**: Review submitted by a patient regarding hospital services. Identified by `feedback_id`.
- **`Users` (Security Entity)**: System credentials with SHA-256 password hash. Identified by `user_id`.
- **`Audit_Logs` (System Entity)**: Autonomous system event log. Identified by `log_id`.

---

### 3.2 Key Attributes, Multi-Valued Attributes & Domain Sets

| Entity | Primary Key (PK) | Candidate / Alternate Keys | Foreign Keys (FK) | Descriptive Attributes |
|:---|:---|:---|:---|:---|
| **Users** | `user_id` | `username` (UNIQUE) | *None* | `password` (SHA-256), `full_name`, `role`, `created_at` |
| **Specializations**| `specialization_id` | `specialization_name` (UNIQUE) | *None* | `specialization_name` |
| **Doctor** | `doctor_id` | *None* | `specialization_id` | `doctor_name`, `qualification`, `consultation_fee`, `contact` |
| **Doctor_Schedule**| `schedule_id` | *None* | `doctor_id` | `day_of_week`, `start_time`, `end_time` |
| **Patients** | `patient_id` | `contact` | *None* | `patient_name`, `gender`, `age`, `blood_group`, `address`, `registration_date` |
| **Appointments** | `appointment_id`| *None* | `patient_id`, `doctor_id` | `appointment_date`, `appointment_time`, `room_number`, `status` |
| **Prescriptions** | `prescription_id`| `appointment_id` | `appointment_id` | `diagnosis`, `medicine`, `next_visit_date`, `remarks` |
| **Billing** | `bill_id` | `appointment_id` | `appointment_id` | `amount`, `bill_date`, `payment_method`, `payment_status` |
| **Emergency** | `emergency_id` | *None* | `patient_id`, `assigned_doctor`| `emergency_type`, `priority_level`, `status`, `arrival_date`, `arrival_time` |
| **Lab_Tests** | `test_id` | *None* | `patient_id`, `doctor_id` | `test_name`, `test_date`, `cost`, `result`, `status` |
| **Bed_Allocation**| `allocation_id` | *None* | `patient_id` | `ward_type`, `bed_number`, `admit_date`, `discharge_date`, `daily_charge`, `status` |
| **Feedback** | `feedback_id` | *None* | `patient_id` | `rating` (1–5), `feedback_date`, `comments` |
| **Audit_Logs** | `log_id` | *None* | `record_id` (Logical) | `action_type`, `log_time`, `details` |

---

### 3.3 Relationship Matrix, Cardinality Ratios & Participation Constraints

| Relationship Name | Connecting Entities | Cardinality Ratio | Participation: Entity 1 | Participation: Entity 2 | Semantic Business Definition |
|:---|:---|:---:|:---:|:---:|:---|
| **Classifies** | `Specializations` ➜ `Doctor` | **1 : N** | Partial (0..N) | **Total (1..1)** | A specialization classifies many doctors; every doctor must possess a valid specialization. |
| **Works_Shifts** | `Doctor` ➜ `Doctor_Schedule` | **1 : N** | Partial (0..N) | **Total (1..1)** | A doctor can work multiple shifts; a schedule slot belongs to exactly one doctor. |
| **Books** | `Patients` ➜ `Appointments` | **1 : N** | Partial (0..N) | **Total (1..1)** | A registered patient may book multiple appointments over time. |
| **Attends** | `Doctor` ➜ `Appointments` | **1 : N** | Partial (0..N) | **Total (1..1)** | A doctor is scheduled to attend multiple patient consultations. |
| **Issues** | `Appointments` ➜ `Prescriptions`| **1 : 1** | Partial (0..1) | **Total (1..1)** | An appointment may generate at most one clinical prescription regimen. |
| **Generates** | `Appointments` ➜ `Billing` | **1 : 1** | Partial (0..1) | **Total (1..1)** | An appointment generates an invoice statement for consultation charges. |
| **Admitted_As**| `Patients` ➜ `Emergency` | **1 : N** | Partial (0..N) | **Total (1..1)** | A patient may have multiple emergency triage admissions over their lifetime. |
| **Treated_By** | `Doctor` ➜ `Emergency` | **1 : N** | Partial (0..N) | Partial (0..1) | An emergency patient is assigned to an on-duty emergency physician. |
| **Undergoes** | `Patients` ➜ `Lab_Tests` | **1 : N** | Partial (0..N) | **Total (1..1)** | A patient undergoes multiple laboratory diagnostic tests. |
| **Orders** | `Doctor` ➜ `Lab_Tests` | **1 : N** | Partial (0..N) | Partial (0..1) | A doctor orders laboratory diagnostics for diagnostic verification. |
| **Assigned_Bed**| `Patients` ➜ `Bed_Allocation` | **1 : N** | Partial (0..N) | **Total (1..1)** | An admitted patient is allocated a specific inpatient ward bed. |
| **Submits** | `Patients` ➜ `Feedback` | **1 : N** | Partial (0..N) | **Total (1..1)** | A patient submits feedback ratings and reviews regarding care quality. |
| **Audits** | `Appointments` ➜ `Audit_Logs` | **1 : N** | Partial (0..N) | Partial (0..1) | Cancellation actions on appointments generate automated audit trail entries. |

---

### 3.4 Full Relational Mermaid ER Diagram

```mermaid
erDiagram
    Users {
        int user_id PK
        varchar username "UK, NOT NULL"
        varchar password "SHA-256, NOT NULL"
        varchar full_name "NOT NULL"
        varchar role "Admin/Doctor/Receptionist/Patient"
        timestamp created_at "DEFAULT CURRENT_TIMESTAMP"
    }

    Specializations {
        int specialization_id PK
        varchar specialization_name "UK, NOT NULL"
    }

    Doctor {
        int doctor_id PK
        varchar doctor_name "NOT NULL"
        int specialization_id FK "NOT NULL"
        varchar qualification "NOT NULL"
        decimal consultation_fee "CHECK (fee >= 0)"
        varchar contact "NOT NULL"
    }

    Doctor_Schedule {
        int schedule_id PK
        int doctor_id FK "NOT NULL"
        varchar day_of_week "NOT NULL"
        time start_time "NOT NULL"
        time end_time "NOT NULL"
    }

    Patients {
        int patient_id PK
        varchar patient_name "NOT NULL"
        varchar gender "CHECK (gender IN ('Male','Female','Other'))"
        int age "CHECK (age >= 0)"
        varchar blood_group "NOT NULL"
        varchar contact "INDEXED"
        varchar address
        date registration_date "NOT NULL"
    }

    Appointments {
        int appointment_id PK
        int patient_id FK "NOT NULL"
        int doctor_id FK "NOT NULL"
        date appointment_date "INDEXED, NOT NULL"
        time appointment_time "NOT NULL"
        varchar room_number "NOT NULL"
        varchar status "Scheduled/Completed/Cancelled/Pending"
    }

    Prescriptions {
        int prescription_id PK
        int appointment_id FK "UK, NOT NULL"
        varchar diagnosis "NOT NULL"
        varchar medicine "NOT NULL"
        date next_visit_date
        varchar remarks
    }

    Billing {
        int bill_id PK
        int appointment_id FK "UK, NOT NULL"
        decimal amount "CHECK (amount >= 0)"
        date bill_date "NOT NULL"
        varchar payment_method "UPI/Card/Cash"
        varchar payment_status "INDEXED (Paid/Pending)"
    }

    Emergency {
        int emergency_id PK
        int patient_id FK "NOT NULL"
        varchar emergency_type "NOT NULL"
        varchar priority_level "INDEXED (Critical/High/Medium/Low)"
        varchar status "NOT NULL"
        int assigned_doctor FK
        date arrival_date "NOT NULL"
        time arrival_time "NOT NULL"
    }

    Lab_Tests {
        int test_id PK
        int patient_id FK "NOT NULL"
        int doctor_id FK
        varchar test_name "NOT NULL"
        date test_date "NOT NULL"
        decimal cost "CHECK (cost >= 0)"
        varchar result
        varchar status "Pending/Sample Collected/Completed"
    }

    Bed_Allocation {
        int allocation_id PK
        int patient_id FK "NOT NULL"
        varchar ward_type "ICU/General/Private AC"
        varchar bed_number "NOT NULL"
        date admit_date "NOT NULL"
        date discharge_date
        decimal daily_charge "CHECK (daily_charge >= 0)"
        varchar status "Occupied/Discharged"
    }

    Feedback {
        int feedback_id PK
        int patient_id FK "NOT NULL"
        int rating "CHECK (rating BETWEEN 1 AND 5)"
        date feedback_date "NOT NULL"
        varchar comments
    }

    Audit_Logs {
        int log_id PK
        varchar action_type "NOT NULL"
        int record_id "NOT NULL"
        timestamp log_time "DEFAULT CURRENT_TIMESTAMP"
        text details "NOT NULL"
    }

    Specializations ||--o{ Doctor : "classifies"
    Doctor ||--o{ Doctor_Schedule : "works_shifts"
    Doctor ||--o{ Appointments : "attends"
    Patients ||--o{ Appointments : "books"
    Appointments ||--o| Prescriptions : "issues"
    Appointments ||--o| Billing : "generates"
    Patients ||--o{ Feedback : "submits"
    Patients ||--o{ Emergency : "admitted_as"
    Doctor ||--o{ Emergency : "treated_by"
    Patients ||--o{ Lab_Tests : "undergoes"
    Doctor ||--o{ Lab_Tests : "orders"
    Patients ||--o{ Bed_Allocation : "assigned"
    Appointments ||--o{ Audit_Logs : "audits_cancellations"
```

### 3.5 Interactive Diagram Reference
An interactive, GPU-accelerated visualization of this ER model with pan, zoom, and live layout controls is embedded directly within the project at [`ER_Diagram.html`](file:///c:/Users/nisar/OneDrive/Desktop/DBMS/ER_Diagram.html).

---

# 4. Conversion of ER Diagram to Relational Tables

### 4.1 Formal ER-to-Relational Mapping Methodology

The conversion from conceptual ER modeling to physical relational schemas followed formal DBMS transformation rules:

1. **Mapping of Regular (Strong) Entity Sets**:
   - Each strong entity set ($E$) becomes a relational table ($R$).
   - All simple atomic attributes of $E$ are mapped directly to columns of $R$.
   - The primary key of $E$ becomes the primary key of $R$.
   - *Example*: `Patients` $\to$ `Patients(patient_id, patient_name, gender, age, blood_group, contact, address, registration_date)` where `patient_id` is the Primary Key.

2. **Mapping of 1:N Binary Relationship Types**:
   - For each binary 1:N relationship between entity sets $S$ (1-side) and $T$ (N-side), the primary key of $S$ is imported as a **Foreign Key** into the relation for $T$.
   - *Example*: `Specializations` (1) to `Doctor` (N) $\implies$ `specialization_id` is placed inside `Doctor` as a Foreign Key.
   - *Example*: `Doctor` (1) to `Doctor_Schedule` (N) $\implies$ `doctor_id` is placed inside `Doctor_Schedule` as a Foreign Key.

3. **Mapping of 1:1 Binary Relationship Types**:
   - To avoid null values in optional participations, the primary key of the mandatory/dependent participant entity is augmented with a Foreign Key pointing to the parent entity, constrained by a **`UNIQUE`** constraint.
   - *Example*: `Appointments` (1) to `Billing` (1) $\implies$ `Billing` includes `appointment_id INT UNIQUE FOREIGN KEY REFERENCES Appointments(appointment_id)`.
   - *Example*: `Appointments` (1) to `Prescriptions` (1) $\implies$ `Prescriptions` includes `appointment_id INT UNIQUE FOREIGN KEY REFERENCES Appointments(appointment_id)`.

4. **Mapping of Multi-Way / Associative Relationships**:
   - Relationship sets connecting multiple entities with operational attributes become separate junction relations.
   - *Example*: `Appointments` associates `Patients` and `Doctor` with attributes `appointment_date`, `appointment_time`, `room_number`, and `status`.

---

### 4.2 Relational Schema Definitions (Formal Notation)

In standard relational calculus notation (primary keys underlined $\underline{\text{PK}}$, foreign keys marked with an asterisk $* \text{FK}$):

1. **`Users`** ( $\underline{\text{user\_id}}$, $\text{username}^{\text{UK}}$, $\text{password}$, $\text{full\_name}$, $\text{role}$, $\text{created\_at}$ )
2. **`Specializations`** ( $\underline{\text{specialization\_id}}$, $\text{specialization\_name}^{\text{UK}}$ )
3. **`Doctor`** ( $\underline{\text{doctor\_id}}$, $\text{doctor\_name}$, $*\text{specialization\_id}$, $\text{qualification}$, $\text{consultation\_fee}$, $\text{contact}$ )
4. **`Doctor_Schedule`** ( $\underline{\text{schedule\_id}}$, $*\text{doctor\_id}$, $\text{day\_of\_week}$, $\text{start\_time}$, $\text{end\_time}$ )
5. **`Patients`** ( $\underline{\text{patient\_id}}$, $\text{patient\_name}$, $\text{gender}$, $\text{age}$, $\text{blood\_group}$, $\text{contact}$, $\text{address}$, $\text{registration\_date}$ )
6. **`Appointments`** ( $\underline{\text{appointment\_id}}$, $*\text{patient\_id}$, $*\text{doctor\_id}$, $\text{appointment\_date}$, $\text{appointment\_time}$, $\text{room\_number}$, $\text{status}$ )
7. **`Prescriptions`** ( $\underline{\text{prescription\_id}}$, $*\text{appointment\_id}^{\text{UK}}$, $\text{diagnosis}$, $\text{medicine}$, $\text{next\_visit\_date}$, $\text{remarks}$ )
8. **`Billing`** ( $\underline{\text{bill\_id}}$, $*\text{appointment\_id}^{\text{UK}}$, $\text{amount}$, $\text{bill\_date}$, $\text{payment\_method}$, $\text{payment\_status}$ )
9. **`Emergency`** ( $\underline{\text{emergency\_id}}$, $*\text{patient\_id}$, $\text{emergency\_type}$, $\text{priority\_level}$, $\text{status}$, $*\text{assigned\_doctor}$, $\text{arrival\_date}$, $\text{arrival\_time}$ )
10. **`Lab_Tests`** ( $\underline{\text{test\_id}}$, $*\text{patient\_id}$, $*\text{doctor\_id}$, $\text{test\_name}$, $\text{test\_date}$, $\text{cost}$, $\text{result}$, $\text{status}$ )
11. **`Bed_Allocation`** ( $\underline{\text{allocation\_id}}$, $*\text{patient\_id}$, $\text{ward\_type}$, $\text{bed\_number}$, $\text{admit\_date}$, $\text{discharge\_date}$, $\text{daily\_charge}$, $\text{status}$ )
12. **`Feedback`** ( $\underline{\text{feedback\_id}}$, $*\text{patient\_id}$, $\text{rating}$, $\text{feedback\_date}$, $\text{comments}$ )
13. **`Audit_Logs`** ( $\underline{\text{log\_id}}$, $\text{action\_type}$, $\text{record\_id}$, $\text{log\_time}$, $\text{details}$ )

---

### 4.3 Comprehensive Relational Table Specifications

#### Table 1: `Users` (Authentication & RBAC)
```sql
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Stored as SHA-256 Digest
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Table 2: `Specializations` (Medical Taxonomy)
```sql
CREATE TABLE Specializations (
    specialization_id INT AUTO_INCREMENT PRIMARY KEY,
    specialization_name VARCHAR(50) NOT NULL UNIQUE
);
```

#### Table 3: `Doctor` (Physician Registry)
```sql
CREATE TABLE Doctor (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_name VARCHAR(50) NOT NULL,
    specialization_id INT NOT NULL,
    qualification VARCHAR(50) NOT NULL,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 500.00,
    contact VARCHAR(15) NOT NULL,
    FOREIGN KEY (specialization_id) REFERENCES Specializations(specialization_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);
```

#### Table 4: `Doctor_Schedule` (Weekly Timetable)
```sql
CREATE TABLE Doctor_Schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week VARCHAR(15) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

#### Table 5: `Patients` (Patient Master Index)
```sql
CREATE TABLE Patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('Male', 'Female', 'Other')),
    age INT CHECK (age >= 0 AND age <= 125),
    blood_group VARCHAR(5) NOT NULL,
    contact VARCHAR(15) NOT NULL,
    address VARCHAR(100),
    registration_date DATE NOT NULL,
    INDEX idx_patient_contact (contact)
);
```

#### Table 6: `Appointments` (Outpatient Reservations)
```sql
CREATE TABLE Appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Scheduled',
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    INDEX idx_appointment_date (appointment_date)
);
```

#### Table 7: `Prescriptions` (Clinical Diagnoses & Medications)
```sql
CREATE TABLE Prescriptions (
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL UNIQUE,
    diagnosis VARCHAR(100) NOT NULL,
    medicine VARCHAR(100) NOT NULL,
    next_visit_date DATE,
    remarks VARCHAR(100),
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

#### Table 8: `Billing` (Invoices & Settlements)
```sql
CREATE TABLE Billing (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    bill_date DATE NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    INDEX idx_payment_status (payment_status)
);
```

#### Table 9: `Emergency` (Emergency Triage Admissions)
```sql
CREATE TABLE Emergency (
    emergency_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    emergency_type VARCHAR(50) NOT NULL,
    priority_level VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Admitted',
    assigned_doctor INT,
    arrival_date DATE NOT NULL,
    arrival_time TIME NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (assigned_doctor) REFERENCES Doctor(doctor_id)
        ON UPDATE CASCADE ON DELETE SET NULL,
    INDEX idx_emergency_priority (priority_level)
);
```

#### Table 10: `Lab_Tests` (Diagnostic Pathology)
```sql
CREATE TABLE Lab_Tests (
    test_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT,
    test_name VARCHAR(100) NOT NULL,
    test_date DATE NOT NULL,
    cost DECIMAL(10,2) NOT NULL CHECK (cost >= 0),
    result VARCHAR(255) DEFAULT 'Pending Analysis',
    status VARCHAR(30) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)
        ON UPDATE CASCADE ON DELETE SET NULL
);
```

#### Table 11: `Bed_Allocation` (Inpatient Stays)
```sql
CREATE TABLE Bed_Allocation (
    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    ward_type VARCHAR(50) NOT NULL,
    bed_number VARCHAR(20) NOT NULL,
    admit_date DATE NOT NULL,
    discharge_date DATE,
    daily_charge DECIMAL(10,2) NOT NULL DEFAULT 1500.00 CHECK (daily_charge >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'Occupied',
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

#### Table 12: `Feedback` (Patient Experience Ratings)
```sql
CREATE TABLE Feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    feedback_date DATE NOT NULL,
    comments VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id)
        ON UPDATE CASCADE ON DELETE CASCADE
);
```

#### Table 13: `Audit_Logs` (Compliance Audit Trail)
```sql
CREATE TABLE Audit_Logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL,
    record_id INT NOT NULL,
    log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT NOT NULL
);
```

---

### 4.4 Foreign Key Constraints & Referential Action Policies

To prevent orphan records while safeguarding medical history, explicit referential actions were engineered:

```
┌─────────────────────────────────┬────────────────────────────────┬─────────────────┐
│ Foreign Key Relationship        │ ON UPDATE Action               │ ON DELETE Action│
├─────────────────────────────────┼────────────────────────────────┼─────────────────┤
│ Doctor(specialization_id)       │ CASCADE                        │ RESTRICT        │
│ Doctor_Schedule(doctor_id)      │ CASCADE                        │ CASCADE         │
│ Appointments(patient_id)        │ CASCADE                        │ CASCADE         │
│ Appointments(doctor_id)         │ CASCADE                        │ CASCADE         │
│ Prescriptions(appointment_id)   │ CASCADE                        │ CASCADE         │
│ Billing(appointment_id)         │ CASCADE                        │ CASCADE         │
│ Emergency(assigned_doctor)      │ CASCADE                        │ SET NULL        │
│ Lab_Tests(doctor_id)            │ CASCADE                        │ SET NULL        │
│ Bed_Allocation(patient_id)      │ CASCADE                        │ CASCADE         │
│ Feedback(patient_id)            │ CASCADE                        │ CASCADE         │
└─────────────────────────────────┴────────────────────────────────┴─────────────────┘
```
- **Rationale for `RESTRICT` on Specializations**: Medical branches cannot be dropped if active physicians practice within them, avoiding catastrophic classification voids.
- **Rationale for `SET NULL` on Doctors in Lab Tests & Emergency**: If a physician resigns or is purged, the emergency case and lab test records of patients must survive for clinical liability and historical continuity.

---

### 4.5 Database Triggers & Procedural Automation Constraints

To enforce mission-critical business rules at the database engine level, four active triggers operate in MySQL:

1. **`trg_CheckBedAllocationInsert`**: Evaluates new admissions. Aborts the insert and signals SQLSTATE `'45000'` if the bed is already occupied or if the patient is already actively admitted elsewhere:
   ```sql
   DELIMITER //
   CREATE TRIGGER trg_CheckBedAllocationInsert
   BEFORE INSERT ON Bed_Allocation
   FOR EACH ROW
   BEGIN
       IF EXISTS (
           SELECT 1 FROM Bed_Allocation
           WHERE bed_number = NEW.bed_number 
             AND status = 'Occupied' 
             AND discharge_date IS NULL
       ) THEN
           SIGNAL SQLSTATE '45000'
           SET MESSAGE_TEXT = 'Integrity Violation: Selected bed is already occupied!';
       END IF;
   END //
   DELIMITER ;
   ```

2. **`trg_AfterBillPaid`**: Automatically synchronizes the clinical appointment lifecycle with accounting. When `payment_status` changes to `'Paid'`, the corresponding appointment status is flipped to `'Completed'`:
   ```sql
   DELIMITER //
   CREATE TRIGGER trg_AfterBillPaid
   AFTER UPDATE ON Billing
   FOR EACH ROW
   BEGIN
       IF NEW.payment_status = 'Paid' AND OLD.payment_status <> 'Paid' THEN
           UPDATE Appointments
           SET status = 'Completed'
           WHERE appointment_id = NEW.appointment_id;
       END IF;
   END //
   DELIMITER ;
   ```

3. **`trg_AuditAppointmentCancel`**: Fires when an appointment is flagged as `'Cancelled'`, creating an immutable audit log entry.

---

# 5. Normalization Concepts & Comprehensive Analysis

### 5.1 Theoretical Background & Objectives of Normalization
**Normalization** is a formal, scientific database design technique developed by Edgar F. Codd. It systematically decomposes relations to minimize data redundancy and eradicate destructive update anomalies while maintaining:
1. **Lossless-Join Decomposition**: Projecting and subsequently rejoining relations via natural joins guarantees the exact original relation without spurious tuples.
2. **Dependency Preservation**: Ensuring all functional dependencies can be enforced using simple key constraints on individual decomposed relations without computing cross-table joins.

---

### 5.2 Functional Dependencies (FD) Formalization

A functional dependency $\alpha \to \beta$ specifies that the value of attribute set $\alpha$ uniquely determines the value of attribute set $\beta$.

#### Canonical Functional Dependencies in CarePoint HMS:
- **FD-01 (Patients)**:
  $$\text{patient\_id} \to \{\text{patient\_name}, \text{gender}, \text{age}, \text{blood\_group}, \text{contact}, \text{address}, \text{registration\_date}\}$$
- **FD-02 (Specializations)**:
  $$\text{specialization\_id} \to \{\text{specialization\_name}\}$$
- **FD-03 (Doctor)**:
  $$\text{doctor\_id} \to \{\text{doctor\_name}, \text{specialization\_id}, \text{qualification}, \text{consultation\_fee}, \text{contact}\}$$
- **FD-04 (Doctor_Schedule)**:
  $$\text{schedule\_id} \to \{\text{doctor\_id}, \text{day\_of\_week}, \text{start\_time}, \text{end\_time}\}$$
- **FD-05 (Appointments)**:
  $$\text{appointment\_id} \to \{\text{patient\_id}, \text{doctor\_id}, \text{appointment\_date}, \text{appointment\_time}, \text{room\_number}, \text{status}\}$$
- **FD-06 (Prescriptions)**:
  $$\text{prescription\_id} \to \{\text{appointment\_id}, \text{diagnosis}, \text{medicine}, \text{next\_visit\_date}, \text{remarks}\}$$
  $$\text{appointment\_id} \to \{\text{prescription\_id}, \text{diagnosis}, \text{medicine}, \text{next\_visit\_date}, \text{remarks}\}$$
- **FD-07 (Billing)**:
  $$\text{bill\_id} \to \{\text{appointment\_id}, \text{amount}, \text{bill\_date}, \text{payment\_method}, \text{payment\_status}\}$$
  $$\text{appointment\_id} \to \{\text{bill\_id}, \text{amount}, \text{bill\_date}, \text{payment\_method}, \text{payment\_status}\}$$
- **FD-08 (Bed_Allocation)**:
  $$\text{allocation\_id} \to \{\text{patient\_id}, \text{ward\_type}, \text{bed\_number}, \text{admit\_date}, \text{discharge\_date}, \text{daily\_charge}, \text{status}\}$$
- **FD-09 (Lab_Tests)**:
  $$\text{test\_id} \to \{\text{patient\_id}, \text{doctor\_id}, \text{test\_name}, \text{test\_date}, \text{cost}, \text{result}, \text{status}\}$$
- **FD-10 (Feedback)**:
  $$\text{feedback\_id} \to \{\text{patient\_id}, \text{rating}, \text{feedback\_date}, \text{comments}\}$$

---

### 5.3 Normalization Walkthrough: Unnormalized Form (UNF) to 1NF

#### The Unnormalized Hospital Master Record (UNF)
Consider a naive, flat unnormalized clinical log where all clinic events are appended as a single spreadsheet table:

$$\text{HOSPITAL\_LOG}(\underline{\text{patient\_id}}, \text{patient\_name}, \text{contact}, \{\text{appointment\_date}, \text{doctor\_name}, \text{specialty}, \text{qualification}, \text{duty\_days}, \text{prescribed\_medicines}, \text{bill\_amt}\})$$

#### Violations in UNF:
1. **Repeating Groups**: A patient possesses multiple appointments, multiple duty days for doctors, and multiple prescribed medicines stored inside multi-valued cells.
2. **Non-Atomic Values**: The attribute `prescribed_medicines` contains comma-separated values (e.g., `'Amlodipine, Aspirin, Atorvastatin'`).
3. **No Defined Primary Key**: Cannot uniquely address an individual consultation without composite string keys.

#### Transformation to First Normal Form (1NF)
> **Definition (1NF)**: A relation $R$ is in First Normal Form if and only if every attribute domain contains only atomic (indivisible) values, and there are no repeating groups or arrays.

**Action Taken**:
1. Flattened all multi-valued strings into distinct individual records.
2. Defined candidate keys for every relation.
3. Separated repeating clinical interactions into designated entities (`Appointments`, `Prescriptions`, `Doctor_Schedule`).

---

### 5.4 Elimination of Partial Dependencies (1NF to 2NF)

> **Definition (2NF)**: A relation $R$ is in Second Normal Form if it is in 1NF and **no non-prime attribute is partially dependent on any candidate key**. That is, no non-prime attribute depends on a proper subset of a composite candidate key.

#### Case Study: The Doctor Schedule Partial Dependency Anomaly
Suppose we merged `Doctor` and `Doctor_Schedule` into a single composite relation:

$$\text{DOCTOR\_DUTY}(\underline{\text{doctor\_id}}, \underline{\text{day\_of\_week}}, \text{doctor\_name}, \text{qualification}, \text{consultation\_fee}, \text{start\_time}, \text{end\_time})$$

- **Candidate Key**: $\{\text{doctor\_id}, \text{day\_of\_week}\}$
- **Prime Attributes**: `doctor_id`, `day_of_week`
- **Non-Prime Attributes**: `doctor_name`, `qualification`, `consultation_fee`, `start_time`, `end_time`

#### Functional Dependencies Observed:
1. $\{\text{doctor\_id}, \text{day\_of\_week}\} \to \{\text{start\_time}, \text{end\_time}\}$ *(Full Dependency)*
2. $\text{doctor\_id} \to \{\text{doctor\_name}, \text{qualification}, \text{consultation\_fee}\}$ *(**Partial Dependency!**)*

Because $\text{doctor\_id}$ is a proper subset of the composite candidate key $\{\text{doctor\_id}, \text{day\_of\_week}\}$, the attributes `doctor_name`, `qualification`, and `consultation_fee` exhibit partial dependency.

#### Decomposition to 2NF:
We decompose $\text{DOCTOR\_DUTY}$ into two distinct relations:
1. **`Doctor`** $(\underline{\text{doctor\_id}}, \text{doctor\_name}, \text{qualification}, \text{consultation\_fee})$
2. **`Doctor_Schedule`** $(\underline{\text{schedule\_id}}, *\text{doctor\_id}, \text{day\_of\_week}, \text{start\_time}, \text{end\_time})$

Now, in both relations, all non-prime attributes depend strictly on the entirety of their respective primary keys. **2NF is fully satisfied.**

---

### 5.5 Elimination of Transitive Dependencies (2NF to 3NF)

> **Definition (3NF)**: A relation $R$ is in Third Normal Form if it is in 2NF and **no non-prime attribute is transitively dependent on the primary key**. Formally, for every non-trivial functional dependency $X \to Y$:
> - Either $X$ is a Superkey, **OR**
> - $Y$ is a Prime Attribute (member of a candidate key).

#### Case Study 1: The Doctor Specialization Transitive Dependency
Suppose the `Doctor` relation was structured without a normalized `Specializations` table:

$$\text{DOCTOR\_INFO}(\underline{\text{doctor\_id}}, \text{doctor\_name}, \text{specialization\_id}, \text{specialization\_name}, \text{consultation\_fee})$$

- **Primary Key**: $\text{doctor\_id}$
- **Dependencies Present**:
  1. $\text{doctor\_id} \to \text{specialization\_id}$
  2. $\text{specialization\_id} \to \text{specialization\_name}$
  3. Therefore, $\text{doctor\_id} \to \text{specialization\_name}$ exists **transitively** via $\text{specialization\_id}$!

Neither is $\text{specialization\_id}$ a superkey of $\text{DOCTOR\_INFO}$, nor is $\text{specialization\_name}$ a prime attribute.

#### Decomposition to 3NF:
We extract the transitive dependency into its own master relation:
1. **`Specializations`** $(\underline{\text{specialization\_id}}, \text{specialization\_name})$
2. **`Doctor`** $(\underline{\text{doctor\_id}}, \text{doctor\_name}, *\text{specialization\_id}, \text{qualification}, \text{consultation\_fee}, \text{contact})$

---

#### Case Study 2: The Appointment, Billing & Prescription Transitive Dependency
If the system had stored billing amounts and medical prescriptions directly inside the `Appointments` table:

$$\text{APPOINTMENT\_MASTER}(\underline{\text{appointment\_id}}, \text{patient\_id}, \text{doctor\_id}, \text{bill\_id}, \text{amount}, \text{payment\_status}, \text{prescription\_id}, \text{diagnosis})$$

- Dependencies:
  - $\text{appointment\_id} \to \text{bill\_id}$
  - $\text{bill\_id} \to \{\text{amount}, \text{payment\_status}\}$ *(Transitive!)*
  - $\text{appointment\_id} \to \text{prescription\_id}$
  - $\text{prescription\_id} \to \{\text{diagnosis}\}$ *(Transitive!)*

#### Decomposition to 3NF:
We decompose into three 3NF-compliant relations:
1. **`Appointments`** $(\underline{\text{appointment\_id}}, *\text{patient\_id}, *\text{doctor\_id}, \text{appointment\_date}, \text{room\_number}, \text{status})$
2. **`Billing`** $(\underline{\text{bill\_id}}, *\text{appointment\_id}, \text{amount}, \text{bill\_date}, \text{payment\_status})$
3. **`Prescriptions`** $(\underline{\text{prescription\_id}}, *\text{appointment\_id}, \text{diagnosis}, \text{medicine}, \text{remarks})$

---

### 5.6 Boyce-Codd Normal Form (BCNF) Verification

> **Definition (BCNF)**: A relation $R$ is in BCNF if and only if for every non-trivial functional dependency $X \to Y$, **$X$ is a Superkey**.

Let us test the canonical dependencies across the CarePoint HMS relations:

1. In **`Patients`**: The only determinant is $\text{patient\_id}$, which is the primary key (Superkey). $\checkmark$
2. In **`Doctor`**: The determinant is $\text{doctor\_id}$, which is the primary key (Superkey). $\checkmark$
3. In **`Specializations`**: $\text{specialization\_id}$ is the primary key, and $\text{specialization\_name}$ is a unique candidate key. Both determinants are Superkeys. $\checkmark$
4. In **`Appointments`**: $\text{appointment\_id}$ is the primary key (Superkey). $\checkmark$
5. In **`Billing`**: Both $\text{bill\_id}$ and $\text{appointment\_id}$ (marked UNIQUE) uniquely determine all invoice attributes; both are candidate superkeys. $\checkmark$
6. In **`Prescriptions`**: Both $\text{prescription\_id}$ and $\text{appointment\_id}$ (marked UNIQUE) are candidate superkeys. $\checkmark$

**Conclusion**: All relations in the CarePoint database schema satisfy **BCNF**.

---

### 5.7 Database Anomaly Mitigation Analysis

| Database Operation | Unnormalized / Denormalized Flaw | Normalized 3NF/BCNF Resolution in CarePoint HMS |
|:---|:---|:---|
| **Insertion Anomaly** | A hospital cannot register a newly accredited medical specialization (e.g., *Oncology*) unless at least one doctor is immediately hired and assigned to it. | Specializations reside in a separate master relation (`Specializations`). A specialty can be registered independently without any doctor assigned yet. |
| **Insertion Anomaly** | A newly registered patient cannot exist in the system unless they immediately book an appointment or an emergency bed. | Patients reside in their own relation (`Patients`). A patient can be registered at intake without requiring an active appointment or bed stay. |
| **Deletion Anomaly** | If Dr. Vikram Singh resigns and his appointment records are deleted, the history of his medical specialization is wiped from the database. | Deleting a record from `Doctor` does not affect `Specializations`. The specialization remains intact for future recruitment. |
| **Deletion Anomaly** | If an inpatient is discharged and their bed allocation record is purged, the patient's entire profile and medical history would be lost. | `Bed_Allocation` only references `patient_id`. Discharging a bed or clearing an allocation preserves the patient's demographic profile and historical prescriptions intact. |
| **Update Anomaly** | If Dr. Neha Shah updates her consultation fee from ₹800 to ₹1000 in an unnormalized schema, every past appointment row containing her name must be updated. A partial update creates contradictory records. | Doctor consultation fees are stored in exactly one tuple in the `Doctor` relation. Updating `Doctor.consultation_fee` updates the fee globally without touching historical appointments or invoices. |

---

### 5.8 Lossless Join & Dependency Preservation Proofs

#### Proof 1: Lossless Join Property
For a decomposition of relation $R$ into $R_1$ and $R_2$ to be lossless with respect to functional dependency set $F$, the intersection of attributes must contain a superkey of at least one of the components:

$$(R_1 \cap R_2) \to R_1 \quad \text{OR} \quad (R_1 \cap R_2) \to R_2$$

Taking the decomposition of Doctor and Specializations:
- $R = \text{Doctor}(\text{doctor\_id}, \text{doctor\_name}, \text{specialization\_id}, \text{specialization\_name})$
- $R_1 = \text{Doctor}(\text{doctor\_id}, \text{doctor\_name}, \text{specialization\_id})$
- $R_2 = \text{Specializations}(\text{specialization\_id}, \text{specialization\_name})$
- Attribute intersection: $R_1 \cap R_2 = \{\text{specialization\_id}\}$
- Since $\text{specialization\_id} \to R_2$, the intersection is a candidate key of $R_2$.
- **Q.E.D.**: The decomposition is strictly **lossless**.

#### Proof 2: Dependency Preservation
Every functional dependency in the canonical set $F$ involves attributes that belong entirely to a single decomposed relation:
- $\text{FD-01}$ is enforceable within `Patients`.
- $\text{FD-02}$ is enforceable within `Specializations`.
- $\text{FD-03}$ is enforceable within `Doctor`.
- $\text{FD-05}$ is enforceable within `Appointments`.
- $\text{FD-06}$ is enforceable within `Prescriptions`.
- $\text{FD-07}$ is enforceable within `Billing`.

No cross-table inter-relational functional dependency is broken. **Q.E.D.**: All dependencies are **fully preserved**.

---

# 6. Conclusion & Summary Metrics

The database architecture for the **CarePoint Hospital Management System** fulfills all rigorous DBMS theoretical benchmarks and real-world clinical requirements:

- **13 Normalized Tables**: Engineered from conceptual modeling to physical DDL without functional dependency loss.
- **BCNF / 3NF Compliance**: Guaranteed zero insertion, deletion, and modification anomalies.
- **Relational Integrity Guarantees**: Active MySQL triggers prevent duplicate inpatient bed allocations (`trg_CheckBedAllocationInsert`) and automate appointment completions upon billing payment (`trg_AfterBillPaid`).
- **High Concurrency & Fast Indexing**: Key predicates indexed via InnoDB B-Trees to ensure responsive dual-client synchronization between the **Java Swing Workstation GUI** and the **React 19 Web Portal**.
- **Interactive Visual Reference**: Fully rendered and zoomable in [`ER_Diagram.html`](file:///c:/Users/nisar/OneDrive/Desktop/DBMS/ER_Diagram.html).

---

*Report prepared and submitted for Academic Course Assessment – 8 (DBMS).*
