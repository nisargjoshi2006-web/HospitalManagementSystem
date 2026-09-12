# 🏥 Hospital Management System

A full-featured **Hospital Management System** built with **Java Swing**, **JDBC**, and **MySQL**. It manages patients, doctors, appointments, billing, prescriptions, feedback, emergency cases, lab tests, bed allocation, and audit logging — through an intuitive tabbed GUI interface with role-based access control and a full suite of backend console tests.

---

## 📋 Features

### 🔐 Authentication & Security
- **SHA-256 Password Hashing** for secure credential storage
- **Role-Based Access Control (RBAC)**:
  - **Admin**: Full access to all 12 modules
  - **Receptionist**: Access to Dashboard, Patients, Appointments, Billing, Lab Tests, Bed Allocation
- Logout confirmation with session management

### 📊 Live Dashboard
- Real-time metric cards: Patients, Doctors, Appointments, Emergencies, Occupied Beds, Prescriptions, Pending Lab Tests, Bills, Revenue, Feedback
- **Refresh Dashboard** button for live data updates
- Database health status indicator

### 🗂️ Core Modules (Full CRUD)
| Module | Key Features |
|--------|-------------|
| **Patients** | Add, View, Search by ID **or Name**, Update, Safe Cascade Delete, Registration Date tracking |
| **Doctors** | Add, View, Search by ID **or Name/Specialization**, Update, Cascade Delete with confirmation dialog |
| **Doctor Schedule** | Day-wise scheduling with time slots |
| **Appointments** | Book, View, Search, Update, Cancel/Delete, Doctor availability & date validation |
| **Prescriptions** | Add, View, **Database-powered search**, Update, Delete, nullable next visit date |
| **Billing** | Auto-calculate fee from Doctor, **validated bill creation**, Update payment status, Delete |
| **Feedback** | Patient ratings (1-5), comments, **full field update** (Patient ID, Date, Rating, Comments) |
| **Emergency** | Priority triage (Low → Critical), doctor assignment, status tracking |
| **Lab Tests** | Diagnostic orders (CBC, MRI, X-Ray), result updates, cost tracking |
| **Bed Allocation** | Ward management (ICU, Private, General), **duplicate prevention triggers**, discharge with confirmation |
| **Reports** | 8 built-in reports with **Export to CSV** |

### 📊 Advanced Reports (Built-in)
| Report | SQL Concepts Used |
|--------|------------------|
| Active Appointments | `JOIN`, `WHERE` filter |
| Hospital Revenue Summary | `SUM`, `GROUP BY` |
| Patient History | Stored Procedure call |
| Pending Bills | `WHERE` filter |
| **Revenue by Doctor** | `LEFT JOIN` (3 tables), `GROUP BY`, `SUM`, `COUNT` |
| **Monthly Patient Trends** | `DATE_FORMAT`, `GROUP BY`, aggregate |
| **Bed Occupancy History** | `CASE WHEN`, `DATEDIFF`, `CURDATE()`, computed columns |
| **Doctor Workload Analysis** | `LEFT JOIN` (4 tables), `COUNT(DISTINCT)`, multi-table aggregation |

---

## 🛠️ Technology Stack

| Technology | Purpose |
|-----------|---------|
| **Java (JDK 17+)** | Core language |
| **Java Swing & AWT** | Desktop GUI |
| **MySQL 8.0+** | RDBMS |
| **JDBC** | Database connectivity with PreparedStatements |
| **SHA-256** | Password hashing |
| **MySQL Connector/J** | JDBC driver (included in `lib/`) |

---

## 📁 Project Structure

```
HospitalManagementSystem/
├── Database.sql                  # Complete schema + seed data + triggers + views + procedures
├── config.properties             # Database connection settings
├── config.properties.example     # Template for new setups
├── lib/
│   └── mysql-connector-j-26.7.0.jar
├── sql/
│   └── queries.sql               # 11 demonstration SQL queries (joins, subqueries, aggregates)
├── src/
│   ├── config.properties         # Classpath connection config
│   ├── db/
│   │   └── DBConnection.java           # Centralized DB connection + health check
│   ├── model/                           # 12 POJO model classes
│   │   ├── Patient.java, Doctor.java, Appointment.java
│   │   ├── Billing.java, Prescription.java, Feedback.java
│   │   ├── DoctorSchedule.java, Emergency.java
│   │   ├── LabTest.java, BedAllocation.java
│   │   ├── AuditLog.java, User.java
│   ├── dao/                             # 12 Data Access Objects (all use try-with-resources)
│   │   ├── PatientDAO.java, DoctorDAO.java, AppointmentDAO.java
│   │   ├── BillingDAO.java, PrescriptionDAO.java, FeedbackDAO.java
│   │   ├── DoctorScheduleDAO.java, EmergencyDAO.java
│   │   ├── LabTestDAO.java, BedAllocationDAO.java
│   │   ├── AuditLogDAO.java, UserDAO.java
│   ├── ui/                              # 14 Swing UI panels
│   │   ├── LoginUI.java                 # Application entry point
│   │   ├── HospitalManagementUI.java    # Main tabbed window
│   │   ├── DashboardPanel.java          # Live metrics + Refresh button
│   │   ├── PatientPanel.java, DoctorPanel.java
│   │   ├── DoctorSchedulePanel.java, AppointmentPanel.java
│   │   ├── PrescriptionPanel.java, FeedbackPanel.java
│   │   ├── BillingPanel.java, EmergencyPanel.java
│   │   ├── LabTestPanel.java, BedAllocationPanel.java
│   │   └── ReportsPanel.java           # 8 reports + CSV export
│   └── test/                            # 13 CLI test harnesses
│       ├── TestPatient.java, TestDoctor.java
│       ├── TestAppointment.java, TestAppointmentBusinessRules.java
│       ├── TestBilling.java, TestPrescription.java
│       ├── TestFeedback.java, TestDoctorSchedule.java
│       ├── TestEmergency.java, TestLabTest.java
│       ├── TestBedAllocation.java, TestDashboard.java
│       └── TestLogin.java
└── README.md
```

---

## ⚙️ Setup & Installation

### Prerequisites
- **Java Development Kit (JDK 17 or higher)** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **MySQL Server 8.0+** — [Download](https://dev.mysql.com/downloads/mysql/)
- **MySQL Connector/J** — Already included in `lib/`

### Step 1: Database Setup
Open **MySQL Workbench** or **MySQL CLI** and run:
```sql
SOURCE C:/path/to/HospitalManagementSystem/Database.sql;
```
This creates the `hospital` database with all 13 tables, triggers, views, stored procedure, indexes, and seed data.

### Step 2: Configure Database Credentials
Edit `config.properties` (in **both** project root and `src/` directory):
```properties
db.url=jdbc:mysql://localhost:3306/hospital
db.user=root
db.password=your_mysql_password
```

### Step 3: Compile the Project
```bash
javac -cp "lib/mysql-connector-j-26.7.0.jar;src" -d out src/db/*.java src/model/*.java src/dao/*.java src/ui/*.java src/test/*.java
```

### Step 4: Run the Application (GUI)
```bash
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" ui.HospitalManagementUI
```
This opens the **Login Window**. Enter credentials and the main application launches.

---

## 🖥️ Running Backend (Console Tests)

Each module has a dedicated CLI test harness. Run them individually:

```bash
# Dashboard — shows all system metrics at a glance
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestDashboard

# Login — test authentication, registration, password change
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestLogin

# Patient CRUD — interactive menu for add/view/search/update/delete
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestPatient

# Doctor CRUD
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestDoctor

# Appointment Booking + Business Rules
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestAppointment
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestAppointmentBusinessRules

# Prescription Management
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestPrescription

# Billing
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestBilling

# Feedback
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestFeedback

# Doctor Schedule
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestDoctorSchedule

# Emergency Cases
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestEmergency

# Lab Tests
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestLabTest

# Bed Allocation (with business rule validation)
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestBedAllocation
```

> **Note for macOS/Linux**: Replace `;` with `:` in the classpath:
> `java -cp "out:lib/mysql-connector-j-26.7.0.jar:src" test.TestDashboard`

---

## 🔑 Default Login Credentials

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `admin` | `admin123` | Admin | All 12 modules |
| `receptionist` | `recep123` | Receptionist | Dashboard, Patients, Appointments, Billing, Lab Tests, Bed Allocation |

---

## 🗄️ Database Schema (13 Tables)

| # | Table | Description |
|---|-------|-------------|
| 1 | `Users` | Login credentials with SHA-256 hashed passwords, roles |
| 2 | `Patients` | Patient demographics, blood group, registration date |
| 3 | `Specializations` | Medical specialization lookup (Cardiology, Neurology, etc.) |
| 4 | `Doctor` | Doctor profiles with specialization FK, consultation fees |
| 5 | `Doctor_Schedule` | Weekly availability (day, start/end time) |
| 6 | `Appointments` | Patient-Doctor bookings with date, time, room, status |
| 7 | `Prescriptions` | Diagnosis, medicine, next visit date, remarks |
| 8 | `Billing` | Auto-calculated fees, payment method/status tracking |
| 9 | `Feedback` | Patient satisfaction ratings (1-5) and comments |
| 10 | `Emergency` | Emergency triage (priority levels), doctor assignment |
| 11 | `Lab_Tests` | Diagnostic test orders, results, costs, status |
| 12 | `Bed_Allocation` | Inpatient admissions (ICU, Private, General), stay tracking |
| 13 | `Audit_Logs` | Automated security & compliance logging |

---

## ⚡ Advanced DBMS Concepts Implemented

### Views (2)
| View | Purpose |
|------|---------|
| `v_ActiveAppointments` | Pre-joined view for all non-cancelled appointments with patient & doctor names |
| `v_HospitalRevenueSummary` | Aggregated revenue grouped by payment method |

### Stored Procedure (1)
| Procedure | Purpose |
|-----------|---------|
| `sp_GetPatientHistory(IN p_patient_id INT)` | Fetches complete clinical, prescription, and billing timeline for a patient |

### Triggers (4)
| Trigger | Event | Action |
|---------|-------|--------|
| `trg_AfterBillPaid` | After bill marked 'Paid' | Auto-updates appointment status to 'Completed' |
| `trg_AuditAppointmentCancel` | After appointment cancelled | Logs cancellation into `Audit_Logs` |
| `trg_CheckBedAllocationInsert` | Before bed allocation insert | Prevents duplicate beds & multiple patient admissions |
| `trg_CheckBedAllocationUpdate` | Before bed allocation update | Prevents bed conflicts on status change |

### Indexes (B-Tree)
- `Patients(contact)`, `Appointments(appointment_date)`, `Doctor(specialization_id)`, `Billing(payment_status)`, `Emergency(priority_level)`

### Date Arithmetic
- `DATEDIFF(discharge_date, admit_date) * daily_charge` — calculates total inpatient stay charges

### Demonstration Queries (`sql/queries.sql`)
- 4-Table Joins (Appointments + Patients + Doctor + Specializations)
- Aggregates with `GROUP BY` & `HAVING`
- Nested Subqueries (Scalar, `IN`, `EXISTS`)
- `AVG`, `SUM`, `COUNT` aggregations

---

## 🔒 Backend Security & Data Integrity

| Feature | Implementation |
|---------|----------------|
| SQL Injection Prevention | All queries use `PreparedStatement` with `?` bind parameters |
| Resource Management | All 12 DAOs use `try-with-resources` for Connection, PreparedStatement, ResultSet |
| Business Rule Enforcement | Bed allocation triggers + DAO-level validation (fail-safe on error) |
| Cascade Delete Safety | Confirmation dialogs before cascade deletions |
| Input Validation | Patient/Doctor existence checks, date format validation, null-safe table handling |
| Audit Trail | Automatic logging of appointment cancellations |
| Password Security | SHA-256 hashing via MySQL `SHA2()` function |

---

## 🧪 How to Verify the System

### Quick Smoke Test
```bash
# 1. Compile (should show 0 errors)
javac -cp "lib/mysql-connector-j-26.7.0.jar;src" -d out src/db/*.java src/model/*.java src/dao/*.java src/ui/*.java src/test/*.java

# 2. Run dashboard test (verifies DB connection + all DAOs)
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestDashboard

# 3. Launch GUI
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" ui.HospitalManagementUI
```

### Manual Test Checklist
| # | Test Scenario | Expected Result |
|---|--------------|-----------------|
| 1 | Login with `admin`/`admin123` | Main window opens with all 12 tabs |
| 2 | Login with `receptionist`/`recep123` | Main window opens with 6 tabs only |
| 3 | Dashboard → Click "Refresh Dashboard" | All metrics update from DB |
| 4 | Patients → Search by name (e.g., "Rahul") | Shows matching patients |
| 5 | Doctors → Table shows specialization names | "Cardiology" instead of "1" |
| 6 | Doctors → Delete → Confirmation dialog | Warns about cascade deletion |
| 7 | Bed Allocation → Admit same patient twice | Error: "Patient already admitted" |
| 8 | Bed Allocation → Admit to occupied bed | Error: "Bed is occupied" |
| 9 | Billing → Add bill with invalid appointment | Error: "Failed to create bill" |
| 10 | Reports → Select "Revenue by Doctor" | Shows revenue breakdown by doctor |
| 11 | Reports → Click "Export to CSV" | CSV file saved to disk |

---

## 👥 Team Members

- **Nisarg Joshi**
- **Shilajit Banerjee**

