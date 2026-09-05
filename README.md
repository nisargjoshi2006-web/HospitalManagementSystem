# 🏥 Hospital Management System

A full-featured **Hospital Management System** developed using Java Swing, JDBC, and MySQL. It manages patients, doctors, appointments, billing, prescriptions, feedback, emergency cases, and doctor schedules through both an intuitive tabbed GUI interface and backend console test suites.

## 📋 Features

### 🔐 Login System & Security
- **Authentication**: Secure login with **SHA-256** password hashing.
- **Role-Based Access Control (RBAC)**:
  - **Admin**: Full access to all 9 system modules and configurations.
  - **Receptionist**: Dedicated access to Dashboard, Patients, Appointments, and Billing.
- **Failed Attempt Tracking**: Security warnings on multiple consecutive failed login attempts.

### 📊 Dashboard
- Real-time live counts of Patients, Doctors, Appointments, Feedback, and Emergency cases.
- Live current date display.

### 🗂️ Core Modules (CRUD Operations)
| Module | Supported Operations |
|--------|---------------------|
| **Patients** | Add, View, Search by ID, Update details, Delete, Count |
| **Doctors** | Add, View, Search, Update, Safe Cascade Delete (Appointments, Billing, Prescriptions, Schedule), Count |
| **Doctor Schedule** | Add, View, Search, Update, Delete, Count |
| **Appointments** | Book, View, Search, Update, Cancel/Delete, Count |
| **Prescriptions** | Add Prescription, View, Search by ID, Update, Delete, Count |
| **Billing** | Auto-calculate fee from Doctor, Add Bill, View, Search, Update Status, Delete, Count |
| **Feedback** | Patient Rating (1-5), Comments, View, Search, Update, Delete, Count |
| **Emergency** | Priority Triage (Low to Critical), Status tracking, Assign Doctor, Update, Delete |

## 🛠️ Technology Stack

| Technology | Purpose |
|-----------|---------|
| **Java (JDK 17+)** | Core programming language |
| **Java Swing & AWT** | Desktop GUI interface |
| **MySQL 8.0+** | Relational Database Management System |
| **JDBC** | Database connectivity & PreparedStatements |
| **SHA-256** | Password hashing for authentication |

## 📁 Project Structure

```
HospitalManagementSystem/
├── Database.sql              # Complete database schema + seed data
├── config.properties         # Database connection settings
├── lib/
│   └── mysql-connector-j-26.7.0.jar
├── src/
│   ├── config.properties     # Classpath connection config
│   ├── db/
│   │   └── DBConnection.java        # Centralized DB connection manager
│   ├── model/
│   │   ├── Patient.java
│   │   ├── Doctor.java
│   │   ├── Appointment.java
│   │   ├── Billing.java
│   │   ├── Prescription.java
│   │   ├── Feedback.java
│   │   ├── DoctorSchedule.java
│   │   ├── Emergency.java
│   │   └── User.java
│   ├── dao/
│   │   ├── PatientDAO.java
│   │   ├── DoctorDAO.java
│   │   ├── AppointmentDAO.java
│   │   ├── BillingDAO.java
│   │   ├── PrescriptionDAO.java
│   │   ├── FeedbackDAO.java
│   │   ├── DoctorScheduleDAO.java
│   │   ├── EmergencyDAO.java
│   │   └── UserDAO.java
│   ├── ui/
│   │   ├── LoginUI.java              # Application entry point & login dialog
│   │   ├── HospitalManagementUI.java # Main tabbed application window
│   │   ├── DashboardPanel.java
│   │   ├── PatientPanel.java
│   │   ├── DoctorPanel.java
│   │   ├── DoctorSchedulePanel.java
│   │   ├── AppointmentPanel.java
│   │   ├── PrescriptionPanel.java
│   │   ├── FeedbackPanel.java
│   │   ├── BillingPanel.java
│   │   └── EmergencyPanel.java
│   └── test/
│       ├── TestPatient.java
│       ├── TestDoctor.java
│       ├── TestAppointment.java
│       ├── TestBilling.java
│       ├── TestPrescription.java
│       ├── TestFeedback.java
│       ├── TestDoctorSchedule.java
│       ├── TestEmergency.java
│       ├── TestDashboard.java
│       └── TestLogin.java
└── README.md
```

## ⚙️ Setup & Installation

### Prerequisites
- **Java Development Kit (JDK 17 or higher)**
- **MySQL Server 8.0+**
- **MySQL Connector/J** (provided in `lib/`)

### 1. Database Setup
Open MySQL Workbench or MySQL CLI and run:
```sql
SOURCE Database.sql;
```
This will create the `hospital` database along with all tables, constraints, foreign keys, and seed the default admin account.

### 2. Configure Database Credentials
Verify or edit `config.properties` (in both the project root and `src/` directory):
```properties
db.url=jdbc:mysql://localhost:3306/hospital
db.user=root
db.password=your_password
```

### 3. Compile Project
```bash
javac -cp "lib/mysql-connector-j-26.7.0.jar" -d out src/db/*.java src/model/*.java src/dao/*.java src/ui/*.java src/test/*.java
```

### 4. Run Application
```bash
# Launch Desktop GUI (starts at Login window)
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" ui.LoginUI

# Or run console backend tests:
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestLogin
java -cp "out;lib/mysql-connector-j-26.7.0.jar;src" test.TestDashboard
```

### Default Credentials
| Username | Password | Role | Permissions |
|----------|----------|------|-------------|
| `admin` | `admin123` | Admin | Full access to all 9 modules |

## 🗄️ Database Schema

The database consists of **10 interconnected tables**:
1. `Users` — Credentials, SHA-256 passwords, full name, role (`Admin`/`Receptionist`)
2. `Patients` — Patient demographics and registration history
3. `Specializations` — Medical specializations lookup
4. `Doctor` — Doctor profiles, specialization mapping, consultation fees
5. `Doctor_Schedule` — Doctor availability schedule by day and time
6. `Appointments` — Patient-Doctor appointment bookings with status
7. `Prescriptions` — Medical prescriptions, diagnoses, and remarks
8. `Billing` — Automated consultation billing and payment tracking
9. `Feedback` — Patient satisfaction ratings (1–5) and review comments
10. `Emergency` — Emergency room admissions and priority triage management

## 👥 Team Members

- **Nisarg Joshi**
- **Shilajit Banerjees**
