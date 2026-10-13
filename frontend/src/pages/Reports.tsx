import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Download, RefreshCw, User, Calendar, FileText, CreditCard, Stethoscope, AlertCircle, CheckCircle2 } from 'lucide-react';
import {
  AreaChart, Area, BarChart, Bar, LineChart, Line, PieChart, Pie, Cell,
  XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Legend
} from 'recharts';
import { revenueData, appointments, doctors } from '../data/mockData';
import { getPatients } from '../api/api';

const reports = [
  'Active Appointments',
  'Hospital Revenue Summary',
  'Patient History',
  'Pending Bills',
  'Revenue by Doctor',
  'Monthly Patient Trends',
  'Bed Occupancy History',
  'Doctor Workload Analysis',
];

const monthlyPatients = [
  { month: 'Mar', new: 180, returning: 280 },
  { month: 'Apr', new: 210, returning: 310 },
  { month: 'May', new: 195, returning: 345 },
  { month: 'Jun', new: 220, returning: 290 },
  { month: 'Jul', new: 250, returning: 360 },
  { month: 'Aug', new: 240, returning: 390 },
  { month: 'Sep', new: 165, returning: 240 },
];

const bedOccupancy = [
  { month: 'Mar', ICU: 70, Private: 65, General: 72 },
  { month: 'Apr', ICU: 75, Private: 70, General: 68 },
  { month: 'May', ICU: 80, Private: 75, General: 74 },
  { month: 'Jun', ICU: 72, Private: 68, General: 70 },
  { month: 'Jul', ICU: 85, Private: 80, General: 78 },
  { month: 'Aug', ICU: 88, Private: 78, General: 80 },
  { month: 'Sep', ICU: 75, Private: 70, General: 70 },
];

const statusDist = [
  { name: 'Confirmed', value: appointments.filter(a => a.status === 'Confirmed').length || 8, color: '#16A34A' },
  { name: 'Scheduled', value: appointments.filter(a => a.status === 'Scheduled').length || 6, color: '#0284C7' },
  { name: 'Completed', value: appointments.filter(a => a.status === 'Completed').length || 12, color: '#64748B' },
  { name: 'Cancelled', value: appointments.filter(a => a.status === 'Cancelled').length || 2, color: '#DC2626' },
];

export default function Reports() {
  const [activeReport, setActiveReport] = useState(reports[0]);
  const [patientsList, setPatientsList] = useState<any[]>([]);
  const [selectedPatientId, setSelectedPatientId] = useState<string>('1');
  const [patientHistoryData, setPatientHistoryData] = useState<any[]>([]);
  const [pendingBillsData, setPendingBillsData] = useState<any[]>([]);
  const [doctorWorkloadData, setDoctorWorkloadData] = useState<any[]>([]);
  const [revenueByDocData, setRevenueByDocData] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);

  // Load patients and report data
  useEffect(() => {
    getPatients().then(data => {
      if (Array.isArray(data) && data.length > 0) {
        setPatientsList(data);
        setSelectedPatientId(String(data[0].rawId || 1));
      }
    }).catch(console.warn);

    // Pre-fetch pending bills & doctor workload
    fetch('/api/reports/pending-bills').then(r => r.json()).then(setPendingBillsData).catch(console.warn);
    fetch('/api/reports/doctor-workload').then(r => r.json()).then(setDoctorWorkloadData).catch(console.warn);
    fetch('/api/reports/revenue-by-doctor').then(r => r.json()).then(setRevenueByDocData).catch(console.warn);
  }, []);

  // Fetch Patient History when patient is selected or report changes
  useEffect(() => {
    if (activeReport === 'Patient History' && selectedPatientId) {
      setLoading(true);
      fetch(`/api/reports/patient-history/${selectedPatientId}`)
        .then(r => r.json())
        .then(data => {
          if (Array.isArray(data)) setPatientHistoryData(data);
        })
        .catch(console.warn)
        .finally(() => setLoading(false));
    }
  }, [activeReport, selectedPatientId]);

  // Export CSV Handler
  const handleExportCSV = () => {
    let csvContent = '';
    const dateStr = new Date().toISOString().slice(0, 10);
    const fileName = `${activeReport.replace(/\s+/g, '_')}_${dateStr}.csv`;

    switch (activeReport) {
      case 'Patient History': {
        const p = patientsList.find(pt => String(pt.rawId) === String(selectedPatientId));
        csvContent = `Patient ID,Patient Name,Age,Blood Group,Appointment Date,Doctor,Diagnosis,Medicine,Bill Amount,Payment Status\n`;
        if (patientHistoryData.length > 0) {
          patientHistoryData.forEach(r => {
            const date = r.appointment_date ? String(r.appointment_date).slice(0, 10) : '';
            csvContent += `"${r.patient_id}","${r.patient_name}","${r.age}","${r.blood_group}","${date}","${r.doctor_name || ''}","${r.diagnosis || ''}","${r.medicine || ''}","${r.amount || 0}","${r.payment_status || ''}"\n`;
          });
        } else {
          csvContent += `"${selectedPatientId}","${p?.name || 'Unknown'}","${p?.age || ''}","${p?.blood || ''}","No History Records","","","","",""\n`;
        }
        break;
      }
      case 'Pending Bills': {
        csvContent = `Bill ID,Patient Name,Contact,Doctor,Amount,Bill Date,Room,Payment Status\n`;
        pendingBillsData.forEach(b => {
          const bdate = b.bill_date ? String(b.bill_date).slice(0, 10) : '';
          csvContent += `"${b.bill_id}","${b.patient_name}","${b.contact || ''}","${b.doctor_name}","${b.amount}","${bdate}","${b.room_number || ''}","${b.payment_status}"\n`;
        });
        break;
      }
      case 'Doctor Workload Analysis': {
        csvContent = `Doctor ID,Doctor Name,Specialization,Total Appointments,Unique Patients,Emergency Cases\n`;
        doctorWorkloadData.forEach(d => {
          csvContent += `"${d.doctor_id}","${d.doctor_name}","${d.specialization_name || 'General'}","${d.total_appointments}","${d.unique_patients}","${d.emergency_cases}"\n`;
        });
        break;
      }
      case 'Revenue by Doctor': {
        csvContent = `Doctor ID,Doctor Name,Specialization,Total Bills,Total Revenue (Rs.)\n`;
        (revenueByDocData.length > 0 ? revenueByDocData : doctors).forEach((d: any) => {
          csvContent += `"${d.doctor_id || d.id}","${d.doctor_name || d.name}","${d.specialization_name || d.specialization || ''}","${d.total_bills || 0}","${d.total_revenue || d.revenue || 0}"\n`;
        });
        break;
      }
      case 'Monthly Patient Trends': {
        csvContent = `Month,New Patients,Returning Patients,Total Patients\n`;
        monthlyPatients.forEach(m => {
          csvContent += `"${m.month}","${m.new}","${m.returning}","${m.new + m.returning}"\n`;
        });
        break;
      }
      case 'Bed Occupancy History': {
        csvContent = `Month,ICU Occupancy (%),Private Ward (%),General Ward (%)\n`;
        bedOccupancy.forEach(b => {
          csvContent += `"${b.month}","${b.ICU}%","${b.Private}%","${b.General}%"\n`;
        });
        break;
      }
      case 'Hospital Revenue Summary': {
        csvContent = `Month,Revenue (Rs.)\n`;
        revenueData.forEach(r => {
          csvContent += `"${r.month}","${r.revenue}"\n`;
        });
        break;
      }
      default: {
        csvContent = `Appointment ID,Patient,Doctor,Date,Time,Status\n`;
        appointments.forEach(a => {
          csvContent += `"${a.id}","${a.patient}","${a.doctor}","${a.date}","${a.time}","${a.status}"\n`;
        });
        break;
      }
    }

    // Trigger download
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  };

  const renderReport = () => {
    switch (activeReport) {
      case 'Active Appointments':
        return (
          <div className="space-y-5">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
              <div className="bg-white rounded-xl border border-slate-200 p-5 shadow-xs">
                <h4 className="text-sm font-semibold text-slate-700 mb-4">Appointment Status Distribution</h4>
                <ResponsiveContainer width="100%" height={220}>
                  <PieChart>
                    <Pie data={statusDist} cx="50%" cy="50%" innerRadius={55} outerRadius={90} dataKey="value" paddingAngle={3}>
                      {statusDist.map((e, i) => <Cell key={i} fill={e.color} />)}
                    </Pie>
                    <Tooltip formatter={(v: any, n: any) => [v, n]} contentStyle={{ fontSize: 12, borderRadius: 8 }} />
                    <Legend iconType="circle" iconSize={10} />
                  </PieChart>
                </ResponsiveContainer>
              </div>
              <div className="bg-white rounded-xl border border-slate-200 p-5 shadow-xs">
                <h4 className="text-sm font-semibold text-slate-700 mb-3">Live Active OPD Queue</h4>
                <div className="space-y-2 max-h-[220px] overflow-y-auto">
                  {appointments.slice(0, 7).map(a => (
                    <div key={a.id} className="flex items-center justify-between text-sm py-1.5 border-b border-slate-50 last:border-0">
                      <div>
                        <span className="font-medium text-slate-800">{a.patient}</span>
                        <span className="text-xs text-slate-500 block">with {a.doctor}</span>
                      </div>
                      <div className="text-right">
                        <span className="font-mono text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded">{a.time}</span>
                        <span className="text-[11px] text-slate-400 block mt-0.5">{a.status}</span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        );

      case 'Patient History':
        return (
          <div className="space-y-4">
            <div className="bg-white rounded-xl border border-slate-200 p-4 shadow-xs flex items-center justify-between flex-wrap gap-3">
              <div className="flex items-center gap-2">
                <User size={16} className="text-teal-600" />
                <label className="text-xs font-semibold text-slate-600 uppercase tracking-wide">Select Patient:</label>
                <select
                  value={selectedPatientId}
                  onChange={e => setSelectedPatientId(e.target.value)}
                  className="pl-3 pr-8 py-1.5 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20 text-slate-800 font-medium"
                >
                  {patientsList.map(p => (
                    <option key={p.id} value={p.rawId || p.id}>
                      {p.name} ({p.id}) - {p.blood}
                    </option>
                  ))}
                </select>
              </div>
              <span className="text-xs text-teal-700 bg-teal-50 px-2.5 py-1 rounded-md font-medium border border-teal-100">
                ⚡ Executing `CALL sp_GetPatientHistory({selectedPatientId})`
              </span>
            </div>

            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
              <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
                <span className="text-sm font-semibold text-slate-700">Clinical Timeline & Encounters ({patientHistoryData.length})</span>
                {loading && <RefreshCw size={14} className="animate-spin text-teal-600" />}
              </div>

              {patientHistoryData.length > 0 ? (
                <div className="overflow-x-auto">
                  <table className="w-full text-sm">
                    <thead>
                      <tr className="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase">
                        <th className="px-4 py-3 text-left">Date</th>
                        <th className="px-4 py-3 text-left">Consulting Doctor</th>
                        <th className="px-4 py-3 text-left">Clinical Diagnosis</th>
                        <th className="px-4 py-3 text-left">Prescribed Medication</th>
                        <th className="px-4 py-3 text-left">Fee (₹)</th>
                        <th className="px-4 py-3 text-left">Billing Status</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                      {patientHistoryData.map((row, idx) => (
                        <tr key={idx} className="hover:bg-slate-50/50">
                          <td className="px-4 py-3 font-mono text-xs text-slate-600">
                            {row.appointment_date ? String(row.appointment_date).slice(0, 10) : '—'}
                          </td>
                          <td className="px-4 py-3 font-medium text-slate-800">{row.doctor_name || 'General OPD'}</td>
                          <td className="px-4 py-3 text-xs text-slate-700 font-medium">
                            <span className="bg-slate-100 px-2 py-0.5 rounded text-slate-800">{row.diagnosis || 'General Consultation'}</span>
                          </td>
                          <td className="px-4 py-3 text-xs text-slate-600 font-mono">{row.medicine || 'Advised rest & hydration'}</td>
                          <td className="px-4 py-3 font-mono text-xs text-slate-800 font-semibold">₹{row.amount || 500}</td>
                          <td className="px-4 py-3 text-xs">
                            <span className={`px-2 py-0.5 rounded-full font-medium ${row.payment_status === 'Paid' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : 'bg-amber-50 text-amber-700 border border-amber-200'}`}>
                              {row.payment_status || 'Paid'}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="p-8 text-center text-sm text-slate-400">
                  No previous consultation records found for this patient.
                </div>
              )}
            </div>
          </div>
        );

      case 'Pending Bills':
        return (
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
            <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
              <span className="text-sm font-semibold text-slate-700">Unsettled Patient Invoices ({pendingBillsData.length})</span>
              <span className="text-xs text-amber-700 bg-amber-50 px-2.5 py-0.5 rounded font-medium border border-amber-100">
                Action Required
              </span>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase">
                    <th className="px-4 py-3 text-left">Bill ID</th>
                    <th className="px-4 py-3 text-left">Patient Name</th>
                    <th className="px-4 py-3 text-left">Contact</th>
                    <th className="px-4 py-3 text-left">Doctor</th>
                    <th className="px-4 py-3 text-left">Date</th>
                    <th className="px-4 py-3 text-left">Amount (₹)</th>
                    <th className="px-4 py-3 text-left">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {pendingBillsData.map(b => (
                    <tr key={b.bill_id} className="hover:bg-slate-50/50">
                      <td className="px-4 py-3 font-mono text-xs text-slate-500">#{b.bill_id}</td>
                      <td className="px-4 py-3 font-medium text-slate-800">{b.patient_name}</td>
                      <td className="px-4 py-3 font-mono text-xs text-slate-600">{b.contact || 'N/A'}</td>
                      <td className="px-4 py-3 text-xs text-slate-700">{b.doctor_name}</td>
                      <td className="px-4 py-3 font-mono text-xs text-slate-500">{String(b.bill_date).slice(0, 10)}</td>
                      <td className="px-4 py-3 font-mono text-xs font-bold text-slate-900">₹{b.amount}</td>
                      <td className="px-4 py-3 text-xs">
                        <span className="bg-amber-50 text-amber-700 border border-amber-200 px-2 py-0.5 rounded-full font-medium">
                          Pending
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        );

      case 'Doctor Workload Analysis':
        return (
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
            <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
              <span className="text-sm font-semibold text-slate-700">Physician Clinical Workload Distribution</span>
              <span className="text-xs text-teal-700 bg-teal-50 px-2.5 py-0.5 rounded font-medium border border-teal-100">
                SQL Aggregation & Multi-table LEFT JOIN
              </span>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200 text-xs font-semibold text-slate-500 uppercase">
                    <th className="px-4 py-3 text-left">Doctor</th>
                    <th className="px-4 py-3 text-left">Specialization</th>
                    <th className="px-4 py-3 text-center">Consultations</th>
                    <th className="px-4 py-3 text-center">Unique Patients</th>
                    <th className="px-4 py-3 text-center">ER Cases</th>
                    <th className="px-4 py-3 text-left">Workload Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {doctorWorkloadData.map(d => {
                    const total = d.total_appointments || 0;
                    return (
                      <tr key={d.doctor_id} className="hover:bg-slate-50/50">
                        <td className="px-4 py-3 font-medium text-slate-800">{d.doctor_name}</td>
                        <td className="px-4 py-3 text-xs text-slate-600">
                          <span className="bg-slate-100 px-2 py-0.5 rounded">{d.specialization_name || 'General Medicine'}</span>
                        </td>
                        <td className="px-4 py-3 text-center font-mono text-xs font-bold text-slate-800">{total}</td>
                        <td className="px-4 py-3 text-center font-mono text-xs text-slate-600">{d.unique_patients || 0}</td>
                        <td className="px-4 py-3 text-center font-mono text-xs text-rose-600 font-semibold">{d.emergency_cases || 0}</td>
                        <td className="px-4 py-3 text-xs">
                          <span className={`px-2 py-0.5 rounded-full font-medium ${total > 5 ? 'bg-rose-50 text-rose-700 border border-rose-200' : 'bg-emerald-50 text-emerald-700 border border-emerald-200'}`}>
                            {total > 5 ? 'High Volume' : 'Balanced'}
                          </span>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        );

      case 'Hospital Revenue Summary':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5 shadow-xs">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">Monthly Revenue Trends (₹)</h4>
            <ResponsiveContainer width="100%" height={300}>
              <AreaChart data={revenueData}>
                <defs>
                  <linearGradient id="revGrad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#0F766E" stopOpacity={0.2} />
                    <stop offset="95%" stopColor="#0F766E" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} tickFormatter={v => `₹${v/1000}k`} />
                <Tooltip formatter={(v: any) => [`₹${(v as number).toLocaleString()}`, "Revenue"]} contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Area type="monotone" dataKey="revenue" stroke="#0F766E" strokeWidth={2.5} fill="url(#revGrad)" dot={{ r: 4, fill: '#0F766E' }} />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        );

      case 'Monthly Patient Trends':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5 shadow-xs">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">New vs Returning Patient Admissions</h4>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={monthlyPatients} barSize={20} barGap={4}>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <Tooltip contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Legend iconType="circle" iconSize={10} />
                <Bar dataKey="new" fill="#14B8A6" radius={[3, 3, 0, 0]} name="New Patients" />
                <Bar dataKey="returning" fill="#0F172A" radius={[3, 3, 0, 0]} name="Returning Patients" opacity={0.6} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        );

      case 'Revenue by Doctor':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5 shadow-xs">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">Total Revenue Generated by Physician (₹)</h4>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={revenueByDocData.length > 0 ? revenueByDocData.map(d => ({ name: d.doctor_name, revenue: Number(d.total_revenue) })) : doctors.map(d => ({ name: d.name, revenue: d.fee * 6 }))} layout="vertical" barSize={18}>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" horizontal={false} />
                <XAxis type="number" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} tickFormatter={v => `₹${v}`} />
                <YAxis type="category" dataKey="name" tick={{ fontSize: 11, fill: '#334155' }} axisLine={false} tickLine={false} width={120} />
                <Tooltip formatter={(v: any) => [`₹${(v as number).toLocaleString()}`, "Revenue"]} contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Bar dataKey="revenue" fill="#0F766E" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        );

      case 'Bed Occupancy History':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5 shadow-xs">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">Ward Occupancy Rate % Over Time</h4>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={bedOccupancy}>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} unit="%" />
                <Tooltip contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Legend iconType="circle" iconSize={10} />
                <Line type="monotone" dataKey="ICU" stroke="#DC2626" strokeWidth={2} dot={{ r: 3 }} name="ICU" />
                <Line type="monotone" dataKey="Private" stroke="#0284C7" strokeWidth={2} dot={{ r: 3 }} name="Private Room" />
                <Line type="monotone" dataKey="General" stroke="#0F766E" strokeWidth={2} dot={{ r: 3 }} name="General Ward" />
              </LineChart>
            </ResponsiveContainer>
          </div>
        );

      default:
        return null;
    }
  };

  return (
    <Layout title="Reports & Analytics" subtitle="Comprehensive hospital business intelligence and clinical auditing">
      <div className="p-6">
        <div className="flex flex-col md:flex-row gap-5">
          {/* Sidebar */}
          <div className="w-full md:w-56 shrink-0">
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
              <div className="px-4 py-3 border-b border-slate-100 bg-slate-50">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Available Reports</span>
              </div>
              <nav className="p-1.5 space-y-0.5">
                {reports.map(r => (
                  <button
                    key={r}
                    onClick={() => setActiveReport(r)}
                    className={`w-full text-left px-3 py-2 rounded-lg text-sm transition-colors ${activeReport === r ? 'bg-teal-600/10 text-teal-700 font-semibold' : 'text-slate-600 hover:bg-slate-50'}`}
                  >
                    {r}
                  </button>
                ))}
              </nav>
            </div>
          </div>

          {/* Content */}
          <div className="flex-1 min-w-0 space-y-4">
            <div className="flex items-center justify-between flex-wrap gap-2">
              <h2 className="text-base font-semibold text-slate-800">{activeReport}</h2>
              <button
                onClick={handleExportCSV}
                className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg shadow-sm hover:opacity-95 transition-all"
                style={{ background: '#0F766E' }}
                title={`Export ${activeReport} as CSV`}
              >
                <Download size={14} /> Export CSV
              </button>
            </div>
            {renderReport()}
          </div>
        </div>
      </div>
    </Layout>
  );
}
