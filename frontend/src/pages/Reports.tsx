import { useState } from 'react';
import Layout from '../components/Layout';
import { Download } from 'lucide-react';
import {
  AreaChart, Area, BarChart, Bar, LineChart, Line, PieChart, Pie, Cell,
  XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Legend
} from 'recharts';
import { revenueData, appointments, doctors } from '../data/mockData';

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

const revenueByDoctor = doctors.slice(0, 6).map(d => ({
  name: d.name.replace('Dr. ', '').split(' ')[0],
  revenue: Math.round(d.patients * d.fee * 0.6),
}));

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
  { name: 'Confirmed', value: appointments.filter(a => a.status === 'Confirmed').length, color: '#16A34A' },
  { name: 'Scheduled', value: appointments.filter(a => a.status === 'Scheduled').length, color: '#0284C7' },
  { name: 'Completed', value: appointments.filter(a => a.status === 'Completed').length, color: '#64748B' },
  { name: 'Cancelled', value: appointments.filter(a => a.status === 'Cancelled').length, color: '#DC2626' },
];

export default function Reports() {
  const [activeReport, setActiveReport] = useState(reports[0]);

  const renderReport = () => {
    switch (activeReport) {
      case 'Active Appointments':
        return (
          <div className="space-y-5">
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-white rounded-xl border border-slate-200 p-5">
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
              <div className="bg-white rounded-xl border border-slate-200 p-5">
                <h4 className="text-sm font-semibold text-slate-700 mb-3">Today's Schedule</h4>
                <div className="space-y-2">
                  {appointments.filter(a => a.date === '2026-09-17').map(a => (
                    <div key={a.id} className="flex items-center justify-between text-sm py-1 border-b border-slate-50">
                      <span className="text-slate-700">{a.patient}</span>
                      <span className="font-mono text-xs text-slate-500">{a.time}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        );

      case 'Hospital Revenue Summary':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">Monthly Revenue (Mar–Sep 2026)</h4>
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
                <YAxis tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} tickFormatter={v => `$${v/1000}k`} />
                <Tooltip formatter={(v: any) => [`$${(v as number).toLocaleString()}`, "Revenue"]} contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Area type="monotone" dataKey="revenue" stroke="#0F766E" strokeWidth={2.5} fill="url(#revGrad)" dot={{ r: 4, fill: '#0F766E' }} />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        );

      case 'Monthly Patient Trends':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">New vs Returning Patients</h4>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={monthlyPatients} barSize={20} barGap={4}>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <Tooltip contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Legend iconType="circle" iconSize={10} />
                <Bar dataKey="new" fill="#14B8A6" radius={[3, 3, 0, 0]} name="New" />
                <Bar dataKey="returning" fill="#0F172A" radius={[3, 3, 0, 0]} name="Returning" opacity={0.6} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        );

      case 'Revenue by Doctor':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">Revenue by Physician (YTD)</h4>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={revenueByDoctor} layout="vertical" barSize={18}>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" horizontal={false} />
                <XAxis type="number" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} tickFormatter={v => `$${v/1000}k`} />
                <YAxis type="category" dataKey="name" tick={{ fontSize: 12, fill: '#334155' }} axisLine={false} tickLine={false} width={70} />
                <Tooltip formatter={(v: any) => [`$${(v as number).toLocaleString()}`, "Revenue"]} contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Bar dataKey="revenue" fill="#0F766E" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        );

      case 'Bed Occupancy History':
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-5">
            <h4 className="text-sm font-semibold text-slate-700 mb-4">Ward Occupancy % Over Time</h4>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={bedOccupancy}>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: '#94A3B8' }} axisLine={false} tickLine={false} unit="%" />
                <Tooltip contentStyle={{ borderRadius: 8, fontSize: 12 }} />
                <Legend iconType="circle" iconSize={10} />
                <Line type="monotone" dataKey="ICU" stroke="#DC2626" strokeWidth={2} dot={{ r: 3 }} />
                <Line type="monotone" dataKey="Private" stroke="#0284C7" strokeWidth={2} dot={{ r: 3 }} />
                <Line type="monotone" dataKey="General" stroke="#0F766E" strokeWidth={2} dot={{ r: 3 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        );

      default:
        return (
          <div className="bg-white rounded-xl border border-slate-200 p-8 flex flex-col items-center justify-center text-center">
            <div className="w-12 h-12 rounded-full bg-slate-100 flex items-center justify-center mb-3">
              <Download size={20} className="text-slate-400" />
            </div>
            <h4 className="font-medium text-slate-700 mb-1">{activeReport}</h4>
            <p className="text-sm text-slate-500">Report data will appear here</p>
          </div>
        );
    }
  };

  return (
    <Layout title="Reports" subtitle="Analytics and operational reporting">
      <div className="p-6">
        <div className="flex gap-5">
          {/* Sidebar */}
          <div className="w-52 shrink-0">
            <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
              <div className="px-4 py-3 border-b border-slate-100 bg-slate-50">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Available Reports</span>
              </div>
              <nav className="p-1.5 space-y-0.5">
                {reports.map(r => (
                  <button
                    key={r}
                    onClick={() => setActiveReport(r)}
                    className={`w-full text-left px-3 py-2 rounded-lg text-sm transition-colors ${activeReport === r ? 'bg-teal-600/10 text-teal-700 font-medium' : 'text-slate-600 hover:bg-slate-50'}`}
                  >
                    {r}
                  </button>
                ))}
              </nav>
            </div>
          </div>

          {/* Content */}
          <div className="flex-1 min-w-0 space-y-4">
            <div className="flex items-center justify-between">
              <h2 className="text-base font-semibold text-slate-800">{activeReport}</h2>
              <button className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>
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
