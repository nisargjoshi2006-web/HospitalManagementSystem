-- QUERY 1: Patient + Doctor + Appointment

SELECT
    p.patient_id,
    p.patient_name,
    d.doctor_name,
    a.appointment_date,
    a.appointment_time,
    a.room_number,
    a.status
FROM Patients p
JOIN Appointments a
    ON p.patient_id = a.patient_id
JOIN Doctor d
    ON a.doctor_id = d.doctor_id;
    -- QUERY 2: Doctor + Specialization

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
-- QUERY 10: Doctors with Above-Average Consultation Fee

SELECT
    doctor_name,
    consultation_fee
FROM Doctor
WHERE consultation_fee >
(
    SELECT AVG(consultation_fee)
    FROM Doctor
);
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