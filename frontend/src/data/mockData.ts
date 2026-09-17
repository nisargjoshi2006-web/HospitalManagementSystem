export const patients = [
  { id: 'P-0001', name: 'Aisha Patel', age: 34, gender: 'Female', blood: 'A+', contact: '+1-555-0101', registered: '2024-01-12', address: '42 Maple Street, Springfield', email: 'aisha.patel@email.com' },
  { id: 'P-0002', name: 'Marcus Chen', age: 58, gender: 'Male', blood: 'O-', contact: '+1-555-0102', registered: '2024-01-15', address: '8 Oak Avenue, Riverside', email: 'm.chen@email.com' },
  { id: 'P-0003', name: 'Elena Rodriguez', age: 27, gender: 'Female', blood: 'B+', contact: '+1-555-0103', registered: '2024-02-03', address: '15 Pine Road, Lakeside', email: 'e.rodriguez@email.com' },
  { id: 'P-0004', name: 'James Whitmore', age: 71, gender: 'Male', blood: 'AB+', contact: '+1-555-0104', registered: '2024-02-18', address: '200 Cedar Blvd, Hilltown', email: 'j.whitmore@email.com' },
  { id: 'P-0005', name: 'Priya Sharma', age: 45, gender: 'Female', blood: 'A-', contact: '+1-555-0105', registered: '2024-03-07', address: '77 Birch Lane, Meadowville', email: 'priya.sharma@email.com' },
  { id: 'P-0006', name: 'David Okonkwo', age: 39, gender: 'Male', blood: 'O+', contact: '+1-555-0106', registered: '2024-03-22', address: '33 Elm Court, Westfield', email: 'd.okonkwo@email.com' },
  { id: 'P-0007', name: 'Sarah Kim', age: 52, gender: 'Female', blood: 'B-', contact: '+1-555-0107', registered: '2024-04-01', address: '91 Willow Way, Eastbrook', email: 'sarah.kim@email.com' },
  { id: 'P-0008', name: 'Thomas Nguyen', age: 63, gender: 'Male', blood: 'A+', contact: '+1-555-0108', registered: '2024-04-14', address: '5 Poplar Drive, Northgate', email: 't.nguyen@email.com' },
  { id: 'P-0009', name: 'Fatima Al-Hassan', age: 31, gender: 'Female', blood: 'O+', contact: '+1-555-0109', registered: '2024-05-02', address: '62 Sycamore St, Bayview', email: 'f.alhassan@email.com' },
  { id: 'P-0010', name: 'Robert Alvarez', age: 48, gender: 'Male', blood: 'AB-', contact: '+1-555-0110', registered: '2024-05-19', address: '11 Magnolia Pkwy, Greenwood', email: 'r.alvarez@email.com' },
  { id: 'P-0011', name: 'Linda Johansson', age: 66, gender: 'Female', blood: 'A+', contact: '+1-555-0111', registered: '2024-06-08', address: '28 Chestnut Ave, Sunnydale', email: 'linda.j@email.com' },
  { id: 'P-0012', name: 'Kevin Park', age: 22, gender: 'Male', blood: 'B+', contact: '+1-555-0112', registered: '2024-06-25', address: '3 Spruce Circle, Fairview', email: 'kpark@email.com' },
];

export const doctors = [
  { id: 'D-001', name: 'Dr. Vivian Torres', specialization: 'Cardiology', fee: 250, available: true, contact: '+1-555-0201', experience: 15, patients: 342 },
  { id: 'D-002', name: 'Dr. Nathan Osei', specialization: 'Neurology', fee: 300, available: true, contact: '+1-555-0202', experience: 20, patients: 287 },
  { id: 'D-003', name: 'Dr. Mei Lin', specialization: 'Orthopedics', fee: 220, available: false, contact: '+1-555-0203', experience: 12, patients: 415 },
  { id: 'D-004', name: 'Dr. Samuel Greene', specialization: 'Pediatrics', fee: 180, available: true, contact: '+1-555-0204', experience: 8, patients: 523 },
  { id: 'D-005', name: 'Dr. Layla Moreau', specialization: 'Dermatology', fee: 200, available: true, contact: '+1-555-0205', experience: 10, patients: 198 },
  { id: 'D-006', name: 'Dr. Arjun Kapoor', specialization: 'Oncology', fee: 350, available: true, contact: '+1-555-0206', experience: 22, patients: 156 },
  { id: 'D-007', name: 'Dr. Ingrid Svensson', specialization: 'Psychiatry', fee: 280, available: false, contact: '+1-555-0207', experience: 16, patients: 234 },
  { id: 'D-008', name: 'Dr. Carlos Mendez', specialization: 'Emergency Medicine', fee: 320, available: true, contact: '+1-555-0208', experience: 18, patients: 612 },
];

export const appointments = [
  { id: 'APT-001', patient: 'Aisha Patel', patientId: 'P-0001', doctor: 'Dr. Vivian Torres', specialization: 'Cardiology', date: '2026-09-17', time: '09:00', room: 'C-12', status: 'Confirmed' },
  { id: 'APT-002', patient: 'Marcus Chen', patientId: 'P-0002', doctor: 'Dr. Nathan Osei', specialization: 'Neurology', date: '2026-09-17', time: '09:30', room: 'N-04', status: 'Scheduled' },
  { id: 'APT-003', patient: 'Elena Rodriguez', patientId: 'P-0003', doctor: 'Dr. Samuel Greene', specialization: 'Pediatrics', date: '2026-09-17', time: '10:00', room: 'P-08', status: 'Confirmed' },
  { id: 'APT-004', patient: 'James Whitmore', patientId: 'P-0004', doctor: 'Dr. Vivian Torres', specialization: 'Cardiology', date: '2026-09-17', time: '10:30', room: 'C-12', status: 'Completed' },
  { id: 'APT-005', patient: 'Priya Sharma', patientId: 'P-0005', doctor: 'Dr. Arjun Kapoor', specialization: 'Oncology', date: '2026-09-17', time: '11:00', room: 'O-02', status: 'Scheduled' },
  { id: 'APT-006', patient: 'David Okonkwo', patientId: 'P-0006', doctor: 'Dr. Mei Lin', specialization: 'Orthopedics', date: '2026-09-17', time: '11:30', room: 'OR-05', status: 'Cancelled' },
  { id: 'APT-007', patient: 'Sarah Kim', patientId: 'P-0007', doctor: 'Dr. Layla Moreau', specialization: 'Dermatology', date: '2026-09-17', time: '14:00', room: 'D-03', status: 'Confirmed' },
  { id: 'APT-008', patient: 'Thomas Nguyen', patientId: 'P-0008', doctor: 'Dr. Nathan Osei', specialization: 'Neurology', date: '2026-09-17', time: '14:30', room: 'N-04', status: 'Scheduled' },
  { id: 'APT-009', patient: 'Fatima Al-Hassan', patientId: 'P-0009', doctor: 'Dr. Ingrid Svensson', specialization: 'Psychiatry', date: '2026-09-18', time: '09:00', room: 'PS-01', status: 'Scheduled' },
  { id: 'APT-010', patient: 'Robert Alvarez', patientId: 'P-0010', doctor: 'Dr. Carlos Mendez', specialization: 'Emergency Medicine', date: '2026-09-18', time: '10:00', room: 'EM-01', status: 'Confirmed' },
];

export const emergencyCases = [
  { id: 'EM-001', patient: 'Unknown Male', priority: 'Critical', doctor: 'Dr. Carlos Mendez', arrival: '08:14', status: 'In Treatment', symptoms: 'Severe chest pain, difficulty breathing' },
  { id: 'EM-002', patient: 'Linda Johansson', priority: 'High', doctor: 'Dr. Vivian Torres', arrival: '08:45', status: 'Stabilizing', symptoms: 'Palpitations, dizziness' },
  { id: 'EM-003', patient: 'Kevin Park', priority: 'High', doctor: 'Dr. Nathan Osei', arrival: '09:02', status: 'Awaiting Scan', symptoms: 'Head trauma after accident' },
  { id: 'EM-004', patient: 'Aisha Patel', priority: 'Medium', doctor: 'Dr. Samuel Greene', arrival: '09:30', status: 'Observation', symptoms: 'High fever, vomiting' },
  { id: 'EM-005', patient: 'Robert Alvarez', priority: 'Low', doctor: 'Dr. Layla Moreau', arrival: '10:05', status: 'Awaiting Discharge', symptoms: 'Minor laceration, left hand' },
];

export const labTests = [
  { id: 'LAB-001', patient: 'Marcus Chen', type: 'MRI', doctor: 'Dr. Nathan Osei', ordered: '2026-09-15', result: 'Pending', cost: 1200, status: 'Pending' },
  { id: 'LAB-002', patient: 'Aisha Patel', type: 'CBC', doctor: 'Dr. Vivian Torres', ordered: '2026-09-15', result: 'Normal', cost: 85, status: 'Completed' },
  { id: 'LAB-003', patient: 'James Whitmore', type: 'ECG', doctor: 'Dr. Vivian Torres', ordered: '2026-09-16', result: 'Abnormal', cost: 150, status: 'Completed' },
  { id: 'LAB-004', patient: 'Elena Rodriguez', type: 'Urinalysis', doctor: 'Dr. Samuel Greene', ordered: '2026-09-16', result: 'Pending', cost: 60, status: 'Pending' },
  { id: 'LAB-005', patient: 'Priya Sharma', type: 'Biopsy', doctor: 'Dr. Arjun Kapoor', ordered: '2026-09-17', result: 'Pending', cost: 450, status: 'In Progress' },
  { id: 'LAB-006', patient: 'Kevin Park', type: 'CT Scan', doctor: 'Dr. Nathan Osei', ordered: '2026-09-17', result: 'Pending', cost: 900, status: 'In Progress' },
  { id: 'LAB-007', patient: 'David Okonkwo', type: 'X-Ray', doctor: 'Dr. Mei Lin', ordered: '2026-09-17', result: 'Pending', cost: 180, status: 'Pending' },
  { id: 'LAB-008', patient: 'Sarah Kim', type: 'Blood Culture', doctor: 'Dr. Layla Moreau', ordered: '2026-09-14', result: 'No Growth', cost: 120, status: 'Completed' },
];

export const beds = {
  ICU: Array.from({ length: 12 }, (_, i) => ({
    id: `ICU-${String(i + 1).padStart(2, '0')}`,
    ward: 'ICU',
    status: i < 9 ? 'Occupied' : i === 9 ? 'Reserved' : 'Available',
    patient: i < 9 ? patients[i]?.name ?? null : null,
    admitted: i < 9 ? '2026-09-14' : null,
  })),
  Private: Array.from({ length: 20 }, (_, i) => ({
    id: `PVT-${String(i + 1).padStart(2, '0')}`,
    ward: 'Private',
    status: i < 14 ? 'Occupied' : i === 14 ? 'Reserved' : 'Available',
    patient: i < 14 ? patients[i % 12]?.name ?? null : null,
    admitted: i < 14 ? '2026-09-10' : null,
  })),
  General: Array.from({ length: 40 }, (_, i) => ({
    id: `GEN-${String(i + 1).padStart(2, '0')}`,
    ward: 'General',
    status: i < 28 ? 'Occupied' : i === 28 || i === 29 ? 'Reserved' : 'Available',
    patient: i < 28 ? patients[i % 12]?.name ?? null : null,
    admitted: i < 28 ? '2026-09-12' : null,
  })),
};

export const prescriptions = [
  { id: 'RX-001', patient: 'Aisha Patel', doctor: 'Dr. Vivian Torres', diagnosis: 'Hypertension', medicine: 'Lisinopril 10mg, Amlodipine 5mg', nextVisit: '2026-10-17', remarks: 'Monitor BP daily', date: '2026-09-10' },
  { id: 'RX-002', patient: 'Marcus Chen', doctor: 'Dr. Nathan Osei', diagnosis: 'Migraine', medicine: 'Sumatriptan 50mg, Metoprolol 25mg', nextVisit: '2026-10-05', remarks: 'Avoid bright lights', date: '2026-09-12' },
  { id: 'RX-003', patient: 'James Whitmore', doctor: 'Dr. Vivian Torres', diagnosis: 'Atrial Fibrillation', medicine: 'Warfarin 5mg, Metoprolol 100mg', nextVisit: '2026-09-24', remarks: 'INR check weekly', date: '2026-09-14' },
  { id: 'RX-004', patient: 'Elena Rodriguez', doctor: 'Dr. Samuel Greene', diagnosis: 'UTI', medicine: 'Ciprofloxacin 500mg, Phenazopyridine 200mg', nextVisit: '2026-09-24', remarks: 'Drink plenty of water', date: '2026-09-15' },
  { id: 'RX-005', patient: 'Priya Sharma', doctor: 'Dr. Arjun Kapoor', diagnosis: 'Chemotherapy support', medicine: 'Ondansetron 8mg, Dexamethasone 4mg', nextVisit: '2026-09-30', remarks: 'Report nausea immediately', date: '2026-09-16' },
];

export const bills = [
  { id: 'BILL-001', patient: 'Aisha Patel', doctor: 'Dr. Vivian Torres', appointment: 'APT-001', fee: 250, method: 'Card', status: 'Paid', date: '2026-09-10' },
  { id: 'BILL-002', patient: 'Marcus Chen', doctor: 'Dr. Nathan Osei', appointment: 'APT-002', fee: 300, method: 'UPI', status: 'Paid', date: '2026-09-11' },
  { id: 'BILL-003', patient: 'James Whitmore', doctor: 'Dr. Vivian Torres', appointment: 'APT-004', fee: 250, method: 'Cash', status: 'Pending', date: '2026-09-14' },
  { id: 'BILL-004', patient: 'Elena Rodriguez', doctor: 'Dr. Samuel Greene', appointment: 'APT-003', fee: 180, method: 'Online', status: 'Paid', date: '2026-09-15' },
  { id: 'BILL-005', patient: 'Priya Sharma', doctor: 'Dr. Arjun Kapoor', appointment: 'APT-005', fee: 350, method: 'Card', status: 'Pending', date: '2026-09-16' },
  { id: 'BILL-006', patient: 'David Okonkwo', doctor: 'Dr. Mei Lin', appointment: 'APT-006', fee: 220, method: 'Cash', status: 'Failed', date: '2026-09-16' },
  { id: 'BILL-007', patient: 'Sarah Kim', doctor: 'Dr. Layla Moreau', appointment: 'APT-007', fee: 200, method: 'UPI', status: 'Paid', date: '2026-09-17' },
];

export const feedback = [
  { id: 'FB-001', patient: 'Aisha Patel', date: '2026-09-10', rating: 5, comment: 'Excellent care from Dr. Torres. Very professional and thorough.' },
  { id: 'FB-002', patient: 'Marcus Chen', date: '2026-09-12', rating: 4, comment: 'Good experience overall. Wait times were a bit long.' },
  { id: 'FB-003', patient: 'Elena Rodriguez', date: '2026-09-15', rating: 5, comment: 'Dr. Greene was amazing with my daughter. Very patient and kind.' },
  { id: 'FB-004', patient: 'James Whitmore', date: '2026-09-14', rating: 3, comment: 'Facilities are good but the billing process needs improvement.' },
  { id: 'FB-005', patient: 'Priya Sharma', date: '2026-09-16', rating: 5, comment: 'Dr. Kapoor explained everything clearly. Very reassuring.' },
  { id: 'FB-006', patient: 'David Okonkwo', date: '2026-09-13', rating: 4, comment: 'Clean and modern facilities. Staff was helpful and courteous.' },
  { id: 'FB-007', patient: 'Sarah Kim', date: '2026-09-17', rating: 2, comment: 'Long wait despite having an appointment. Communication could improve.' },
];

export const auditLogs = [
  { id: 'LOG-001', timestamp: '2026-09-17 09:15:32', user: 'admin', action: 'CREATE', module: 'Patients', recordId: 'P-0012', description: 'New patient registered: Kevin Park', severity: 'Info' },
  { id: 'LOG-002', timestamp: '2026-09-17 09:02:14', user: 'receptionist', action: 'UPDATE', module: 'Appointments', recordId: 'APT-001', description: 'Appointment status changed to Confirmed', severity: 'Info' },
  { id: 'LOG-003', timestamp: '2026-09-17 08:58:01', user: 'admin', action: 'DELETE', module: 'Billing', recordId: 'BILL-008', description: 'Bill deleted for patient Thomas Nguyen', severity: 'Warning' },
  { id: 'LOG-004', timestamp: '2026-09-17 08:45:00', user: 'system', action: 'ALERT', module: 'Emergency', recordId: 'EM-001', description: 'Critical emergency case admitted', severity: 'Critical' },
  { id: 'LOG-005', timestamp: '2026-09-17 08:30:19', user: 'admin', action: 'LOGIN', module: 'Auth', recordId: '-', description: 'Admin user logged in successfully', severity: 'Info' },
  { id: 'LOG-006', timestamp: '2026-09-17 08:12:44', user: 'receptionist', action: 'CREATE', module: 'Prescriptions', recordId: 'RX-005', description: 'Prescription created for Priya Sharma', severity: 'Info' },
  { id: 'LOG-007', timestamp: '2026-09-16 17:45:30', user: 'admin', action: 'UPDATE', module: 'Doctors', recordId: 'D-003', description: 'Doctor availability updated: Dr. Mei Lin set to unavailable', severity: 'Warning' },
  { id: 'LOG-008', timestamp: '2026-09-16 16:20:11', user: 'system', action: 'BACKUP', module: 'System', recordId: '-', description: 'Automated database backup completed successfully', severity: 'Info' },
];

export const revenueData = [
  { month: 'Mar', revenue: 42000, appointments: 312 },
  { month: 'Apr', revenue: 48500, appointments: 356 },
  { month: 'May', revenue: 51200, appointments: 389 },
  { month: 'Jun', revenue: 46800, appointments: 341 },
  { month: 'Jul', revenue: 55400, appointments: 412 },
  { month: 'Aug', revenue: 59100, appointments: 445 },
  { month: 'Sep', revenue: 38200, appointments: 287 },
];

export const weeklyRevenue = [
  { day: 'Mon', revenue: 6800 },
  { day: 'Tue', revenue: 7200 },
  { day: 'Wed', revenue: 5900 },
  { day: 'Thu', revenue: 8100 },
  { day: 'Fri', revenue: 7600 },
  { day: 'Sat', revenue: 4200 },
  { day: 'Sun', revenue: 2400 },
];
