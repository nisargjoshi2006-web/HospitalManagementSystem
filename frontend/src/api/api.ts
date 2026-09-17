const API_BASE = '/api';

async function fetchJSON(url: string, options?: RequestInit) {
  const res = await fetch(`${API_BASE}${url}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options?.headers,
    },
    ...options,
  });
  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    throw new Error(errorData.error || errorData.message || `Request failed with status ${res.status}`);
  }
  return res.json();
}

// Authentication
export async function login(username: string, password: string) {
  return fetchJSON('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ username, password }),
  });
}

// Dashboard
export async function getDashboard() {
  return fetchJSON('/dashboard');
}

// Patients
export async function getPatients(search?: string, blood?: string) {
  const params = new URLSearchParams();
  if (search) params.append('search', search);
  if (blood && blood !== 'All') params.append('blood', blood);
  const query = params.toString() ? `?${params.toString()}` : '';
  return fetchJSON(`/patients${query}`);
}

export async function addPatient(patient: {
  name: string;
  age: number | string;
  gender: string;
  blood: string;
  contact: string;
  address: string;
  registered?: string;
}) {
  return fetchJSON('/patients', {
    method: 'POST',
    body: JSON.stringify(patient),
  });
}

export async function deletePatient(id: string) {
  return fetchJSON(`/patients/${id}`, {
    method: 'DELETE',
  });
}

// Doctors
export async function getDoctors() {
  return fetchJSON('/doctors');
}

export async function addDoctor(doctor: {
  name: string;
  specialization?: string;
  specializationId?: number;
  qualification?: string;
  fee: number | string;
  contact: string;
}) {
  return fetchJSON('/doctors', {
    method: 'POST',
    body: JSON.stringify(doctor),
  });
}

export async function deleteDoctor(id: string) {
  return fetchJSON(`/doctors/${id}`, {
    method: 'DELETE',
  });
}

export async function getSpecializations() {
  return fetchJSON('/specializations');
}

// Appointments
export async function getAppointments() {
  return fetchJSON('/appointments');
}

export async function addAppointment(apt: {
  patientId: string | number;
  doctorId: string | number;
  date: string;
  time: string;
  room?: string;
  status?: string;
}) {
  return fetchJSON('/appointments', {
    method: 'POST',
    body: JSON.stringify(apt),
  });
}

export async function deleteAppointment(id: string | number) {
  return fetchJSON(`/appointments/${id}`, {
    method: 'DELETE',
  });
}

// Bed Allocation
export async function getBeds() {
  return fetchJSON('/beds');
}

export async function allocateBed(data: {
  patientId: string | number;
  wardType: string;
  bedNumber: string;
  admitDate?: string;
  dailyCharge?: number;
}) {
  return fetchJSON('/beds/allocate', {
    method: 'POST',
    body: JSON.stringify(data),
  });
}

export async function dischargeBed(allocationId: number, dischargeDate?: string) {
  return fetchJSON('/beds/discharge', {
    method: 'POST',
    body: JSON.stringify({ allocationId, dischargeDate }),
  });
}

// Billing
export async function getBilling() {
  return fetchJSON('/billing');
}

export async function addBill(bill: {
  appointmentId: string | number;
  amount?: number;
  billDate?: string;
  paymentMethod: string;
  paymentStatus: string;
}) {
  return fetchJSON('/billing', {
    method: 'POST',
    body: JSON.stringify(bill),
  });
}

export async function updateBill(id: string | number, update: { paymentMethod?: string; paymentStatus?: string }) {
  return fetchJSON(`/billing/${id}`, {
    method: 'PUT',
    body: JSON.stringify(update),
  });
}

// Emergency
export async function getEmergency() {
  return fetchJSON('/emergency');
}

export async function addEmergency(em: {
  patientId: string | number;
  emergencyType: string;
  priorityLevel: string;
  arrivalDate?: string;
  arrivalTime?: string;
  status?: string;
  assignedDoctor?: string | number | null;
}) {
  return fetchJSON('/emergency', {
    method: 'POST',
    body: JSON.stringify(em),
  });
}

// Lab Tests
export async function getLabTests() {
  return fetchJSON('/lab-tests');
}

export async function addLabTest(test: {
  patientId: string | number;
  doctorId: string | number;
  testName: string;
  testDate?: string;
  cost?: number;
  result?: string;
  status?: string;
}) {
  return fetchJSON('/lab-tests', {
    method: 'POST',
    body: JSON.stringify(test),
  });
}

export async function updateLabTest(id: string | number, data: { result?: string; status?: string }) {
  return fetchJSON(`/lab-tests/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data),
  });
}

// Prescriptions
export async function getPrescriptions() {
  return fetchJSON('/prescriptions');
}

export async function addPrescription(rx: {
  appointmentId: string | number;
  diagnosis: string;
  medicine: string;
  nextVisitDate?: string;
  remarks?: string;
}) {
  return fetchJSON('/prescriptions', {
    method: 'POST',
    body: JSON.stringify(rx),
  });
}

// Doctor Schedule
export async function getSchedules() {
  return fetchJSON('/schedules');
}

export async function addSchedule(sch: {
  doctorId: string | number;
  dayOfWeek: string;
  startTime: string;
  endTime: string;
}) {
  return fetchJSON('/schedules', {
    method: 'POST',
    body: JSON.stringify(sch),
  });
}

export async function deleteSchedule(id: string | number) {
  return fetchJSON(`/schedules/${id}`, {
    method: 'DELETE',
  });
}

// Feedback
export async function getFeedback() {
  return fetchJSON('/feedback');
}

export async function addFeedback(fb: {
  patientId: string | number;
  rating: number;
  feedbackDate?: string;
  comments?: string;
}) {
  return fetchJSON('/feedback', {
    method: 'POST',
    body: JSON.stringify(fb),
  });
}

// Audit Logs
export async function getAuditLogs() {
  return fetchJSON('/audit-logs');
}

