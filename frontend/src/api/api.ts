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

// Emergency
export async function getEmergency() {
  return fetchJSON('/emergency');
}

// Lab Tests
export async function getLabTests() {
  return fetchJSON('/lab-tests');
}

// Prescriptions
export async function getPrescriptions() {
  return fetchJSON('/prescriptions');
}

// Feedback
export async function getFeedback() {
  return fetchJSON('/feedback');
}

// Audit Logs
export async function getAuditLogs() {
  return fetchJSON('/audit-logs');
}
