import express from 'express';
import cors from 'cors';
import pool from './db.js';

const app = express();
const PORT = process.env.PORT || 5000;

app.use(cors());
app.use(express.json());

// ==========================================
// 1. AUTHENTICATION
// ==========================================
app.post('/api/auth/login', async (req, res) => {
  try {
    const { username, password } = req.body;
    if (!username || !password) {
      return res.status(400).json({ success: false, message: 'Username and password required' });
    }

    const [rows] = await pool.query(
      'SELECT user_id, username, full_name, role FROM Users WHERE username = ? AND password = SHA2(?, 256)',
      [username, password]
    );

    if (rows.length > 0) {
      const user = rows[0];
      return res.json({
        success: true,
        user: {
          id: user.user_id,
          username: user.username,
          name: user.full_name,
          role: user.role
        }
      });
    } else {
      return res.status(401).json({ success: false, message: 'Invalid username or password' });
    }
  } catch (err) {
    console.error('Login error:', err);
    res.status(500).json({ success: false, message: err.message });
  }
});

// ==========================================
// 2. DASHBOARD KPI & METRICS
// ==========================================
app.get('/api/dashboard', async (req, res) => {
  try {
    const [[{ totalPatients }]] = await pool.query('SELECT COUNT(*) AS totalPatients FROM Patients');
    const [[{ totalDoctors }]] = await pool.query('SELECT COUNT(*) AS totalDoctors FROM Doctor');
    const [[{ todayAppointments }]] = await pool.query('SELECT COUNT(*) AS todayAppointments FROM Appointments WHERE appointment_date = CURDATE()');
    const [[{ totalAppointments }]] = await pool.query('SELECT COUNT(*) AS totalAppointments FROM Appointments');
    const [[{ activeEmergencies }]] = await pool.query("SELECT COUNT(*) AS activeEmergencies FROM Emergency WHERE status != 'Discharged'");
    const [[{ occupiedBeds }]] = await pool.query("SELECT COUNT(*) AS occupiedBeds FROM Bed_Allocation WHERE status = 'Occupied'");
    const [[{ pendingLabTests }]] = await pool.query("SELECT COUNT(*) AS pendingLabTests FROM Lab_Tests WHERE status = 'Pending'");
    const [[{ pendingBills }]] = await pool.query("SELECT COUNT(*) AS pendingBills FROM Billing WHERE payment_status = 'Pending'");
    const [[{ totalRevenue }]] = await pool.query("SELECT COALESCE(SUM(amount), 0) AS totalRevenue FROM Billing WHERE payment_status = 'Paid'");

    const [recentAppointments] = await pool.query(`
      SELECT a.appointment_id AS id, p.patient_name AS patient, d.doctor_name AS doctor,
             a.appointment_time AS time, a.room_number AS room, a.status
      FROM Appointments a
      JOIN Patients p ON a.patient_id = p.patient_id
      JOIN Doctor d ON a.doctor_id = d.doctor_id
      ORDER BY a.appointment_date DESC, a.appointment_time ASC
      LIMIT 6
    `);

    const [recentEmergencies] = await pool.query(`
      SELECT e.emergency_id AS id, p.patient_name AS patient, e.priority_level AS priority,
             COALESCE(d.doctor_name, 'Unassigned') AS doctor, e.arrival_time AS arrival,
             e.status, e.emergency_type AS symptoms
      FROM Emergency e
      JOIN Patients p ON e.patient_id = p.patient_id
      LEFT JOIN Doctor d ON e.assigned_doctor = d.doctor_id
      ORDER BY e.arrival_date DESC, e.arrival_time DESC
      LIMIT 5
    `);

    res.json({
      kpis: {
        totalPatients,
        totalDoctors,
        todayAppointments,
        totalAppointments,
        activeEmergencies,
        occupiedBeds,
        totalBeds: 72,
        pendingLabTests,
        pendingBills,
        totalRevenue: Number(totalRevenue || 0)
      },
      recentAppointments,
      recentEmergencies
    });
  } catch (err) {
    console.error('Dashboard error:', err);
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 3. PATIENTS
// ==========================================
app.get('/api/patients', async (req, res) => {
  try {
    const { search, blood } = req.query;
    let sql = 'SELECT * FROM Patients WHERE 1=1';
    const params = [];

    if (search) {
      sql += ' AND (patient_name LIKE ? OR patient_id LIKE ? OR contact LIKE ?)';
      params.push(`%${search}%`, `%${search}%`, `%${search}%`);
    }
    if (blood && blood !== 'All') {
      sql += ' AND blood_group = ?';
      params.push(blood);
    }
    sql += ' ORDER BY patient_id DESC';

    const [rows] = await pool.query(sql, params);
    const formatted = rows.map(r => ({
      id: `P-${String(r.patient_id).padStart(4, '0')}`,
      rawId: r.patient_id,
      name: r.patient_name,
      age: r.age,
      gender: r.gender,
      blood: r.blood_group,
      contact: r.contact,
      address: r.address,
      registered: r.registration_date ? String(r.registration_date).slice(0, 10) : ''
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/patients', async (req, res) => {
  try {
    const { name, age, gender, blood, contact, address, registered } = req.body;
    const regDate = registered || new Date().toISOString().slice(0, 10);
    const [result] = await pool.query(
      'INSERT INTO Patients (patient_name, age, gender, blood_group, contact, address, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [name, parseInt(age) || 0, gender, blood, contact, address, regDate]
    );
    res.status(201).json({ success: true, patientId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/patients/:id', async (req, res) => {
  const patientId = req.params.id.replace(/^P-0*/, '');
  try {
    await pool.query('DELETE FROM Prescriptions WHERE appointment_id IN (SELECT appointment_id FROM Appointments WHERE patient_id=?)', [patientId]);
    await pool.query('DELETE FROM Billing WHERE appointment_id IN (SELECT appointment_id FROM Appointments WHERE patient_id=?)', [patientId]);
    await pool.query('DELETE FROM Appointments WHERE patient_id=?', [patientId]);
    await pool.query('DELETE FROM Feedback WHERE patient_id=?', [patientId]);
    await pool.query('DELETE FROM Emergency WHERE patient_id=?', [patientId]);
    await pool.query('DELETE FROM Bed_Allocation WHERE patient_id=?', [patientId]);
    await pool.query('DELETE FROM Lab_Tests WHERE patient_id=?', [patientId]);
    await pool.query('DELETE FROM Patients WHERE patient_id=?', [patientId]);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 4. DOCTORS & SPECIALIZATIONS
// ==========================================
app.get('/api/doctors', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT d.doctor_id, d.doctor_name, d.specialization_id, s.specialization_name,
             d.qualification, d.consultation_fee, d.contact,
             (SELECT COUNT(*) FROM Appointments WHERE doctor_id = d.doctor_id) AS total_patients
      FROM Doctor d
      LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id
      ORDER BY d.doctor_id ASC
    `);
    const formatted = rows.map(d => ({
      id: `D-${String(d.doctor_id).padStart(3, '0')}`,
      rawId: d.doctor_id,
      name: d.doctor_name,
      specialization: d.specialization_name || 'General',
      specializationId: d.specialization_id,
      qualification: d.qualification,
      fee: Number(d.consultation_fee),
      contact: d.contact,
      available: true,
      patients: d.total_patients || 0
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.get('/api/specializations', async (req, res) => {
  try {
    const [rows] = await pool.query('SELECT * FROM Specializations');
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/doctors', async (req, res) => {
  try {
    const { name, specializationId, specialization, qualification, fee, contact } = req.body;
    let specId = specializationId;
    if (!specId && specialization) {
      const [specs] = await pool.query('SELECT specialization_id FROM Specializations WHERE specialization_name = ?', [specialization]);
      if (specs.length > 0) {
        specId = specs[0].specialization_id;
      }
    }
    const [result] = await pool.query(
      'INSERT INTO Doctor (doctor_name, specialization_id, qualification, consultation_fee, contact) VALUES (?, ?, ?, ?, ?)',
      [name, specId || 1, qualification || 'MBBS, MD', parseFloat(fee) || 500, contact || '9876543210']
    );
    res.status(201).json({ success: true, doctorId: result.insertId });
  } catch (err) {
    console.error('Add doctor error:', err);
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/doctors/:id', async (req, res) => {
  const doctorId = req.params.id.replace(/^D-0*/, '');
  try {
    await pool.query('DELETE FROM Prescriptions WHERE appointment_id IN (SELECT appointment_id FROM Appointments WHERE doctor_id=?)', [doctorId]);
    await pool.query('DELETE FROM Billing WHERE appointment_id IN (SELECT appointment_id FROM Appointments WHERE doctor_id=?)', [doctorId]);
    await pool.query('DELETE FROM Appointments WHERE doctor_id=?', [doctorId]);
    await pool.query('DELETE FROM Doctor_Schedule WHERE doctor_id=?', [doctorId]);
    await pool.query('UPDATE Emergency SET assigned_doctor = NULL WHERE assigned_doctor=?', [doctorId]);
    await pool.query('DELETE FROM Lab_Tests WHERE doctor_id=?', [doctorId]);
    await pool.query('DELETE FROM Doctor WHERE doctor_id=?', [doctorId]);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 5. APPOINTMENTS
// ==========================================
app.get('/api/appointments', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT a.appointment_id, a.patient_id, p.patient_name,
             a.doctor_id, d.doctor_name, s.specialization_name,
             a.appointment_date, a.appointment_time, a.room_number, a.status
      FROM Appointments a
      JOIN Patients p ON a.patient_id = p.patient_id
      JOIN Doctor d ON a.doctor_id = d.doctor_id
      LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id
      ORDER BY a.appointment_date DESC, a.appointment_time ASC
    `);
    const formatted = rows.map(a => ({
      id: `APT-${String(a.appointment_id).padStart(3, '0')}`,
      rawId: a.appointment_id,
      patient: a.patient_name,
      patientId: `P-${String(a.patient_id).padStart(4, '0')}`,
      rawPatientId: a.patient_id,
      doctor: a.doctor_name,
      doctorId: a.doctor_id,
      specialization: a.specialization_name || 'General',
      date: a.appointment_date ? String(a.appointment_date).slice(0, 10) : '',
      time: a.appointment_time ? String(a.appointment_time).slice(0, 5) : '09:00',
      room: a.room_number || '101',
      status: a.status
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/appointments', async (req, res) => {
  try {
    const { patientId, doctorId, date, time, room, status } = req.body;
    const cleanPid = typeof patientId === 'string' ? parseInt(patientId.replace(/^P-0*/, '')) : patientId;
    const cleanDid = typeof doctorId === 'string' ? parseInt(doctorId.replace(/^D-0*/, '')) : doctorId;
    const [result] = await pool.query(
      'INSERT INTO Appointments (patient_id, doctor_id, appointment_date, appointment_time, room_number, status) VALUES (?, ?, ?, ?, ?, ?)',
      [cleanPid, cleanDid, date, time, room || '101', status || 'Scheduled']
    );
    res.status(201).json({ success: true, appointmentId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/appointments/:id', async (req, res) => {
  const aptId = req.params.id.replace(/^APT-0*/, '');
  try {
    await pool.query('DELETE FROM Prescriptions WHERE appointment_id=?', [aptId]);
    await pool.query('DELETE FROM Billing WHERE appointment_id=?', [aptId]);
    await pool.query('DELETE FROM Appointments WHERE appointment_id=?', [aptId]);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 6. BED ALLOCATION
// ==========================================
app.get('/api/beds', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT ba.allocation_id, ba.patient_id, p.patient_name,
             ba.ward_type, ba.bed_number, ba.admit_date, ba.discharge_date,
             ba.daily_charge, ba.status
      FROM Bed_Allocation ba
      JOIN Patients p ON ba.patient_id = p.patient_id
      ORDER BY ba.allocation_id DESC
    `);
    const formatted = rows.map(b => ({
      id: `${b.ward_type.slice(0, 3).toUpperCase()}-${b.bed_number}`,
      allocationId: b.allocation_id,
      patientId: b.patient_id,
      patient: b.patient_name,
      ward: b.ward_type,
      bedNumber: b.bed_number,
      admitDate: b.admit_date ? String(b.admit_date).slice(0, 10) : '',
      dischargeDate: b.discharge_date ? String(b.discharge_date).slice(0, 10) : null,
      dailyCharge: Number(b.daily_charge),
      status: b.status
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/beds/allocate', async (req, res) => {
  try {
    const { patientId, wardType, bedNumber, admitDate, dailyCharge } = req.body;
    const cleanPid = typeof patientId === 'string' ? parseInt(patientId.replace(/^P-0*/, '')) : patientId;

    const [[{ bedCount }]] = await pool.query(
      "SELECT COUNT(*) AS bedCount FROM Bed_Allocation WHERE ward_type = ? AND bed_number = ? AND status = 'Occupied'",
      [wardType, bedNumber]
    );
    if (bedCount > 0) {
      return res.status(400).json({ error: `Bed ${bedNumber} in ${wardType} is currently OCCUPIED!` });
    }

    const [[{ patientCount }]] = await pool.query(
      "SELECT COUNT(*) AS patientCount FROM Bed_Allocation WHERE patient_id = ? AND status = 'Occupied'",
      [cleanPid]
    );
    if (patientCount > 0) {
      return res.status(400).json({ error: `Patient ID ${cleanPid} is already admitted in a bed!` });
    }

    const [result] = await pool.query(
      "INSERT INTO Bed_Allocation (patient_id, ward_type, bed_number, admit_date, daily_charge, status) VALUES (?, ?, ?, ?, ?, 'Occupied')",
      [cleanPid, wardType, bedNumber, admitDate || new Date().toISOString().slice(0, 10), dailyCharge || 1500.00]
    );
    res.status(201).json({ success: true, allocationId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/beds/discharge', async (req, res) => {
  try {
    const { allocationId, dischargeDate } = req.body;
    await pool.query(
      "UPDATE Bed_Allocation SET discharge_date = ?, status = 'Discharged' WHERE allocation_id = ?",
      [dischargeDate || new Date().toISOString().slice(0, 10), allocationId]
    );
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 7. BILLING
// ==========================================
app.get('/api/billing', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT b.bill_id, b.appointment_id, p.patient_name, d.doctor_name,
             b.amount, b.bill_date, b.payment_method, b.payment_status
      FROM Billing b
      JOIN Appointments a ON b.appointment_id = a.appointment_id
      JOIN Patients p ON a.patient_id = p.patient_id
      JOIN Doctor d ON a.doctor_id = d.doctor_id
      ORDER BY b.bill_id DESC
    `);
    const formatted = rows.map(b => ({
      id: `BILL-${String(b.bill_id).padStart(3, '0')}`,
      rawId: b.bill_id,
      patient: b.patient_name,
      doctor: b.doctor_name,
      appointment: `APT-${String(b.appointment_id).padStart(3, '0')}`,
      fee: Number(b.amount),
      method: b.payment_method,
      status: b.payment_status,
      date: b.bill_date ? String(b.bill_date).slice(0, 10) : ''
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/billing', async (req, res) => {
  try {
    const { appointmentId, amount, billDate, paymentMethod, paymentStatus } = req.body;
    const cleanAid = typeof appointmentId === 'string' ? parseInt(appointmentId.replace(/^APT-0*/, '')) : appointmentId;
    let billAmount = amount;
    if (!billAmount) {
      const [feeRows] = await pool.query(
        'SELECT d.consultation_fee FROM Appointments a JOIN Doctor d ON a.doctor_id = d.doctor_id WHERE a.appointment_id = ?',
        [cleanAid]
      );
      billAmount = feeRows.length > 0 ? feeRows[0].consultation_fee : 500.00;
    }
    const [result] = await pool.query(
      'INSERT INTO Billing (appointment_id, amount, bill_date, payment_method, payment_status) VALUES (?, ?, ?, ?, ?)',
      [cleanAid, billAmount, billDate || new Date().toISOString().slice(0, 10), paymentMethod || 'Cash', paymentStatus || 'Pending']
    );
    res.status(201).json({ success: true, billId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.put('/api/billing/:id', async (req, res) => {
  const billId = req.params.id.replace(/^BILL-0*/, '');
  try {
    const { paymentMethod, paymentStatus } = req.body;
    await pool.query(
      'UPDATE Billing SET payment_method = ?, payment_status = ? WHERE bill_id = ?',
      [paymentMethod || 'Cash', paymentStatus || 'Paid', billId]
    );
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 8. EMERGENCY
// ==========================================
app.get('/api/emergency', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT e.emergency_id, e.patient_id, p.patient_name, e.emergency_type, e.priority_level,
             e.arrival_date, e.arrival_time, e.status, e.assigned_doctor, d.doctor_name
      FROM Emergency e
      JOIN Patients p ON e.patient_id = p.patient_id
      LEFT JOIN Doctor d ON e.assigned_doctor = d.doctor_id
      ORDER BY e.emergency_id DESC
    `);
    const formatted = rows.map(e => ({
      id: `EM-${String(e.emergency_id).padStart(3, '0')}`,
      rawId: e.emergency_id,
      patientId: e.patient_id,
      patient: e.patient_name,
      priority: e.priority_level,
      doctor: e.doctor_name || 'Unassigned',
      arrival: e.arrival_time ? String(e.arrival_time).slice(0, 5) : '08:00',
      status: e.status,
      symptoms: e.emergency_type
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/emergency', async (req, res) => {
  try {
    const { patientId, emergencyType, priorityLevel, arrivalDate, arrivalTime, status, assignedDoctor } = req.body;
    const cleanPid = typeof patientId === 'string' ? parseInt(patientId.replace(/^P-0*/, '')) : patientId;
    const cleanDid = assignedDoctor ? (typeof assignedDoctor === 'string' ? parseInt(assignedDoctor.replace(/^D-0*/, '')) : assignedDoctor) : null;
    const [result] = await pool.query(
      'INSERT INTO Emergency (patient_id, emergency_type, priority_level, arrival_date, arrival_time, status, assigned_doctor) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [cleanPid, emergencyType || 'Emergency', priorityLevel || 'High', arrivalDate || new Date().toISOString().slice(0, 10), arrivalTime || '10:00:00', status || 'Admitted', cleanDid]
    );
    res.status(201).json({ success: true, emergencyId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 9. LAB TESTS
// ==========================================
app.get('/api/lab-tests', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT lt.test_id, lt.patient_id, p.patient_name, lt.doctor_id, d.doctor_name, lt.test_name,
             lt.test_date, lt.cost, lt.result, lt.status
      FROM Lab_Tests lt
      JOIN Patients p ON lt.patient_id = p.patient_id
      JOIN Doctor d ON lt.doctor_id = d.doctor_id
      ORDER BY lt.test_id DESC
    `);
    const formatted = rows.map(l => ({
      id: `LAB-${String(l.test_id).padStart(3, '0')}`,
      rawId: l.test_id,
      patientId: l.patient_id,
      patient: l.patient_name,
      doctor: l.doctor_name,
      type: l.test_name,
      ordered: l.test_date ? String(l.test_date).slice(0, 10) : '',
      cost: Number(l.cost),
      result: l.result || 'Pending',
      status: l.status
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/lab-tests', async (req, res) => {
  try {
    const { patientId, doctorId, testName, testDate, cost, result, status } = req.body;
    const cleanPid = typeof patientId === 'string' ? parseInt(patientId.replace(/^P-0*/, '')) : patientId;
    const cleanDid = typeof doctorId === 'string' ? parseInt(doctorId.replace(/^D-0*/, '')) : doctorId;
    const [dbRes] = await pool.query(
      'INSERT INTO Lab_Tests (patient_id, doctor_id, test_name, test_date, cost, result, status) VALUES (?, ?, ?, ?, ?, ?, ?)',
      [cleanPid, cleanDid, testName, testDate || new Date().toISOString().slice(0, 10), cost || 500.00, result || 'Pending', status || 'Pending']
    );
    res.status(201).json({ success: true, testId: dbRes.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.put('/api/lab-tests/:id', async (req, res) => {
  const testId = req.params.id.replace(/^LAB-0*/, '');
  try {
    const { result, status } = req.body;
    await pool.query(
      'UPDATE Lab_Tests SET result = ?, status = ? WHERE test_id = ?',
      [result, status || 'Completed', testId]
    );
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 10. PRESCRIPTIONS
// ==========================================
app.get('/api/prescriptions', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT pr.prescription_id, pr.appointment_id, p.patient_name, d.doctor_name,
             pr.diagnosis, pr.medicine, pr.next_visit_date, pr.remarks, a.appointment_date
      FROM Prescriptions pr
      JOIN Appointments a ON pr.appointment_id = a.appointment_id
      JOIN Patients p ON a.patient_id = p.patient_id
      JOIN Doctor d ON a.doctor_id = d.doctor_id
      ORDER BY pr.prescription_id DESC
    `);
    const formatted = rows.map(r => ({
      id: `RX-${String(r.prescription_id).padStart(3, '0')}`,
      rawId: r.prescription_id,
      patient: r.patient_name,
      doctor: r.doctor_name,
      diagnosis: r.diagnosis,
      medicine: r.medicine,
      nextVisit: r.next_visit_date ? String(r.next_visit_date).slice(0, 10) : 'None',
      remarks: r.remarks,
      date: r.appointment_date ? String(r.appointment_date).slice(0, 10) : ''
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/prescriptions', async (req, res) => {
  try {
    const { appointmentId, diagnosis, medicine, nextVisitDate, remarks } = req.body;
    const cleanAid = typeof appointmentId === 'string' ? parseInt(appointmentId.replace(/^APT-0*/, '')) : appointmentId;
    const nextDate = nextVisitDate && nextVisitDate.trim() ? nextVisitDate : null;
    const [result] = await pool.query(
      'INSERT INTO Prescriptions (appointment_id, diagnosis, medicine, next_visit_date, remarks) VALUES (?, ?, ?, ?, ?)',
      [cleanAid, diagnosis, medicine, nextDate, remarks || '']
    );
    res.status(201).json({ success: true, prescriptionId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 11. DOCTOR SCHEDULE
// ==========================================
app.get('/api/schedules', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT ds.schedule_id, ds.doctor_id, d.doctor_name, ds.day_of_week,
             ds.start_time, ds.end_time, s.specialization_name
      FROM Doctor_Schedule ds
      JOIN Doctor d ON ds.doctor_id = d.doctor_id
      LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id
      ORDER BY FIELD(ds.day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')
    `);
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/schedules', async (req, res) => {
  try {
    const { doctorId, dayOfWeek, startTime, endTime } = req.body;
    const cleanDid = typeof doctorId === 'string' ? parseInt(doctorId.replace(/^D-0*/, '')) : doctorId;
    const [result] = await pool.query(
      'INSERT INTO Doctor_Schedule (doctor_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)',
      [cleanDid, dayOfWeek, startTime, endTime]
    );
    res.status(201).json({ success: true, scheduleId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.delete('/api/schedules/:id', async (req, res) => {
  try {
    await pool.query('DELETE FROM Doctor_Schedule WHERE schedule_id = ?', [req.params.id]);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 12. FEEDBACK
// ==========================================
app.get('/api/feedback', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT f.feedback_id, p.patient_name, f.rating, f.feedback_date, f.comments
      FROM Feedback f
      JOIN Patients p ON f.patient_id = p.patient_id
      ORDER BY f.feedback_id DESC
    `);
    const formatted = rows.map(f => ({
      id: `FB-${String(f.feedback_id).padStart(3, '0')}`,
      rawId: f.feedback_id,
      patient: f.patient_name,
      rating: f.rating,
      date: f.feedback_date ? String(f.feedback_date).slice(0, 10) : '',
      comment: f.comments
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

app.post('/api/feedback', async (req, res) => {
  try {
    const { patientId, rating, feedbackDate, comments } = req.body;
    const cleanPid = typeof patientId === 'string' ? parseInt(patientId.replace(/^P-0*/, '')) : patientId;
    const [result] = await pool.query(
      'INSERT INTO Feedback (patient_id, rating, feedback_date, comments) VALUES (?, ?, ?, ?)',
      [cleanPid, rating || 5, feedbackDate || new Date().toISOString().slice(0, 10), comments || '']
    );
    res.status(201).json({ success: true, feedbackId: result.insertId });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 13. AUDIT LOGS
// ==========================================
app.get('/api/audit-logs', async (req, res) => {
  try {
    const [rows] = await pool.query('SELECT * FROM Audit_Logs ORDER BY log_id DESC LIMIT 50');
    const formatted = rows.map(l => ({
      id: `LOG-${String(l.log_id).padStart(3, '0')}`,
      timestamp: l.log_time ? String(l.log_time).replace('T', ' ').slice(0, 19) : '',
      user: 'system',
      action: l.action_type,
      module: 'Hospital',
      recordId: String(l.record_id),
      description: l.details,
      severity: l.action_type && l.action_type.includes('CANCEL') ? 'Warning' : 'Info'
    }));
    res.json(formatted);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// ==========================================
// 14. REPORTS & ANALYTICS
// ==========================================
app.get('/api/reports/revenue-by-doctor', async (req, res) => {
  try {
    const [rows] = await pool.query(`
      SELECT d.doctor_id, d.doctor_name, s.specialization_name,
             COUNT(b.bill_id) AS total_bills,
             COALESCE(SUM(b.amount), 0) AS total_revenue
      FROM Doctor d
      LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id
      LEFT JOIN Appointments a ON d.doctor_id = a.doctor_id
      LEFT JOIN Billing b ON a.appointment_id = b.appointment_id AND b.payment_status = 'Paid'
      GROUP BY d.doctor_id, d.doctor_name, s.specialization_name
      ORDER BY total_revenue DESC
    `);
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Health check
app.get('/api/health', (req, res) => {
  res.json({ status: 'ONLINE', timestamp: new Date().toISOString() });
});

app.listen(PORT, () => {
  console.log(`Hospital REST API server running on http://localhost:${PORT}`);
});
