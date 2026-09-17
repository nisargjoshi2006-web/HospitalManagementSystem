import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import {
  Users, UserRound, Calendar, AlertTriangle, Bed, FlaskConical,
  CreditCard, TrendingUp, TrendingDown, RefreshCw, Plus,
  ClipboardList, UserPlus
} from 'lucide-react';
import { appointments, emergencyCases, labTests, bills, revenueData, weeklyRevenue } from '../data/mockData';
import { AreaChart, Area, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from 'recharts';
import { useNavigate } from 'react-router-dom';
import { getDashboard } from '../api/api';

const statusColors: Record<string, string> = {
  Confirmed: 'bg-green-100 text-green-700',
  Scheduled: 'bg-blue-100 text-blue-700',
  Completed: 'bg-slate-100 text-slate-600',
  Cancelled: 'bg-red-100 text-red-600',
  Pending: 'bg-amber-100 text-amber-700',
};

const priorityColors: Record<string, string> = {
  Critical: 'bg-red-100 text-red-700 border border-red-200',
  High: 'bg-orange-100 text-orange-700 border border-orange-200',
  Medium: 'bg-yellow-100 text-yellow-700 border border-yellow-200',
  Low: 'bg-slate-100 text-slate-600',
};

export default function Dashboard() {
  const navigate = useNavigate();
  const today = new Date().toLocaleDateString('en-US', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
  const [data, setData] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  const loadData = async () => {
    setLoading(true);
    try {
      const res = await getDashboard();
      setData(res);
    } catch (e) {
      console.warn('Could not load live dashboard data, using mock:', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const kpis = [
    { label: 'Total Patients', value: data?.kpis?.totalPatients?.toString() ?? '34', icon: Users, trend: '+Live DB', up: true, color: '#0F766E', bg: '#F0FDFA' },
    { label: 'Doctors', value: data?.kpis?.totalDoctors?.toString() ?? '3', icon: UserRound, trend: 'Verified', up: true, color: '#6366F1', bg: '#EEF2FF' },
    { label: "Today's Appointments", value: data?.kpis?.todayAppointments?.toString() ?? '0', icon: Calendar, trend: 'Active', up: true, color: '#0284C7', bg: '#F0F9FF' },
    { label: 'Emergency Cases', value: data?.kpis?.activeEmergencies?.toString() ?? '6', icon: AlertTriangle, trend: 'Triage', up: false, color: '#DC2626', bg: '#FEF2F2' },
    { label: 'Occupied Beds', value: `${data?.kpis?.occupiedBeds ?? 1}/72`, icon: Bed, trend: 'Tracked', up: false, color: '#D97706', bg: '#FFFBEB' },
    { label: 'Pending Lab Tests', value: data?.kpis?.pendingLabTests?.toString() ?? '2', icon: FlaskConical, trend: 'Lab', up: true, color: '#7C3AED', bg: '#F5F3FF' },
    { label: 'Pending Bills', value: data?.kpis?.pendingBills?.toString() ?? '1', icon: CreditCard, trend: 'Invoice', up: false, color: '#0F766E', bg: '#F0FDFA' },
    { label: 'Revenue (Paid)', value: `₹${(data?.kpis?.totalRevenue ?? 3400).toLocaleString()}`, icon: TrendingUp, trend: '+Realtime', up: true, color: '#16A34A', bg: '#F0FDF4' },
  ];

  const displayApts = data?.recentAppointments && data.recentAppointments.length > 0
    ? data.recentAppointments
    : appointments.slice(0, 6);

  const displayEmergencies = data?.recentEmergencies && data.recentEmergencies.length > 0
    ? data.recentEmergencies
    : emergencyCases;

  return (
    <Layout title="Dashboard" subtitle={`Today's hospital overview · ${today}`}>
      <div className="p-6 space-y-6">
        {/* KPI row */}
        <div className="grid grid-cols-2 md:grid-cols-4 xl:grid-cols-8 gap-3">
          {kpis.map(({ label, value, icon: Icon, trend, up, color, bg }) => (
            <div key={label} className="bg-white rounded-xl border border-slate-200 p-4 hover:shadow-sm transition-shadow">
              <div className="flex items-center justify-between mb-3">
                <div className="w-8 h-8 rounded-lg flex items-center justify-center" style={{ background: bg }}>
                  <Icon size={15} style={{ color }} />
                </div>
                <span className={`text-xs font-medium flex items-center gap-0.5 ${up ? 'text-green-600' : 'text-amber-600'}`}>
                  {up ? <TrendingUp size={11} /> : <TrendingDown size={11} />}
                  {trend}
                </span>
              </div>
              <div className="text-xl font-bold text-slate-900">{value}</div>
              <div className="text-xs text-slate-500 mt-0.5 leading-tight">{label}</div>
            </div>
          ))}
        </div>

        {/* Main grid */}
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
          {/* Appointments table */}
          <div className="xl:col-span-2 bg-white rounded-xl border border-slate-200">
            <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100">
              <div>
                <h3 className="font-semibold text-slate-800 text-sm">Recent Appointments</h3>
                <p className="text-xs text-slate-500 mt-0.5">{displayApts.length} appointments on record</p>
              </div>
              <button onClick={() => navigate('/app/appointments')} className="text-xs text-teal-600 hover:text-teal-700 font-medium">
                View all →
              </button>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-slate-100">
                    {['Patient', 'Doctor', 'Time', 'Room', 'Status'].map(h => (
                      <th key={h} className="px-4 py-2.5 text-left text-xs font-medium text-slate-500">{h}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {displayApts.map((a: any) => (
                    <tr key={a.id} className="border-b border-slate-50 hover:bg-slate-50/50 transition-colors">
                      <td className="px-4 py-2.5 font-medium text-slate-800">{a.patient}</td>
                      <td className="px-4 py-2.5 text-slate-600">{a.doctor}</td>
                      <td className="px-4 py-2.5 font-mono text-slate-700 text-xs">{a.time}</td>
                      <td className="px-4 py-2.5 text-slate-500 text-xs">{a.room}</td>
                      <td className="px-4 py-2.5">
                        <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[a.status] || 'bg-slate-100 text-slate-600'}`}>
                          {a.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Emergency cases */}
          <div className="bg-white rounded-xl border border-slate-200">
            <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100">
              <div>
                <h3 className="font-semibold text-slate-800 text-sm flex items-center gap-1.5">
                  <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
                  Emergency Cases
                </h3>
                <p className="text-xs text-slate-500 mt-0.5">{displayEmergencies.length} active</p>
              </div>
              <button onClick={() => navigate('/app/emergency')} className="text-xs text-teal-600 hover:text-teal-700 font-medium">
                View all →
              </button>
            </div>
            <div className="divide-y divide-slate-50">
              {displayEmergencies.map((c: any) => (
                <div key={c.id} className={`p-4 ${c.priority === 'Critical' ? 'bg-red-50/50' : ''}`}>
                  <div className="flex items-start justify-between gap-2 mb-1">
                    <span className="font-medium text-sm text-slate-800 truncate">{c.patient}</span>
                    <span className={`text-xs px-2 py-0.5 rounded-full font-medium shrink-0 ${priorityColors[c.priority] || 'bg-slate-100'}`}>
                      {c.priority}
                    </span>
                  </div>
                  <div className="text-xs text-slate-500">{c.doctor} · {c.arrival}</div>
                  <div className="text-xs text-slate-600 mt-1">{c.status}</div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Second row */}
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
          {/* Revenue chart */}
          <div className="xl:col-span-2 bg-white rounded-xl border border-slate-200 p-5">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h3 className="font-semibold text-slate-800 text-sm">Revenue Overview</h3>
                <p className="text-xs text-slate-500 mt-0.5">Monthly revenue trend (Mar–Sep 2026)</p>
              </div>
              <div className="text-right">
                <div className="text-lg font-bold text-slate-900">$38,200</div>
                <div className="text-xs text-green-600">Sep MTD</div>
              </div>
            </div>
            <ResponsiveContainer width="100%" height={180}>
              <AreaChart data={revenueData}>
                <defs>
                  <linearGradient id="rev" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#0F766E" stopOpacity={0.15} />
                    <stop offset="95%" stopColor="#0F766E" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#F1F5F9" />
                <XAxis dataKey="month" tick={{ fontSize: 11, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 11, fill: '#94A3B8' }} axisLine={false} tickLine={false} tickFormatter={v => `$${v / 1000}k`} />
                <Tooltip
                  formatter={(v: any) => [`$${(v as number).toLocaleString()}`, "Revenue"]}
                  contentStyle={{ border: '1px solid #E2E8F0', borderRadius: 8, fontSize: 12 }}
                />
                <Area type="monotone" dataKey="revenue" stroke="#0F766E" strokeWidth={2} fill="url(#rev)" dot={false} />
              </AreaChart>
            </ResponsiveContainer>
          </div>

          {/* Bed occupancy */}
          <div className="bg-white rounded-xl border border-slate-200 p-5">
            <h3 className="font-semibold text-slate-800 text-sm mb-4">Bed Occupancy</h3>
            <div className="space-y-4">
              {[
                { ward: 'ICU', occupied: 9, total: 12, color: '#DC2626', bg: '#FEF2F2' },
                { ward: 'Private', occupied: 14, total: 20, color: '#0284C7', bg: '#F0F9FF' },
                { ward: 'General', occupied: 28, total: 40, color: '#0F766E', bg: '#F0FDFA' },
              ].map(({ ward, occupied, total, color, bg }) => (
                <div key={ward}>
                  <div className="flex justify-between text-xs mb-1.5">
                    <span className="font-medium text-slate-700">{ward}</span>
                    <span className="text-slate-500">{occupied}/{total} occupied</span>
                  </div>
                  <div className="h-2 bg-slate-100 rounded-full overflow-hidden">
                    <div
                      className="h-full rounded-full transition-all duration-700"
                      style={{ width: `${(occupied / total) * 100}%`, background: color }}
                    />
                  </div>
                  <div className="text-xs mt-1" style={{ color }}>
                    {Math.round((occupied / total) * 100)}% capacity · {total - occupied} available
                  </div>
                </div>
              ))}
            </div>

            {/* Weekly revenue bar */}
            <div className="mt-5 pt-4 border-t border-slate-100">
              <h4 className="text-xs font-medium text-slate-700 mb-3">Weekly Revenue</h4>
              <ResponsiveContainer width="100%" height={80}>
                <BarChart data={weeklyRevenue} barSize={16}>
                  <XAxis dataKey="day" tick={{ fontSize: 10, fill: '#94A3B8' }} axisLine={false} tickLine={false} />
                  <Tooltip
                    formatter={(v: any) => [`$${(v as number).toLocaleString()}`, '']}
                    contentStyle={{ border: '1px solid #E2E8F0', borderRadius: 6, fontSize: 11 }}
                  />
                  <Bar dataKey="revenue" fill="#0F766E" radius={[3, 3, 0, 0]} opacity={0.8} />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>

        {/* Third row: lab tests + quick actions */}
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-5">
          <div className="xl:col-span-2 bg-white rounded-xl border border-slate-200">
            <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100">
              <h3 className="font-semibold text-slate-800 text-sm">Pending Lab Tests</h3>
              <button onClick={() => navigate('/app/lab-tests')} className="text-xs text-teal-600 hover:text-teal-700 font-medium">View all →</button>
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b border-slate-100">
                    {['Test ID', 'Patient', 'Test Type', 'Doctor', 'Status'].map(h => (
                      <th key={h} className="px-4 py-2.5 text-left text-xs font-medium text-slate-500">{h}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {labTests.filter(t => t.status !== 'Completed').map(t => (
                    <tr key={t.id} className="border-b border-slate-50 hover:bg-slate-50/50 transition-colors">
                      <td className="px-4 py-2.5 font-mono text-xs text-slate-500">{t.id}</td>
                      <td className="px-4 py-2.5 font-medium text-slate-800">{t.patient}</td>
                      <td className="px-4 py-2.5 text-slate-600">{t.type}</td>
                      <td className="px-4 py-2.5 text-slate-500 text-xs">{t.doctor}</td>
                      <td className="px-4 py-2.5">
                        <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${t.status === 'In Progress' ? 'bg-blue-100 text-blue-700' : 'bg-amber-100 text-amber-700'}`}>
                          {t.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* Quick actions */}
          <div className="bg-white rounded-xl border border-slate-200 p-5">
            <div className="flex items-center justify-between mb-4">
              <h3 className="font-semibold text-slate-800 text-sm">Quick Actions</h3>
              <button className="text-xs text-slate-500 hover:text-slate-700 flex items-center gap-1">
                <RefreshCw size={11} /> Refresh
              </button>
            </div>
            <div className="grid grid-cols-2 gap-2">
              {[
                { label: 'Add Patient', icon: UserPlus, path: '/app/patients', color: '#0F766E' },
                { label: 'Book Appointment', icon: Calendar, path: '/app/appointments', color: '#0284C7' },
                { label: 'Add Prescription', icon: ClipboardList, path: '/app/prescriptions', color: '#7C3AED' },
                { label: 'Create Bill', icon: CreditCard, path: '/app/billing', color: '#D97706' },
                { label: 'Admit Patient', icon: Plus, path: '/app/beds', color: '#16A34A' },
                { label: 'Emergency Case', icon: AlertTriangle, path: '/app/emergency', color: '#DC2626' },
              ].map(({ label, icon: Icon, path, color }) => (
                <button
                  key={label}
                  onClick={() => navigate(path)}
                  className="flex flex-col items-center gap-2 p-3 rounded-xl border border-slate-200 hover:border-slate-300 hover:bg-slate-50 transition-all text-center group"
                >
                  <div className="w-8 h-8 rounded-lg flex items-center justify-center" style={{ background: `${color}15` }}>
                    <Icon size={15} style={{ color }} />
                  </div>
                  <span className="text-xs font-medium text-slate-700 leading-tight">{label}</span>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}
