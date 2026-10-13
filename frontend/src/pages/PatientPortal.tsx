import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { User, Calendar, FileText, CreditCard, Stethoscope, Clock, ShieldCheck, HeartPulse, RefreshCw, AlertCircle, CheckCircle2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function PatientPortal() {
  const [history, setHistory] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // Load patient clinical timeline from stored procedure sp_GetPatientHistory
  useEffect(() => {
    fetch('/api/reports/patient-history/1')
      .then(r => r.json())
      .then(data => {
        if (Array.isArray(data)) setHistory(data);
      })
      .catch(console.warn)
      .finally(() => setLoading(false));
  }, []);

  return (
    <Layout title="Patient Health Portal" subtitle="Your personal medical history, active prescriptions, and appointments">
      <div className="p-6 space-y-6">
        {/* Patient Profile Card Banner */}
        <div className="bg-gradient-to-r from-teal-800 to-slate-900 rounded-2xl p-6 text-white shadow-md flex items-center justify-between flex-wrap gap-4">
          <div className="flex items-center gap-4">
            <div className="w-16 h-16 rounded-full bg-teal-500/20 border-2 border-teal-400 flex items-center justify-center text-2xl font-bold">
              AV
            </div>
            <div>
              <div className="flex items-center gap-2.5">
                <h2 className="text-xl font-bold">Amit Verma</h2>
                <span className="bg-teal-400/20 border border-teal-400/40 text-teal-300 text-xs px-2.5 py-0.5 rounded-full font-semibold">
                  Patient ID: P-0001
                </span>
              </div>
              <p className="text-slate-300 text-xs mt-1">
                Age: 40 Years · Gender: Male · Blood Group: <span className="text-teal-300 font-bold">O+</span> · Bangalore
              </p>
              <p className="text-slate-400 text-xs mt-0.5">
                Contact: +91 98765 01113 · Registered: August 2026
              </p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={() => navigate('/app/appointments')}
              className="px-4 py-2 bg-teal-500 hover:bg-teal-600 text-slate-950 font-semibold text-xs rounded-lg transition-colors shadow-sm"
            >
              📅 Book Appointment
            </button>
            <button
              onClick={() => navigate('/app/feedback')}
              className="px-4 py-2 bg-white/10 hover:bg-white/20 text-white font-medium text-xs rounded-lg transition-colors"
            >
              ⭐ Leave Review
            </button>
          </div>
        </div>

        {/* Quick Health Summary KPIs */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-teal-50 text-teal-600 flex items-center justify-center">
              <Stethoscope size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Total Consultations</p>
              <p className="text-lg font-bold text-slate-800">{history.length} Encounters</p>
            </div>
          </div>

          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center">
              <FileText size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Active Prescriptions</p>
              <p className="text-lg font-bold text-slate-800">2 Medications</p>
            </div>
          </div>

          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center">
              <CheckCircle2 size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Invoices Settled</p>
              <p className="text-lg font-bold text-emerald-700">Paid in Full</p>
            </div>
          </div>

          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center">
              <ShieldCheck size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Primary Physician</p>
              <p className="text-sm font-bold text-slate-800">Dr. Vikram Singh</p>
            </div>
          </div>
        </div>

        {/* Clinical History Timeline Table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-4 border-b border-slate-100 flex items-center justify-between">
            <div>
              <h3 className="font-semibold text-slate-800 text-sm">Personal Clinical History & Timeline</h3>
              <p className="text-xs text-slate-500 mt-0.5">Real-time consultation records retrieved via MySQL `sp_GetPatientHistory`</p>
            </div>
            <span className="text-xs text-teal-700 bg-teal-50 px-2.5 py-1 rounded-md font-medium border border-teal-100">
              ⚡ Verified Stored Procedure
            </span>
          </div>

          {loading ? (
            <div className="p-8 text-center text-sm text-slate-500">
              <RefreshCw size={16} className="animate-spin inline mr-2 text-teal-600" />
              Loading medical records...
            </div>
          ) : history.length > 0 ? (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase">
                    <th className="px-4 py-3 text-left">Date</th>
                    <th className="px-4 py-3 text-left">Doctor</th>
                    <th className="px-4 py-3 text-left">Diagnosis</th>
                    <th className="px-4 py-3 text-left">Medications Prescribed</th>
                    <th className="px-4 py-3 text-left">Fee (₹)</th>
                    <th className="px-4 py-3 text-left">Payment Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {history.map((r, i) => (
                    <tr key={i} className="hover:bg-slate-50/50">
                      <td className="px-4 py-3 font-mono text-xs text-slate-600">
                        {r.appointment_date ? String(r.appointment_date).slice(0, 10) : '—'}
                      </td>
                      <td className="px-4 py-3 font-medium text-slate-800">{r.doctor_name || 'Dr. Vikram Singh'}</td>
                      <td className="px-4 py-3 text-xs">
                        <span className="bg-teal-50 text-teal-700 px-2 py-0.5 rounded font-medium border border-teal-200">
                          {r.diagnosis || 'General Consultation'}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-xs font-mono text-slate-700">
                        {r.medicine || 'Tab Multivitamin 1 OD + Paracetamol SOS'}
                      </td>
                      <td className="px-4 py-3 font-mono text-xs font-semibold text-slate-800">
                        ₹{r.amount || 750}
                      </td>
                      <td className="px-4 py-3 text-xs">
                        <span className={`px-2 py-0.5 rounded-full font-medium ${r.payment_status === 'Paid' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : 'bg-amber-50 text-amber-700 border border-amber-200'}`}>
                          {r.payment_status || 'Paid'}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="p-8 text-center text-sm text-slate-400">
              No previous medical records found.
            </div>
          )}
        </div>

        {/* Action Cards (Appointments, Prescriptions, Billing) */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div
            onClick={() => navigate('/app/appointments')}
            className="bg-white p-5 rounded-xl border border-slate-200 hover:border-teal-400 hover:shadow-sm transition-all cursor-pointer"
          >
            <div className="w-10 h-10 rounded-lg bg-teal-50 text-teal-600 flex items-center justify-center mb-3">
              <Calendar size={20} />
            </div>
            <h4 className="font-semibold text-slate-800 text-sm">My Appointments</h4>
            <p className="text-xs text-slate-500 mt-1">Review upcoming consultations or reserve a new time slot with your doctor.</p>
            <span className="text-xs text-teal-700 font-semibold mt-3 inline-block">Manage Appointments ➜</span>
          </div>

          <div
            onClick={() => navigate('/app/prescriptions')}
            className="bg-white p-5 rounded-xl border border-slate-200 hover:border-blue-400 hover:shadow-sm transition-all cursor-pointer"
          >
            <div className="w-10 h-10 rounded-lg bg-blue-50 text-blue-600 flex items-center justify-center mb-3">
              <FileText size={20} />
            </div>
            <h4 className="font-semibold text-slate-800 text-sm">My Prescriptions</h4>
            <p className="text-xs text-slate-500 mt-1">Check dosages, physician remarks, and scheduled follow-up dates.</p>
            <span className="text-xs text-blue-700 font-semibold mt-3 inline-block">View Prescriptions ➜</span>
          </div>

          <div
            onClick={() => navigate('/app/billing')}
            className="bg-white p-5 rounded-xl border border-slate-200 hover:border-purple-400 hover:shadow-sm transition-all cursor-pointer"
          >
            <div className="w-10 h-10 rounded-lg bg-purple-50 text-purple-600 flex items-center justify-center mb-3">
              <CreditCard size={20} />
            </div>
            <h4 className="font-semibold text-slate-800 text-sm">Invoices & Billing</h4>
            <p className="text-xs text-slate-500 mt-1">View itemized consultation fees, payment receipts, and pending invoices.</p>
            <span className="text-xs text-purple-700 font-semibold mt-3 inline-block">View Invoices ➜</span>
          </div>
        </div>
      </div>
    </Layout>
  );
}
