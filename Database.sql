CREATE DATABASE hospital;
USE hospital;
CREATE TABLE Patients(
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    age INT,
    blood_group VARCHAR(5),
    contact VARCHAR(15),
    address VARCHAR(100),
    registration_date DATE
);
CREATE TABLE Specializations(
    specialization_id INT AUTO_INCREMENT PRIMARY KEY,
    specialization_name VARCHAR(50)
);
-- 3. Doctor
CREATE TABLE Doctor(
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_name VARCHAR(50),
    specialization_id INT,
    qualification VARCHAR(50),
    consultation_fee DECIMAL(10,2),
    contact VARCHAR(15),

    FOREIGN KEY (specialization_id)
    REFERENCES Specializations(specialization_id)
);
CREATE TABLE Appointments(
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    doctor_id INT,
    appointment_date DATE,
    appointment_time TIME,
    room_number VARCHAR(10),
    status VARCHAR(20),

    FOREIGN KEY(patient_id) REFERENCES Patients(patient_id),
    FOREIGN KEY(doctor_id) REFERENCES Doctor(doctor_id)
);
CREATE TABLE Prescriptions(
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT,
    diagnosis VARCHAR(100),
    medicine VARCHAR(100),
    next_visit_date DATE,
    remarks VARCHAR(100),

    FOREIGN KEY(appointment_id) REFERENCES Appointments(appointment_id)
);
CREATE TABLE Billing(
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT,
    amount DECIMAL(10,2),
    bill_date DATE,
    payment_method VARCHAR(20),
    payment_status VARCHAR(20),

    FOREIGN KEY(appointment_id) REFERENCES Appointments(appointment_id)
);
CREATE TABLE Feedback(
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT,
    rating INT,
    feedback_date DATE,
    comments VARCHAR(100),

    FOREIGN KEY(patient_id) REFERENCES Patients(patient_id)
);
CREATE TABLE Doctor_Schedule(
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT,
    day_of_week VARCHAR(15),
    start_time TIME,
    end_time TIME,

    FOREIGN KEY (doctor_id)
    REFERENCES Doctor(doctor_id)
);
SHOW TABLES;
INSERT INTO Specializations (specialization_name) VALUES
('Cardiology'),
('Neurology'),
('Orthopedics'),
('Dermatology'),
('General Medicine');
INSERT INTO Doctor
(doctor_name, specialization_id, qualification, consultation_fee, contact)
VALUES
('Dr. Rakesh Kumar',1,'MBBS, MD',800.00,'9876543210'),
('Dr. Neha Shah',2,'MBBS, DM',1000.00,'9876543211'),
('Dr. Vikram Singh',3,'MBBS, MS',900.00,'9876543212'),
('Dr. Pooja Mehta',4,'MBBS, MD',700.00,'9876543213'),
('Dr. Arjun Patel',5,'MBBS',500.00,'9876543214');
INSERT INTO Patients
(patient_name, gender, age, blood_group, contact, address, registration_date)
VALUES
('Rahul Sharma','Male',25,'B+','9876501111','Vellore','2026-08-01'),
('Priya Patel','Female',30,'A+','9876501112','Chennai','2026-08-02'),
('Amit Verma','Male',40,'O+','9876501113','Bangalore','2026-08-03'),
('Sneha Joshi','Female',22,'AB+','9876501114','Mumbai','2026-08-04'),
('Karan Mehta','Male',35,'B-','9876501115','Pune','2026-08-05'),
('Anjali Shah','Female',28,'A-','9876501116','Ahmedabad','2026-08-06'),
('Rohan Gupta','Male',45,'O-','9876501117','Delhi','2026-08-07'),
('Meera Iyer','Female',32,'B+','9876501118','Hyderabad','2026-08-08'),
('Vikas Singh','Male',27,'AB-','9876501119','Jaipur','2026-08-09'),
('Nisha Kapoor','Female',29,'O+','9876501120','Surat','2026-08-10');
INSERT INTO Doctor_Schedule
(doctor_id, day_of_week, start_time, end_time)
VALUES
(1,'Monday','09:00:00','13:00:00'),
(2,'Tuesday','10:00:00','14:00:00'),
(3,'Wednesday','09:00:00','13:00:00'),
(4,'Thursday','11:00:00','15:00:00'),
(5,'Friday','09:00:00','12:00:00');
INSERT INTO Appointments
(patient_id, doctor_id, appointment_date, appointment_time, room_number, status)
VALUES
(1,1,'2026-08-20','09:30:00','101','Completed'),
(2,2,'2026-08-20','10:00:00','102','Completed'),
(3,3,'2026-08-21','11:00:00','103','Completed'),
(4,4,'2026-08-21','12:00:00','104','Completed'),
(5,5,'2026-08-22','09:00:00','105','Completed'),
(6,1,'2026-08-22','10:30:00','101','Completed'),
(7,2,'2026-08-23','11:30:00','102','Pending'),
(8,3,'2026-08-23','12:30:00','103','Completed'),
(9,4,'2026-08-24','09:15:00','104','Pending'),
(10,5,'2026-08-24','10:15:00','105','Completed');
INSERT INTO Prescriptions
(appointment_id, diagnosis, medicine, next_visit_date, remarks)
VALUES
(1,'High Blood Pressure','Amlodipine','2026-09-20','Regular checkup'),
(2,'Migraine','Sumatriptan','2026-09-18','Avoid stress'),
(3,'Knee Pain','Ibuprofen','2026-09-15','Exercise regularly'),
(4,'Skin Allergy','Cetirizine','2026-09-12','Avoid allergens'),
(5,'Fever','Paracetamol','2026-08-30','Drink water'),
(6,'Chest Pain','Aspirin','2026-09-25','Monitor health'),
(7,'Headache','Paracetamol','2026-09-01','Take rest'),
(8,'Back Pain','Diclofenac','2026-09-10','Physiotherapy'),
(9,'Acne','Clindamycin Gel','2026-09-20','Use cream daily'),
(10,'Cold','Cetirizine','2026-08-28','Steam inhalation');
INSERT INTO Billing
(appointment_id, amount, bill_date, payment_method, payment_status)
VALUES
(1,800.00,'2026-08-20','UPI','Paid'),
(2,1000.00,'2026-08-20','Cash','Paid'),
(3,900.00,'2026-08-21','Card','Paid'),
(4,700.00,'2026-08-21','UPI','Paid'),
(5,500.00,'2026-08-22','Cash','Paid'),
(6,800.00,'2026-08-22','Card','Paid'),
(7,1000.00,'2026-08-23','UPI','Pending'),
(8,900.00,'2026-08-23','Cash','Paid'),
(9,700.00,'2026-08-24','Card','Pending'),
(10,500.00,'2026-08-24','UPI','Paid');
INSERT INTO Feedback
(patient_id, rating, feedback_date, comments)
VALUES
(1,5,'2026-08-20','Excellent service'),
(2,4,'2026-08-20','Good doctor consultation'),
(3,5,'2026-08-21','Very satisfied'),
(4,4,'2026-08-21','Friendly staff'),
(5,5,'2026-08-22','Quick treatment');
INSERT INTO Feedback
(patient_id, rating, feedback_date, comments)
VALUES
(6,4,'2026-08-22','Good treatment'),
(7,5,'2026-08-23','Doctor was very helpful'),
(8,4,'2026-08-23','Satisfied with service'),
(9,3,'2026-08-24','Waiting time was long'),
(10,5,'2026-08-24','Excellent hospital facilities');
INSERT INTO Doctor_Schedule
(doctor_id, day_of_week, start_time, end_time)
VALUES
(1,'Wednesday','09:00:00','13:00:00'),
(2,'Thursday','10:00:00','14:00:00'),
(3,'Friday','09:00:00','13:00:00'),
(4,'Monday','11:00:00','15:00:00'),
(5,'Tuesday','09:00:00','12:00:00');
INSERT INTO Appointments
(patient_id, doctor_id, appointment_date, appointment_time, room_number, status)
VALUES
(1,3,'2026-09-01','11:00:00','103','Scheduled'),
(4,2,'2026-09-02','12:00:00','102','Pending');
INSERT INTO Billing
(appointment_id, amount, bill_date, payment_method, payment_status)
VALUES
(11,900.00,'2026-09-01','Cash','Pending'),
(12,1000.00,'2026-09-02','UPI','Pending');
SELECT * FROM Patients;
SELECT * FROM Doctor;
SELECT * FROM Appointments;
SHOW TABLES;
SELECT * FROM Patients;
DESC Patients;
SELECT * FROM Patients
ORDER BY patient_id DESC
LIMIT 5;
INSERT INTO Patients(patient_name, gender, age)
VALUES ('Nisarg', 'Male', 19);
SELECT COUNT(*) FROM Patients;
SELECT * FROM Patients;
SELECT * FROM Patients
ORDER BY patient_id DESC;
SELECT COUNT(*) FROM Patients;
SELECT DATABASE();
SELECT * FROM Patients
ORDER BY patient_id DESC;
SELECT patient_id, patient_name, gender, age
FROM Patients
ORDER BY patient_id DESC;
COMMIT;
SELECT COUNT(*) FROM Patients;
SELECT *
FROM Patients
WHERE patient_name = 'TestUser';
SELECT DATABASE();
SHOW TABLES;
SELECT COUNT(*) FROM Patients;
SELECT DATABASE();
SELECT COUNT(*) FROM Patients;
SELECT * FROM Patients;
SELECT COUNT(*) FROM Patients;
SELECT patient_id, patient_name
FROM Patients;
USE hospital;

SELECT patient_id, patient_name
FROM Patients
WHERE patient_id = 13;
SHOW CREATE TABLE Doctor;

DESC Doctor;
SELECT * FROM Doctor;
SELECT * FROM doctor;

SELECT * FROM doctor;
USE hospital;
SELECT * FROM Patients;
DESC Appointments;
SELECT * FROM Doctor;
USE hospital;

SELECT * FROM Appointments
ORDER BY appointment_id DESC;
SELECT * FROM Appointments
ORDER BY appointment_id DESC;
SELECT * 
FROM Appointments
WHERE appointment_id = 13;SELECT * FROM Appointments
ORDER BY appointment_id DESC;
SELECT * FROM Prescriptions
ORDER BY prescription_id DESC;
SELECT * FROM Prescriptions WHERE prescription_id = 11;
DESC Feedback;
SELECT * FROM Feedback;

SELECT * FROM Billing
ORDER BY bill_id DESC;
SELECT * FROM Billing
WHERE bill_id = 13;
SELECT p.patient_id,p.patient_name,d.doctor_name,a.appointment_date,a.appointment_time,a.room_number,a.status
FROM Patients p JOIN Appointments a
    ON p.patient_id = a.patient_id
JOIN Doctor d ON a.doctor_id = d.doctor_id;
SELECT
    d.doctor_id,
    d.doctor_name,
    s.specialization_name,
    d.qualification,
    d.consultation_fee,
    d.contact
FROM Doctor d
JOIN Specializations s
    ON d.specialization_id = s.specialization_id;
-- QUERY 3: Appointment + Billing

SELECT
    a.appointment_id,
    p.patient_name,
    d.doctor_name,
    a.appointment_date,
    b.amount,
    b.payment_method,
    b.payment_status
FROM Appointments a
JOIN Patients p
    ON a.patient_id = p.patient_id
JOIN Doctor d
    ON a.doctor_id = d.doctor_id
JOIN Billing b
    ON a.appointment_id = b.appointment_id;    
    -- QUERY 4: Patient + Prescription

SELECT
    p.patient_id,
    p.patient_name,
    pr.diagnosis,
    pr.medicine,
    pr.next_visit_date,
    pr.remarks
FROM Patients p
JOIN Appointments a
    ON p.patient_id = a.patient_id
JOIN Prescriptions pr
    ON a.appointment_id = pr.appointment_id;
    -- QUERY 5: Total Number of Patients

SELECT COUNT(*) AS total_patients
FROM Patients;
-- QUERY 6: Total Appointments Per Doctor

SELECT
    d.doctor_name,
    COUNT(a.appointment_id) AS total_appointments
FROM Doctor d
LEFT JOIN Appointments a
    ON d.doctor_id = a.doctor_id
GROUP BY d.doctor_id, d.doctor_name;
-- QUERY 7: Average Consultation Fee

SELECT
    AVG(consultation_fee) AS average_consultation_fee
FROM Doctor;
-- QUERY 8: Total Paid Revenue

SELECT
    SUM(amount) AS total_paid_revenue
FROM Billing
WHERE payment_status = 'Paid';
-- QUERY 9: Bills by Payment Status

SELECT
    payment_status,
    COUNT(*) AS total_bills
FROM Billing
GROUP BY payment_status;
-- QUERY 11: Doctors with More Than One Appointment

SELECT
    doctor_name
FROM Doctor
WHERE doctor_id IN
(
    SELECT doctor_id
    FROM Appointments
    GROUP BY doctor_id
    HAVING COUNT(*) > 1
);
SELECT * FROM Patients
ORDER BY patient_id DESC;
SHOW COLUMNS FROM Billing;
CREATE TABLE doctor_schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    available_day VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (doctor_id)
    REFERENCES doctors(doctor_id)
);
DESC doctor_schedule;
SELECT DATABASE();
SHOW TABLES;
SHOW CREATE TABLE Appointments;
SELECT MIN(doctor_id), MAX(doctor_id) FROM doctor;
INSERT INTO appointments
(patient_id, doctor_id, appointment_date, appointment_time, room_number, status)
VALUES
(1,1,'2026-08-20','09:30:00','101','Completed');
SHOW CREATE TABLE appointments;
SELECT COUNT(*) FROM doctor;
SELECT MIN(patient_id), MAX(patient_id)
FROM patients;
INSERT INTO appointments
(patient_id, doctor_id, appointment_date, appointment_time, room_number, status)
VALUES
(1,1,'2026-08-20','09:30:00','101','Completed');
INSERT INTO appointments
(patient_id, doctor_id, appointment_date, appointment_time, room_number, status)
VALUES
(1,1,'2026-08-20','09:30:00','101','Completed'),
(2,2,'2026-08-20','10:00:00','102','Completed'),
(3,3,'2026-08-21','11:00:00','103','Completed'),
(4,4,'2026-08-21','12:00:00','104','Completed'),
(5,5,'2026-08-22','09:00:00','105','Completed'),
(6,1,'2026-08-22','10:30:00','101','Completed'),
(7,2,'2026-08-23','11:30:00','102','Pending'),
(8,3,'2026-08-23','12:30:00','103','Completed'),
(9,4,'2026-08-24','09:15:00','104','Pending'),
(10,5,'2026-08-24','10:15:00','105','Completed');
SELECT COUNT(*) FROM appointments;
INSERT INTO Doctor_Schedule
(doctor_id, day_of_week, start_time, end_time)
VALUES
(1,'Monday','09:00:00','13:00:00'),
(2,'Tuesday','10:00:00','14:00:00'),
(3,'Wednesday','09:00:00','13:00:00'),
(4,'Thursday','11:00:00','15:00:00'),
(5,'Friday','09:00:00','12:00:00');
SELECT COUNT(*) FROM Doctor_Schedule;
SELECT MIN(appointment_id), MAX(appointment_id)
FROM appointments;
INSERT INTO Prescriptions
(appointment_id, diagnosis, medicine, next_visit_date, remarks)
VALUES
(12,'High Blood Pressure','Amlodipine','2026-09-20','Regular checkup'),
(13,'Migraine','Sumatriptan','2026-09-18','Avoid stress'),
(14,'Knee Pain','Ibuprofen','2026-09-15','Exercise regularly'),
(15,'Skin Allergy','Cetirizine','2026-09-12','Avoid allergens'),
(16,'Fever','Paracetamol','2026-08-30','Drink water'),
(17,'Chest Pain','Aspirin','2026-09-25','Monitor health'),
(18,'Headache','Paracetamol','2026-09-01','Take rest'),
(19,'Back Pain','Diclofenac','2026-09-10','Physiotherapy'),
(20,'Acne','Clindamycin Gel','2026-09-20','Use cream daily'),
(21,'Cold','Cetirizine','2026-08-28','Steam inhalation');
INSERT INTO Billing
(appointment_id, amount, bill_date, payment_method, payment_status)
VALUES
(12,800.00,'2026-08-20','UPI','Paid'),
(13,1000.00,'2026-08-20','Cash','Paid'),
(14,900.00,'2026-08-21','Card','Paid'),
(15,700.00,'2026-08-21','UPI','Paid'),
(16,500.00,'2026-08-22','Cash','Paid'),
(17,800.00,'2026-08-22','Card','Paid'),
(18,1000.00,'2026-08-23','UPI','Pending'),
(19,900.00,'2026-08-23','Cash','Paid'),
(20,700.00,'2026-08-24','Card','Pending'),
(21,500.00,'2026-08-24','UPI','Paid');
INSERT INTO Feedback
(patient_id, rating, feedback_date, comments)
VALUES
(1,5,'2026-08-20','Excellent service'),
(2,4,'2026-08-20','Doctor was very helpful'),
(3,5,'2026-08-21','Quick treatment'),
(4,3,'2026-08-21','Waiting time was long'),
(5,4,'2026-08-22','Good consultation'),
(6,5,'2026-08-22','Friendly staff'),
(7,4,'2026-08-23','Satisfied with treatment'),
(8,5,'2026-08-23','Very professional'),
(9,3,'2026-08-24','Room could be cleaner'),
(10,4,'2026-08-24','Good overall experience');
SELECT COUNT(*) FROM patients;
SELECT COUNT(*) FROM doctor;
SELECT COUNT(*) FROM appointments;
SELECT COUNT(*) FROM doctor_schedule;
SELECT COUNT(*) FROM prescriptions;
SELECT COUNT(*) FROM billing;
SELECT COUNT(*) FROM feedback;
SELECT * FROM doctor;
UPDATE doctor
SET doctor_name='TEST'
WHERE doctor_id=1;
SELECT * FROM appointments WHERE doctor_id = 1;
DELETE FROM doctor_schedule
WHERE doctor_id = 1;
DELETE FROM doctor
WHERE doctor_id = 1;
SELECT * FROM doctor;
DESC doctor_schedule;
SHOW CREATE TABLE Appointments;
SELECT doctor_id FROM doctor;
SELECT patient_id FROM patients;
SHOW CREATE TABLE prescriptions;
SHOW TABLES;
SELECT CURRENT_USER();
ALTER USER 'root'@'localhost'
IDENTIFIED BY 'Kalpana1979*';

FLUSH PRIVILEGES;
USE hospital;

SHOW TABLES;
CREATE TABLE emergency (
    emergency_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT,
    emergency_type VARCHAR(100),
    priority_level VARCHAR(20),
    arrival_time DATE,
    status VARCHAR(30),
    assigned_doctor INT
);
SHOW TABLES;
DESC emergency;
ALTER TABLE emergency
DROP COLUMN arrival_time;
ALTER TABLE emergency
DROP COLUMN arrival_time;
ALTER TABLE emergency
ADD arrival_date DATE,
ADD arrival_time TIME;
ALTER TABLE emergency
ADD CONSTRAINT fk_emergency_doctor
FOREIGN KEY (assigned_doctor) REFERENCES Doctor(doctor_id);
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Users (username, password, full_name, role)
VALUES ('admin', SHA2('admin123', 256), 'Administrator', 'Admin');
SELECT 
    a.appointment_id,
    p.patient_name,
    p.contact AS patient_contact,
    d.doctor_name,
    s.specialization_name,
    a.appointment_date,
    a.appointment_time,
    a.status
FROM Appointments a
JOIN Patients p ON a.patient_id = p.patient_id
JOIN Doctor d ON a.doctor_id = d.doctor_id
JOIN Specializations s ON d.specialization_id = s.specialization_id
ORDER BY a.appointment_date DESC;


SELECT 
    payment_method,
    COUNT(*) AS total_transactions,
    SUM(amount) AS total_revenue,
    AVG(amount) AS average_bill
FROM Billing
WHERE payment_status = 'Paid'
GROUP BY payment_method
HAVING SUM(amount) > 1000;


SELECT 
    doctor_name, 
    consultation_fee 
FROM Doctor
WHERE consultation_fee > (
    SELECT AVG(consultation_fee) FROM Doctor
);
-- 1. View all active appointments pre-joined with patient and doctor names:
SELECT * FROM v_ActiveAppointments;

-- 2. View financial summary by payment method:
SELECT * FROM v_HospitalRevenueSummary;

USE hospital;

-- Create View 1: Active Appointments
CREATE OR REPLACE VIEW v_ActiveAppointments AS
SELECT 
    a.appointment_id,
    p.patient_name,
    p.contact AS patient_contact,
    d.doctor_name,
    s.specialization_name,
    a.appointment_date,
    a.appointment_time,
    a.room_number,
    a.status
FROM Appointments a
JOIN Patients p ON a.patient_id = p.patient_id
JOIN Doctor d ON a.doctor_id = d.doctor_id
JOIN Specializations s ON d.specialization_id = s.specialization_id
WHERE a.status != 'Cancelled';

-- Create View 2: Revenue Summary
CREATE OR REPLACE VIEW v_HospitalRevenueSummary AS
SELECT 
    payment_method,
    payment_status,
    COUNT(*) AS total_bills,
    SUM(amount) AS total_amount,
    AVG(amount) AS average_amount
FROM Billing
GROUP BY payment_method, payment_status;
SELECT * FROM v_ActiveAppointments;
-- Stored Procedure: Patient Medical History
DELIMITER //
CREATE PROCEDURE sp_GetPatientHistory(IN p_patient_id INT)
BEGIN
    SELECT 
        p.patient_id,
        p.patient_name,
        p.age,
        p.blood_group,
        a.appointment_id,
        a.appointment_date,
        d.doctor_name,
        pr.diagnosis,
        pr.medicine,
        b.amount,
        b.payment_status
    FROM Patients p
    LEFT JOIN Appointments a ON p.patient_id = a.patient_id
    LEFT JOIN Doctor d ON a.doctor_id = d.doctor_id
    LEFT JOIN Prescriptions pr ON a.appointment_id = pr.appointment_id
    LEFT JOIN Billing b ON a.appointment_id = b.appointment_id
    WHERE p.patient_id = p_patient_id;
END //
DELIMITER ;

-- Trigger: Automatically Complete Appointment on Paid Bill
DELIMITER //
CREATE TRIGGER trg_AfterBillPaid
AFTER UPDATE ON Billing
FOR EACH ROW
BEGIN
    IF NEW.payment_status = 'Paid' AND OLD.payment_status != 'Paid' THEN
        UPDATE Appointments 
        SET status = 'Completed' 
        WHERE appointment_id = NEW.appointment_id;
    END IF;
END //
DELIMITER ;
	
SELECT * FROM v_HospitalRevenueSummary;
SELECT * FROM v_ActiveAppointments;
CREATE INDEX idx_patient_contact ON Patients(contact);
CREATE INDEX idx_appointment_date ON Appointments(appointment_date);
CREATE INDEX idx_billing_status ON Billing(payment_status);

-- ==================================================================================
-- NEW ENTITY 1: LAB_TESTS (DIAGNOSTIC LABORATORY MANAGEMENT)
-- ==================================================================================
CREATE TABLE IF NOT EXISTS Lab_Tests (
    test_id       INT AUTO_INCREMENT PRIMARY KEY,
    patient_id    INT NOT NULL,
    doctor_id     INT NOT NULL,
    test_name     VARCHAR(100) NOT NULL,
    test_date     DATE NOT NULL,
    cost          DECIMAL(10,2) NOT NULL,
    result        VARCHAR(255),
    status        VARCHAR(30) NOT NULL DEFAULT 'Pending',
    
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id)  REFERENCES Doctor(doctor_id) ON DELETE CASCADE
);

INSERT INTO Lab_Tests (patient_id, doctor_id, test_name, test_date, cost, result, status) VALUES
(1, 1, 'Complete Blood Count (CBC)', '2026-09-01', 450.00, 'Hemoglobin 13.5 g/dL (Normal)', 'Completed'),
(2, 2, 'Brain MRI Scan', '2026-09-02', 3500.00, 'Mild migraine changes, no lesion', 'Completed'),
(3, 3, 'Knee X-Ray (AP/Lateral)', '2026-09-03', 800.00, 'Grade 1 Osteoarthritis', 'Completed'),
(4, 4, 'Skin Allergy Panel', '2026-09-04', 1200.00, 'Positive for pollen & dust mite', 'Completed'),
(5, 5, 'Dengue Serology NS1', '2026-09-05', 600.00, 'Negative', 'Completed'),
(1, 1, 'Lipid Profile', '2026-09-06', 750.00, 'Pending Lab Analysis', 'Pending');

-- ==================================================================================
-- NEW ENTITY 2: BED_ALLOCATION (IN-PATIENT & WARD MANAGEMENT)
-- ==================================================================================
CREATE TABLE IF NOT EXISTS Bed_Allocation (
    allocation_id  INT AUTO_INCREMENT PRIMARY KEY,
    patient_id     INT NOT NULL,
    ward_type      VARCHAR(50) NOT NULL,
    bed_number     VARCHAR(10) NOT NULL,
    admit_date     DATE NOT NULL,
    discharge_date DATE,
    daily_charge   DECIMAL(10,2) NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'Occupied',
    
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE
);

INSERT INTO Bed_Allocation (patient_id, ward_type, bed_number, admit_date, discharge_date, daily_charge, status) VALUES
(1, 'General Ward', 'GW-101', '2026-08-20', '2026-08-25', 1500.00, 'Discharged'),
(2, 'ICU', 'ICU-04', '2026-08-22', '2026-08-26', 5000.00, 'Discharged'),
(3, 'Private AC Room', 'PVT-205', '2026-09-01', NULL, 3000.00, 'Occupied'),
(5, 'Emergency Ward', 'EMG-02', '2026-09-04', NULL, 2500.00, 'Occupied');

-- ==================================================================================
-- NEW ENTITY 3: AUDIT_LOGS (SYSTEM SECURITY & COMPLIANCE LOGGING)
-- ==================================================================================
CREATE TABLE IF NOT EXISTS Audit_Logs (
    log_id      INT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(50) NOT NULL,
    record_id   INT NOT NULL,
    log_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details     TEXT
);

INSERT INTO Audit_Logs (action_type, record_id, details) VALUES
('INITIAL_SEED', 1, 'System initialized with default admin account'),
('BED_ALLOCATED', 3, 'Patient 3 admitted to PVT-205'),
('LAB_TEST_ORDERED', 6, 'Lipid Profile ordered for Patient 1');

-- Trigger: Automatically Log Cancelled Appointments into Audit_Logs
DELIMITER //
CREATE TRIGGER trg_AuditAppointmentCancel
AFTER UPDATE ON Appointments
FOR EACH ROW
BEGIN
    IF NEW.status = 'Cancelled' AND OLD.status != 'Cancelled' THEN
        INSERT INTO Audit_Logs (action_type, record_id, details)
        VALUES ('APPOINTMENT_CANCELLED', NEW.appointment_id, CONCAT('Appointment cancelled for Patient ID: ', NEW.patient_id));
    END IF;
END //
DELIMITER ;

-- Date Arithmetic Query (Stay charges demonstration)
SELECT 
    b.allocation_id,
    p.patient_name,
    b.ward_type,
    b.admit_date,
    b.discharge_date,
    DATEDIFF(b.discharge_date, b.admit_date) AS total_days_admitted,
    DATEDIFF(b.discharge_date, b.admit_date) * b.daily_charge AS total_stay_charge
FROM Bed_Allocation b
JOIN Patients p ON b.patient_id = p.patient_id
WHERE b.status = 'Discharged';