import { useState } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Pencil, Trash2, X, FlaskConical, Clock, CheckCircle, Calendar } from 'lucide-react';
import { labTests } from '../data/mockData';

const statusColors: Record<string, string> = {
  Pending: 'bg-amber-100 text-amber-700',
  'In Progress': 'bg-blue-100 text-blue-700',
  Completed: 'bg-green-100 text-green-700',
};

export default function LabTests() {
  const [search, setSearch] = useState('');
  const [showAdd, setShowAdd] = useState(false);

  const filtered = labTests.filter(t =>
    t.patient.toLowerCase().includes(search.toLowerCase()) || t.id.includes(search)
  );

  const pending = labTests.filter(t => t.status === 'Pending').length;
  const inProgress = labTests.filter(t => t.status === 'In Progress').length;
  const completed = labTests.filter(t => t.status === 'Completed').length;
  const today = labTests.filter(t => t.ordered === '2026-09-17').length;

  return (
    <Layout title="Lab Tests" subtitle="Diagnostic tests and results management">
      <div className="p-6 space-y-5">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
          {[
            { label: 'Total Tests', value: labTests.length, icon: FlaskConical, color: '#7C3AED', bg: '#F5F3FF' },
            { label: 'Pending', value: pending, icon: Clock, color: '#D97706', bg: '#FFFBEB' },
            { label: 'Completed', value: completed, icon: CheckCircle, color: '#16A34A', bg: '#F0FDF4' },
            { label: "Today's Tests", value: today, icon: Calendar, color: '#0284C7', bg: '#F0F9FF' },
          ].map(({ label, value, icon: Icon, color, bg }) => (
            <div key={label} className="bg-white rounded-xl border border-slate-200 p-4">
              <div className="w-8 h-8 rounded-lg flex items-center justify-center mb-2" style={{ background: bg }}>
                <Icon size={15} style={{ color }} />
              </div>
              <div className="text-2xl font-bold text-slate-900">{value}</div>
              <div className="text-xs text-slate-500 mt-0.5">{label}</div>
            </div>
          ))}
        </div>

        <div className="flex items-center gap-3">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="text" placeholder="Search tests..." value={search} onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white" />
          </div>
          <button onClick={() => setShowAdd(true)} className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto" style={{ background: '#0F766E' }}>
            <Plus size={15} /> Order Test
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Test ID', 'Patient', 'Test Type', 'Doctor', 'Ordered', 'Result', 'Cost', 'Status', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(t => (
                  <tr key={t.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{t.id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{t.patient}</td>
                    <td className="px-4 py-3">
                      <span className="text-xs px-2 py-0.5 bg-purple-100 text-purple-700 rounded-full font-medium">{t.type}</span>
                    </td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{t.doctor}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{t.ordered}</td>
                    <td className="px-4 py-3 text-slate-600 text-sm">
                      {t.result === 'Pending' ? <span className="text-slate-400 text-xs">—</span> : (
                        <span className={`text-xs font-medium ${t.result === 'Abnormal' ? 'text-red-600' : 'text-green-600'}`}>{t.result}</span>
                      )}
                    </td>
                    <td className="px-4 py-3 font-semibold text-slate-900">${t.cost}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[t.status]}`}>{t.status}</span>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex gap-1">
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

      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Order Lab Test</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              {['Patient', 'Test Type', 'Ordering Doctor', 'Notes'].map(f => (
                <div key={f}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  {f === 'Notes'
                    ? <textarea className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 resize-none h-16" />
                    : <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400" />
                  }
                </div>
              ))}
              <div className="flex gap-2 mt-4">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Order Test</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
