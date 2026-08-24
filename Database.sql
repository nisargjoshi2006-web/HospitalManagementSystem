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

//MAIN CODE FOR THE DATABASE//
