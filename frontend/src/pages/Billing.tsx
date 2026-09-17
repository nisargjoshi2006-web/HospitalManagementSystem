import { useState } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Eye, Pencil, Trash2, X, TrendingUp, CreditCard, Clock, AlertCircle } from 'lucide-react';
import { bills } from '../data/mockData';

const statusColors: Record<string, string> = {
  Paid: 'bg-green-100 text-green-700',
  Pending: 'bg-amber-100 text-amber-700',
  Failed: 'bg-red-100 text-red-700',
};

const methodIcons: Record<string, string> = {
  Cash: '💵',
  Card: '💳',
  UPI: '📱',
  Online: '🌐',
};

export default function Billing() {
  const [search, setSearch] = useState('');
  const [showCreate, setShowCreate] = useState(false);

  const filtered = bills.filter(b =>
    b.patient.toLowerCase().includes(search.toLowerCase()) ||
    b.id.includes(search)
  );

  const totalRevenue = bills.filter(b => b.status === 'Paid').reduce((s, b) => s + b.fee, 0);
  const pending = bills.filter(b => b.status === 'Pending').reduce((s, b) => s + b.fee, 0);
  const todayRevenue = bills.filter(b => b.date === '2026-09-17' && b.status === 'Paid').reduce((s, b) => s + b.fee, 0);

  return (
    <Layout title="Billing" subtitle="Manage payments and invoices">
      <div className="p-6 space-y-5">
        {/* Summary cards */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
          {[
            { label: 'Total Revenue', value: `$${totalRevenue.toLocaleString()}`, icon: TrendingUp, color: '#16A34A', bg: '#F0FDF4' },
            { label: 'Paid Bills', value: bills.filter(b => b.status === 'Paid').length.toString(), icon: CreditCard, color: '#0F766E', bg: '#F0FDFA' },
            { label: 'Pending', value: `$${pending.toLocaleString()}`, icon: Clock, color: '#D97706', bg: '#FFFBEB' },
            { label: "Today's Revenue", value: `$${todayRevenue.toLocaleString()}`, icon: AlertCircle, color: '#0284C7', bg: '#F0F9FF' },
          ].map(({ label, value, icon: Icon, color, bg }) => (
            <div key={label} className="bg-white rounded-xl border border-slate-200 p-4">
              <div className="w-8 h-8 rounded-lg flex items-center justify-center mb-2" style={{ background: bg }}>
                <Icon size={15} style={{ color }} />
              </div>
              <div className="text-xl font-bold text-slate-900">{value}</div>
              <div className="text-xs text-slate-500 mt-0.5">{label}</div>
            </div>
          ))}
        </div>

        <div className="flex items-center gap-3">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="text" placeholder="Search bills..." value={search} onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white" />
          </div>
          <button onClick={() => setShowCreate(true)} className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto" style={{ background: '#0F766E' }}>
            <Plus size={15} /> Create Bill
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Bill ID', 'Patient', 'Doctor', 'Appointment', 'Fee', 'Method', 'Status', 'Date', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(b => (
                  <tr key={b.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{b.id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{b.patient}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{b.doctor}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{b.appointment}</td>
                    <td className="px-4 py-3 font-semibold text-slate-900">${b.fee}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{methodIcons[b.method]} {b.method}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[b.status]}`}>{b.status}</span>
                    </td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{b.date}</td>
                    <td className="px-4 py-3">
                      <div className="flex gap-1">
                        <button className="p-1.5 rounded-lg hover:bg-teal-50 text-slate-400 hover:text-teal-600 transition-colors"><Eye size={13} /></button>
                        <button className="p-1.5 rounded-lg hover:bg-blue-50 text-slate-400 hover:text-blue-600 transition-colors"><Pencil size={13} /></button>
                        <button className="p-1.5 rounded-lg hover:bg-red-50 text-slate-400 hover:text-red-600 transition-colors"><Trash2 size={13} /></button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {showCreate && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowCreate(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-lg">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Create Bill</h3>
              <button onClick={() => setShowCreate(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 grid grid-cols-2 gap-3">
              {['Patient', 'Doctor', 'Appointment ID', 'Payment Method'].map(f => (
                <div key={f}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400" />
                </div>
              ))}
              <div className="col-span-2 bg-slate-50 rounded-lg p-3 border border-slate-200">
                <div className="flex justify-between text-sm">
                  <span className="text-slate-600">Consultation Fee (auto-calculated)</span>
                  <span className="font-semibold text-slate-900">$250.00</span>
                </div>
              </div>
              <div className="col-span-2 flex gap-2 mt-2">
                <button onClick={() => setShowCreate(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowCreate(false)} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Create Bill</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
