import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Patients from './pages/Patients';
import Doctors from './pages/Doctors';
import DoctorSchedule from './pages/DoctorSchedule';
import Appointments from './pages/Appointments';
import Prescriptions from './pages/Prescriptions';
import Billing from './pages/Billing';
import Emergency from './pages/Emergency';
import LabTests from './pages/LabTests';
import BedAllocation from './pages/BedAllocation';
import Feedback from './pages/Feedback';
import Reports from './pages/Reports';
import AuditLogs from './pages/AuditLogs';
import Settings from './pages/Settings';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login />} />
        <Route path="/app/dashboard" element={<Dashboard />} />
        <Route path="/app/patients" element={<Patients />} />
        <Route path="/app/doctors" element={<Doctors />} />
        <Route path="/app/schedule" element={<DoctorSchedule />} />
        <Route path="/app/appointments" element={<Appointments />} />
        <Route path="/app/prescriptions" element={<Prescriptions />} />
        <Route path="/app/billing" element={<Billing />} />
        <Route path="/app/emergency" element={<Emergency />} />
        <Route path="/app/lab-tests" element={<LabTests />} />
        <Route path="/app/beds" element={<BedAllocation />} />
        <Route path="/app/feedback" element={<Feedback />} />
        <Route path="/app/reports" element={<Reports />} />
        <Route path="/app/audit" element={<AuditLogs />} />
        <Route path="/app/settings" element={<Settings />} />
        <Route path="/app" element={<Navigate to="/app/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
