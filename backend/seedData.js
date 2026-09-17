import pool from './db.js';

async function seed() {
  console.log('--- Starting Comprehensive Data Seeding ---');

  // 1. Add doctors if not present
  console.log('1. Checking & inserting Doctors...');
  const newDoctors = [
    { name: 'Dr. Priya Sharma', specId: 4, qual: 'MBBS, MD (Dermatology)', fee: 800, contact: '9876543220' },
    { name: 'Dr. Rajesh Kumar', specId: 1, qual: 'MBBS, DM (Cardiology)', fee: 1200, contact: '9876543221' },
    { name: 'Dr. Ananya Sen', specId: 2, qual: 'MBBS, DM (Neurology)', fee: 1000, contact: '9876543222' },
    { name: 'Dr. Rohan Verma', specId: 3, qual: 'MBBS, MS (Orthopedics)', fee: 850, contact: '9876543223' },
  ];

  for (const doc of newDoctors) {
    const [existing] = await pool.query('SELECT doctor_id FROM Doctor WHERE doctor_name = ?', [doc.name]);
    if (existing.length === 0) {
      await pool.query(
        'INSERT INTO Doctor (doctor_name, specialization_id, qualification, consultation_fee, contact) VALUES (?, ?, ?, ?, ?)',
        [doc.name, doc.specId, doc.qual, doc.fee, doc.contact]
      );
      console.log(`Inserted doctor: ${doc.name}`);
    }
  }

  // Fetch all current doctors
  const [allDoctors] = await pool.query(`
    SELECT d.doctor_id, d.doctor_name, s.specialization_name 
    FROM Doctor d 
    LEFT JOIN Specializations s ON d.specialization_id = s.specialization_id
  `);
  console.log(`Total Doctors available: ${allDoctors.length}`);

  // 2. Doctor Schedules (Weekly Timetable)
  console.log('2. Populating Doctor Schedules (Weekly Shifts)...');
  // Clear old schedules to ensure clean timetable structure
  await pool.query('DELETE FROM Doctor_Schedule');

  // Realistic weekly schedule templates for each doctor
  const weeklyShifts = [
    // Monday
    { docName: 'Dr. Vikram Singh', day: 'Monday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Pooja Mehta', day: 'Monday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Manav', day: 'Monday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Priya Sharma', day: 'Monday', start: '14:00:00', end: '18:00:00' },
    { docName: 'Dr. Rajesh Kumar', day: 'Monday', start: '15:00:00', end: '19:00:00' },
    { docName: 'Dr. Arjun Patel', day: 'Monday', start: '16:00:00', end: '20:00:00' },

    // Tuesday
    { docName: 'Dr. Ananya Sen', day: 'Tuesday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Arjun Patel', day: 'Tuesday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Priya Sharma', day: 'Tuesday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Rohan Verma', day: 'Tuesday', start: '14:00:00', end: '18:00:00' },
    { docName: 'Dr. Vikram Singh', day: 'Tuesday', start: '14:00:00', end: '18:00:00' },
    { docName: 'Dr. Manav', day: 'Tuesday', start: '15:00:00', end: '19:00:00' },

    // Wednesday
    { docName: 'Dr. Pooja Mehta', day: 'Wednesday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Rajesh Kumar', day: 'Wednesday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Rohan Verma', day: 'Wednesday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Ananya Sen', day: 'Wednesday', start: '14:00:00', end: '18:00:00' },
    { docName: 'Dr. Priya Sharma', day: 'Wednesday', start: '15:00:00', end: '19:00:00' },
    { docName: 'Shila', day: 'Wednesday', start: '16:00:00', end: '20:00:00' },

    // Thursday
    { docName: 'Dr. Vikram Singh', day: 'Thursday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Pooja Mehta', day: 'Thursday', start: '11:00:00', end: '15:00:00' },
    { docName: 'Dr. Manav', day: 'Thursday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Shila', day: 'Thursday', start: '14:00:00', end: '18:00:00' },
    { docName: 'Dr. Arjun Patel', day: 'Thursday', start: '16:00:00', end: '20:00:00' },
    { docName: 'Dr. Rajesh Kumar', day: 'Thursday', start: '16:00:00', end: '20:00:00' },

    // Friday
    { docName: 'Dr. Priya Sharma', day: 'Friday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Rajesh Kumar', day: 'Friday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Rohan Verma', day: 'Friday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Pooja Mehta', day: 'Friday', start: '14:00:00', end: '18:00:00' },
    { docName: 'Dr. Ananya Sen', day: 'Friday', start: '15:00:00', end: '19:00:00' },
    { docName: 'Dr. Vikram Singh', day: 'Friday', start: '16:00:00', end: '20:00:00' },

    // Saturday
    { docName: 'Dr. Arjun Patel', day: 'Saturday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Manav', day: 'Saturday', start: '09:00:00', end: '13:00:00' },
    { docName: 'Dr. Vikram Singh', day: 'Saturday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Priya Sharma', day: 'Saturday', start: '11:00:00', end: '15:00:00' },
    { docName: 'Dr. Rohan Verma', day: 'Saturday', start: '14:00:00', end: '18:00:00' },

    // Sunday (On-call / emergency rounds)
    { docName: 'Dr. Arjun Patel', day: 'Sunday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Rajesh Kumar', day: 'Sunday', start: '10:00:00', end: '14:00:00' },
    { docName: 'Dr. Pooja Mehta', day: 'Sunday', start: '14:00:00', end: '18:00:00' }
  ];

  for (const s of weeklyShifts) {
    const doc = allDoctors.find(d => d.doctor_name.toLowerCase().includes(s.docName.toLowerCase()));
    if (doc) {
      await pool.query(
        'INSERT INTO Doctor_Schedule (doctor_id, day_of_week, start_time, end_time) VALUES (?, ?, ?, ?)',
        [doc.doctor_id, s.day, s.start, s.end]
      );
    }
  }
  console.log('Doctor Schedules inserted successfully.');

  // 3. Appointments & Prescriptions & Billing
  console.log('3. Checking Appointments, Prescriptions, and Billing...');
  const [patRows] = await pool.query('SELECT patient_id FROM Patients ORDER BY patient_id ASC LIMIT 20');
  const pids = patRows.map(p => p.patient_id);
  const dids = allDoctors.map(d => d.doctor_id);

  // Generate today's date, yesterday, tomorrow, and this week
  const todayStr = new Date().toISOString().slice(0, 10);
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);
  const tomorrowStr = tomorrow.toISOString().slice(0, 10);
  const nextDay = new Date();
  nextDay.setDate(nextDay.getDate() + 2);
  const nextDayStr = nextDay.toISOString().slice(0, 10);
  const yesterday = new Date();
  yesterday.setDate(yesterday.getDate() - 1);
  const yesterdayStr = yesterday.toISOString().slice(0, 10);
  const pastDay = new Date();
  pastDay.setDate(pastDay.getDate() - 5);
  const pastDayStr = pastDay.toISOString().slice(0, 10);

  const sampleAppointments = [
    // Today's appointments (KPI: todayAppointments)
    { pid: pids[0] || 1, did: dids[0], date: todayStr, time: '09:30:00', room: 'OPD-101', status: 'Scheduled' },
    { pid: pids[1] || 3, did: dids[1] || dids[0], date: todayStr, time: '10:15:00', room: 'OPD-102', status: 'Scheduled' },
    { pid: pids[2] || 4, did: dids[2] || dids[0], date: todayStr, time: '11:00:00', room: 'OPD-105', status: 'Scheduled' },
    { pid: pids[3] || 5, did: dids[3] || dids[0], date: todayStr, time: '14:30:00', room: 'OPD-104', status: 'Scheduled' },
    { pid: pids[4] || 6, did: dids[4] || dids[0], date: todayStr, time: '16:00:00', room: 'OPD-106', status: 'Scheduled' },

    // Tomorrow & Upcoming
    { pid: pids[5] || 7, did: dids[0], date: tomorrowStr, time: '09:45:00', room: 'OPD-101', status: 'Scheduled' },
    { pid: pids[6] || 8, did: dids[1] || dids[0], date: tomorrowStr, time: '11:30:00', room: 'OPD-102', status: 'Scheduled' },
    { pid: pids[7] || 9, did: dids[2] || dids[0], date: nextDayStr, time: '10:00:00', room: 'OPD-103', status: 'Scheduled' },

    // Yesterday & Past (Completed / Billed)
    { pid: pids[0] || 1, did: dids[1] || dids[0], date: yesterdayStr, time: '10:00:00', room: 'OPD-102', status: 'Completed' },
    { pid: pids[1] || 3, did: dids[2] || dids[0], date: yesterdayStr, time: '14:00:00', room: 'OPD-103', status: 'Completed' },
    { pid: pids[2] || 4, did: dids[3] || dids[0], date: pastDayStr, time: '11:30:00', room: 'OPD-104', status: 'Completed' },
    { pid: pids[3] || 5, did: dids[4] || dids[0], date: pastDayStr, time: '15:15:00', room: 'OPD-105', status: 'Completed' },
    { pid: pids[4] || 6, did: dids[0], date: pastDayStr, time: '09:15:00', room: 'OPD-101', status: 'Completed' },
  ];

  for (const apt of sampleAppointments) {
    const [existing] = await pool.query(
      'SELECT appointment_id FROM Appointments WHERE patient_id = ? AND doctor_id = ? AND appointment_date = ? AND appointment_time = ?',
      [apt.pid, apt.did, apt.date, apt.time]
    );
    if (existing.length === 0) {
      const [res] = await pool.query(
        'INSERT INTO Appointments (patient_id, doctor_id, appointment_date, appointment_time, room_number, status) VALUES (?, ?, ?, ?, ?, ?)',
        [apt.pid, apt.did, apt.date, apt.time, apt.room, apt.status]
      );
      const newAptId = res.insertId;

      // If completed, add Prescription & Billing
      if (apt.status === 'Completed') {
        const nextVisit = new Date();
        nextVisit.setDate(nextVisit.getDate() + 14);
        await pool.query(
          'INSERT INTO Prescriptions (appointment_id, diagnosis, medicine, next_visit_date, remarks) VALUES (?, ?, ?, ?, ?)',
          [
            newAptId,
            'Routine Clinical Evaluation & Management',
            'Tab Multivitamin 1 OD + Paracetamol 650mg SOS',
            nextVisit.toISOString().slice(0, 10),
            'Take after meals, follow up in two weeks'
          ]
        );

        await pool.query(
          'INSERT INTO Billing (appointment_id, amount, bill_date, payment_method, payment_status) VALUES (?, ?, ?, ?, ?)',
          [newAptId, 850.00, apt.date, 'UPI', 'Paid']
        );
      } else {
        // Scheduled appointment with pending bill
        await pool.query(
          'INSERT INTO Billing (appointment_id, amount, bill_date, payment_method, payment_status) VALUES (?, ?, ?, ?, ?)',
          [newAptId, 750.00, apt.date, 'Pending', 'Pending']
        );
      }
    }
  }
  console.log('Appointments & Billing synced.');

  // 4. Emergency Cases
  console.log('4. Checking & adding Emergency cases...');
  const emergencyCases = [
    { pid: pids[1] || 3, type: 'Acute Myocardial Infarction', prio: 'Critical', status: 'Admitted', doc: dids[0], date: todayStr, time: '06:45:00' },
    { pid: pids[2] || 4, type: 'Head Injury / Concussion', prio: 'High', status: 'Under Treatment', doc: dids[1] || dids[0], date: todayStr, time: '08:15:00' },
    { pid: pids[3] || 5, type: 'Severe Asthma Attack', prio: 'High', status: 'In Observation', doc: dids[2] || dids[0], date: todayStr, time: '11:20:00' },
    { pid: pids[4] || 6, type: 'Acute Abdominal Pain', prio: 'Medium', status: 'Under Treatment', doc: dids[3] || dids[0], date: todayStr, time: '13:05:00' },
    { pid: pids[5] || 7, type: 'High Grade Fever & Dehydration', prio: 'Low', status: 'In Observation', doc: dids[0], date: yesterdayStr, time: '19:40:00' }
  ];

  for (const em of emergencyCases) {
    const [exist] = await pool.query(
      'SELECT emergency_id FROM Emergency WHERE patient_id = ? AND emergency_type = ?',
      [em.pid, em.type]
    );
    if (exist.length === 0) {
      await pool.query(
        'INSERT INTO Emergency (patient_id, emergency_type, priority_level, status, assigned_doctor, arrival_date, arrival_time) VALUES (?, ?, ?, ?, ?, ?, ?)',
        [em.pid, em.type, em.prio, em.status, em.doc, em.date, em.time]
      );
    }
  }
  console.log('Emergency cases populated.');

  // 5. Bed Allocations (carefully respecting trg_CheckBedAllocationInsert)
  console.log('5. Populating Bed Allocations...');
  // Check which beds and patients are currently occupied
  const [occupied] = await pool.query("SELECT ward_type, bed_number, patient_id FROM Bed_Allocation WHERE status = 'Occupied'");
  const occupiedBeds = new Set(occupied.map(o => `${o.ward_type}_${o.bed_number}`));
  const occupiedPids = new Set(occupied.map(o => o.patient_id));

  const candidateBeds = [
    { pid: pids[1] || 3, ward: 'ICU', bed: 'ICU-101', charge: 4500, date: todayStr, status: 'Occupied' },
    { pid: pids[2] || 4, ward: 'ICU', bed: 'ICU-102', charge: 4500, date: yesterdayStr, status: 'Occupied' },
    { pid: pids[3] || 5, ward: 'Private AC Room', bed: 'PVT-201', charge: 3000, date: pastDayStr, status: 'Occupied' },
    { pid: pids[4] || 6, ward: 'General Ward', bed: 'GW-102', charge: 1500, date: pastDayStr, status: 'Occupied' },
    { pid: pids[5] || 7, ward: 'General Ward', bed: 'GW-103', charge: 1500, date: pastDayStr, status: 'Occupied' },
    // Historical discharged admissions
    { pid: pids[6] || 8, ward: 'General Ward', bed: 'GW-104', charge: 1500, date: '2026-08-10', dis: '2026-08-15', status: 'Discharged' },
    { pid: pids[7] || 9, ward: 'Private AC Room', bed: 'PVT-202', charge: 3000, date: '2026-08-12', dis: '2026-08-18', status: 'Discharged' },
    { pid: pids[8] || 10, ward: 'ICU', bed: 'ICU-103', charge: 4500, date: '2026-08-20', dis: '2026-08-25', status: 'Discharged' },
  ];

  for (const b of candidateBeds) {
    const bedKey = `${b.ward}_${b.bed}`;
    if (b.status === 'Occupied') {
      if (occupiedBeds.has(bedKey) || occupiedPids.has(b.pid)) {
        continue; // skip if bed or patient is already occupied
      }
    }
    const [exist] = await pool.query(
      'SELECT allocation_id FROM Bed_Allocation WHERE patient_id = ? AND ward_type = ? AND bed_number = ?',
      [b.pid, b.ward, b.bed]
    );
    if (exist.length === 0) {
      await pool.query(
        'INSERT INTO Bed_Allocation (patient_id, ward_type, bed_number, admit_date, discharge_date, daily_charge, status) VALUES (?, ?, ?, ?, ?, ?, ?)',
        [b.pid, b.ward, b.bed, b.date, b.dis || null, b.charge, b.status]
      );
      if (b.status === 'Occupied') {
        occupiedBeds.add(bedKey);
        occupiedPids.add(b.pid);
      }
    }
  }
  console.log('Bed allocations updated.');

  // 6. Lab Tests
  console.log('6. Populating Lab Tests...');
  const sampleLabTests = [
    { pid: pids[0] || 1, did: dids[0], test: 'Cardiac Troponin-I', cost: 1200, res: '0.02 ng/mL (Normal Baseline)', status: 'Completed', date: todayStr },
    { pid: pids[1] || 3, did: dids[1] || dids[0], test: '12-Lead Electrocardiogram (ECG)', cost: 600, res: 'Normal Sinus Rhythm, No ST elevation', status: 'Completed', date: todayStr },
    { pid: pids[2] || 4, did: dids[2] || dids[0], test: 'Complete Blood Count (CBC) with Platelets', cost: 450, res: 'Platelets: 280,000 /mcL, Hb: 14.2 g/dL', status: 'Completed', date: yesterdayStr },
    { pid: pids[3] || 5, did: dids[3] || dids[0], test: 'Comprehensive Lipid Profile', cost: 850, res: 'Cholesterol 185 mg/dL, HDL 48 mg/dL, LDL 110 mg/dL', status: 'Completed', date: yesterdayStr },
    { pid: pids[4] || 6, did: dids[0], test: 'Glycated Hemoglobin (HbA1c)', cost: 750, res: '6.2% (Prediabetes monitoring)', status: 'Completed', date: pastDayStr },
    { pid: pids[5] || 7, did: dids[1] || dids[0], test: 'High-Resolution Chest CT Scan', cost: 3800, res: 'Sample under radiological review', status: 'Pending', date: todayStr },
    { pid: pids[6] || 8, did: dids[2] || dids[0], test: 'Renal Function Panel (BUN & Creatinine)', cost: 650, res: 'Pending specimen analysis', status: 'Pending', date: todayStr },
    { pid: pids[7] || 9, did: dids[3] || dids[0], test: 'Thyroid Stimulating Hormone (TSH)', cost: 550, res: 'Pending specimen analysis', status: 'Pending', date: tomorrowStr }
  ];

  for (const lt of sampleLabTests) {
    const [exist] = await pool.query(
      'SELECT test_id FROM Lab_Tests WHERE patient_id = ? AND test_name = ?',
      [lt.pid, lt.test]
    );
    if (exist.length === 0) {
      await pool.query(
        'INSERT INTO Lab_Tests (patient_id, doctor_id, test_name, test_date, cost, result, status) VALUES (?, ?, ?, ?, ?, ?, ?)',
        [lt.pid, lt.did, lt.test, lt.date, lt.cost, lt.res, lt.status]
      );
    }
  }
  console.log('Lab tests synced.');

  // 7. Feedback
  console.log('7. Populating Feedback...');
  const sampleFeedback = [
    { pid: pids[0] || 1, rating: 5, date: yesterdayStr, comments: 'Extremely polite nursing staff and the doctor explained every step clearly.' },
    { pid: pids[1] || 3, rating: 5, date: yesterdayStr, comments: 'Fast triage at the emergency desk. Very grateful for the prompt care.' },
    { pid: pids[2] || 4, rating: 4, date: pastDayStr, comments: 'Clean facilities and smooth digital prescription process.' },
    { pid: pids[3] || 5, rating: 5, date: pastDayStr, comments: 'Dr. Pooja Mehta took time to answer all questions. Highly recommended.' },
    { pid: pids[4] || 6, rating: 4, date: pastDayStr, comments: 'Minimal waiting time and hassle-free billing checkout.' }
  ];

  for (const fb of sampleFeedback) {
    const [exist] = await pool.query(
      'SELECT feedback_id FROM Feedback WHERE patient_id = ? AND comments = ?',
      [fb.pid, fb.comments]
    );
    if (exist.length === 0) {
      await pool.query(
        'INSERT INTO Feedback (patient_id, rating, feedback_date, comments) VALUES (?, ?, ?, ?)',
        [fb.pid, fb.rating, fb.date, fb.comments]
      );
    }
  }
  console.log('Feedback seeded successfully.');

  console.log('--- Database Seeding Complete! ---');
  process.exit(0);
}

seed().catch(err => {
  console.error('Seeding error:', err);
  process.exit(1);
});
