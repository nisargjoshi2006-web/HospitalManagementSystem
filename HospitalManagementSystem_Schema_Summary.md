# 🏥 CarePoint Hospital Management System — Relational Schema Summary

> **Technical Database Specification & Data Dictionary**  
> *Extracted from the implemented MySQL 8.0 Hospital Management System repository*

---

## 📑 Table of Contents
1. [Database Overview](#database-overview)
2. [Relational Data Dictionary (13 Tables)](#relational-data-dictionary)
3. [Relationship Matrix & Cardinality Rules](#relationship-matrix--cardinality-rules)
4. [Relational Integrity & Triggers](#relational-integrity--triggers)

---

## 1. Database Overview

- **Database Technology**: MySQL 8.0 Enterprise / Community Server
- **Database Name**: `hospital`
- **Storage Engine**: InnoDB (Row-level locking, ACID compliance, foreign key cascade support)
- **Character Set**: `utf8mb4`
- **Total Verified Tables**: 13
- **Normalization Level**: Third Normal Form (3NF) & Boyce-Codd Normal Form (BCNF)

---

## 2. Relational Data Dictionary

### 📋 `Specializations` Table
**Functional Domain**: Doctor Management  
**Primary Key**: `specialization_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `specialization_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `specialization_name` | `VARCHAR(50)` | - | UNIQUE, NOT NULL |

### 📋 `Doctor` Table
**Functional Domain**: Doctor Management  
**Primary Key**: `doctor_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `doctor_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `doctor_name` | `VARCHAR(50)` | - | NOT NULL |
| `specialization_id` | `INT` | **FK** | FK -> Specializations |
| `qualification` | `VARCHAR(50)` | - | NOT NULL |
| `consultation_fee` | `DECIMAL(10,2)` | - | DEFAULT 500.00 |
| `contact` | `VARCHAR(15)` | - | NOT NULL |

### 📋 `Doctor_Schedule` Table
**Functional Domain**: Doctor Management  
**Primary Key**: `schedule_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `schedule_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `doctor_id` | `INT` | **FK** | FK -> Doctor, NOT NULL |
| `day_of_week` | `VARCHAR(15)` | - | NOT NULL |
| `start_time` | `TIME` | - | NOT NULL |
| `end_time` | `TIME` | - | NOT NULL |

### 📋 `Patients` Table
**Functional Domain**: Patient & Outpatient Core  
**Primary Key**: `patient_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `patient_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `patient_name` | `VARCHAR(50)` | - | NOT NULL |
| `gender` | `VARCHAR(10)` | - | Male/Female/Other |
| `age` | `INT` | - | CHECK >= 0 |
| `blood_group` | `VARCHAR(5)` | - | NOT NULL |
| `contact` | `VARCHAR(15)` | - | INDEXED, NOT NULL |
| `address` | `VARCHAR(100)` | - | Nullable |
| `registration_date` | `DATE` | - | NOT NULL |

### 📋 `Appointments` Table
**Functional Domain**: Patient & Outpatient Core  
**Primary Key**: `appointment_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `appointment_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `patient_id` | `INT` | **FK** | FK -> Patients, NOT NULL |
| `doctor_id` | `INT` | **FK** | FK -> Doctor, NOT NULL |
| `appointment_date` | `DATE` | - | INDEXED, NOT NULL |
| `appointment_time` | `TIME` | - | NOT NULL |
| `room_number` | `VARCHAR(10)` | - | NOT NULL |
| `status` | `VARCHAR(20)` | - | DEFAULT 'Scheduled' |

### 📋 `Emergency` Table
**Functional Domain**: Patient & Outpatient Core  
**Primary Key**: `emergency_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `emergency_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `patient_id` | `INT` | **FK** | FK -> Patients (Inferred) |
| `emergency_type` | `VARCHAR(100)` | - | NOT NULL |
| `priority_level` | `VARCHAR(20)` | - | Critical/High/Med/Low |
| `status` | `VARCHAR(30)` | - | DEFAULT 'Admitted' |
| `assigned_doctor` | `INT` | **FK** | FK -> Doctor (Inferred) |
| `arrival_date` | `DATE` | - | NOT NULL |
| `arrival_time` | `TIME` | - | NOT NULL |

### 📋 `Prescriptions` Table
**Functional Domain**: Clinical & Financial Encounters  
**Primary Key**: `prescription_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `prescription_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `appointment_id` | `INT` | **FK** | FK -> Appointments, UNIQUE |
| `diagnosis` | `VARCHAR(100)` | - | NOT NULL |
| `medicine` | `VARCHAR(100)` | - | NOT NULL |
| `next_visit_date` | `DATE` | - | Nullable |
| `remarks` | `VARCHAR(100)` | - | Nullable |

### 📋 `Billing` Table
**Functional Domain**: Clinical & Financial Encounters  
**Primary Key**: `bill_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `bill_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `appointment_id` | `INT` | **FK** | FK -> Appointments, UNIQUE |
| `amount` | `DECIMAL(10,2)` | - | CHECK >= 0 |
| `bill_date` | `DATE` | - | NOT NULL |
| `payment_method` | `VARCHAR(20)` | - | UPI/Card/Cash |
| `payment_status` | `VARCHAR(20)` | - | INDEXED (Paid/Pending) |

### 📋 `Lab_Tests` Table
**Functional Domain**: Clinical & Financial Encounters  
**Primary Key**: `test_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `test_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `patient_id` | `INT` | **FK** | FK -> Patients, NOT NULL |
| `doctor_id` | `INT` | **FK** | FK -> Doctor, NOT NULL |
| `test_name` | `VARCHAR(100)` | - | NOT NULL |
| `test_date` | `DATE` | - | NOT NULL |
| `cost` | `DECIMAL(10,2)` | - | CHECK >= 0 |
| `result` | `VARCHAR(255)` | - | Default 'Pending Analysis' |
| `status` | `VARCHAR(30)` | - | DEFAULT 'Pending' |

### 📋 `Bed_Allocation` Table
**Functional Domain**: Facility, Security & Audit  
**Primary Key**: `allocation_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `allocation_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `patient_id` | `INT` | **FK** | FK -> Patients, NOT NULL |
| `ward_type` | `VARCHAR(50)` | - | ICU/General/Private AC |
| `bed_number` | `VARCHAR(10)` | - | NOT NULL |
| `admit_date` | `DATE` | - | NOT NULL |
| `discharge_date` | `DATE` | - | Nullable |
| `daily_charge` | `DECIMAL(10,2)` | - | NOT NULL |
| `status` | `VARCHAR(20)` | - | DEFAULT 'Occupied' |

### 📋 `Feedback` Table
**Functional Domain**: Facility, Security & Audit  
**Primary Key**: `feedback_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `feedback_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `patient_id` | `INT` | **FK** | FK -> Patients, NOT NULL |
| `rating` | `INT` | - | CHECK (1 TO 5) |
| `feedback_date` | `DATE` | - | NOT NULL |
| `comments` | `VARCHAR(100)` | - | Nullable |

### 📋 `Users` Table
**Functional Domain**: Facility, Security & Audit  
**Primary Key**: `user_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `user_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `username` | `VARCHAR(50)` | - | UNIQUE, NOT NULL |
| `password` | `VARCHAR(128)` | - | SHA-256 Digest |
| `full_name` | `VARCHAR(100)` | - | NOT NULL |
| `role` | `VARCHAR(20)` | - | Admin/Doctor/Recep/Patient |
| `created_at` | `TIMESTAMP` | - | DEFAULT CURRENT_TIMESTAMP |

### 📋 `Audit_Logs` Table
**Functional Domain**: Facility, Security & Audit  
**Primary Key**: `log_id`  

| Column Name | Data Type | Key Constraint | Attributes & Business Rules |
| :--- | :--- | :---: | :--- |
| `log_id` | `INT` | **PK** | PK, AUTO_INCREMENT |
| `action_type` | `VARCHAR(50)` | - | NOT NULL |
| `record_id` | `INT` | - | Logical FK (Entity ID) |
| `log_time` | `TIMESTAMP` | - | DEFAULT CURRENT_TIMESTAMP |
| `details` | `TEXT` | - | Nullable |

---

## 3. Relationship Matrix & Cardinality Rules

| Parent Table | Child Table | Cardinality | Relationship Label | Enforcement Level | Technical Evidence |
| :--- | :--- | :---: | :--- | :---: | :--- |
| `Specializations` | `Doctor` | **1:N** | `classifies` | ✅ MySQL FK | Explicit FK: doctor.specialization_id -> specializations.specialization_id |
| `Doctor` | `Doctor_Schedule` | **1:N** | `works_shifts` | ✅ MySQL FK | Explicit FK: doctor_schedule.doctor_id -> doctor.doctor_id |
| `Doctor` | `Appointments` | **1:N** | `attends` | ✅ MySQL FK | Explicit FK: appointments.doctor_id -> doctor.doctor_id |
| `Patients` | `Appointments` | **1:N** | `books` | ✅ MySQL FK | Explicit FK: appointments.patient_id -> patients.patient_id |
| `Appointments` | `Prescriptions` | **1:1** | `issues` | ✅ MySQL FK | Explicit FK: prescriptions.appointment_id -> appointments.appointment_id (Unique per consult) |
| `Appointments` | `Billing` | **1:1** | `generates` | ✅ MySQL FK | Explicit FK: billing.appointment_id -> appointments.appointment_id (Unique per consult) |
| `Patients` | `Bed_Allocation` | **1:N** | `allocated_bed` | ✅ MySQL FK | Explicit FK: bed_allocation.patient_id -> patients.patient_id |
| `Patients` | `Feedback` | **1:N** | `submits` | ✅ MySQL FK | Explicit FK: feedback.patient_id -> patients.patient_id |
| `Patients` | `Lab_Tests` | **1:N** | `undergoes` | ✅ MySQL FK | Explicit FK: lab_tests.patient_id -> patients.patient_id (ON DELETE CASCADE) |
| `Doctor` | `Lab_Tests` | **1:N** | `orders` | ✅ MySQL FK | Explicit FK: lab_tests.doctor_id -> doctor.doctor_id (ON DELETE CASCADE) |
| `Patients` | `Emergency` | **1:N** | `admitted_as` | ⚠️ Inferred (App Logic) | Inferred: emergency.patient_id = patients.patient_id enforced via DAO and API queries |
| `Doctor` | `Emergency` | **1:N** | `assigned_doctor` | ⚠️ Inferred (App Logic) | Inferred: emergency.assigned_doctor = doctor.doctor_id enforced via backend JOIN queries |
| `Appointments` | `Audit_Logs` | **1:N** | `audits` | ⚠️ Inferred (App Logic) | Inferred: audit_logs.record_id = appointments.appointment_id logged on trigger trg_AuditAppointmentCancel |

---

## 4. Relational Integrity & Triggers

1. **`trg_CheckBedAllocationInsert`**: Before insert trigger enforcing strict single bed occupancy and single active patient admission.
2. **`trg_AfterBillPaid`**: After update trigger automatically transitioning appointment status to `'Completed'` when bill payment transitions to `'Paid'`.
3. **`trg_AuditAppointmentCancel`**: Trigger recording appointment cancellations directly to `Audit_Logs`.
4. **`sp_GetPatientHistory`**: Stored procedure consolidating consultations, diagnoses, prescribed medicines, and billing history for any patient ID.

---
*Certified accurate against GitHub repository: https://github.com/nisargjoshi2006-web/HospitalManagementSystem*
