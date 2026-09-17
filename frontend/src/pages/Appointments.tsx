import { useState } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, ChevronDown, X, Pencil, Trash2 } from 'lucide-react';
import { appointments } from '../data/mockData';

const statusColors: Record<string, string> = {
  Confirmed: 'bg-green-100 text-green-700',
  Scheduled: 'bg-blue-100 text-blue-700',
  Completed: 'bg-slate-100 text-slate-600',
  Cancelled: 'bg-red-100 text-red-600',
};

export default function Appointments() {
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [showBook, setShowBook] = useState(false);

  const filtered = appointments.filter(a => {
    const matchSearch = a.patient.toLowerCase().includes(search.toLowerCase()) || a.doctor.toLowerCase().includes(search.toLowerCase()) || a.id.includes(search);
    const matchStatus = statusFilter === 'All' || a.status === statusFilter;
    return matchSearch && matchStatus;
  });

  const counts = ['Scheduled', 'Confirmed', 'Completed', 'Cancelled'].map(s => ({
    label: s, count: appointments.filter(a => a.status === s).length, color: statusColors[s],
  }));

  return (
    <Layout title="Appointments" subtitle="Schedule and manage patient appointments">
      <div className="p-6 space-y-5">
        {/* Status summary */}
        <div className="grid grid-cols-4 gap-3">
          {counts.map(({ label, count, color }) => (
            <button
              key={label}
              onClick={() => setStatusFilter(statusFilter === label ? 'All' : label)}
              className={`bg-white rounded-xl border p-4 text-left transition-all hover:shadow-sm ${statusFilter === label ? 'border-teal-400 ring-1 ring-teal-400/20' : 'border-slate-200'}`}
            >
              <div className="text-2xl font-bold text-slate-900 mb-1">{count}</div>
              <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${color}`}>{label}</span>
            </button>
          ))}
        </div>

        {/* Toolbar */}
        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 min-w-[200px]">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search patient, doctor, ID..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
            />
          </div>
          <div className="relative">
            <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)} className="appearance-none pl-3 pr-8 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none bg-white text-slate-700">
              {['All', 'Scheduled', 'Confirmed', 'Completed', 'Cancelled'].map(s => <option key={s}>{s === 'All' ? 'All Statuses' : s}</option>)}
            </select>
            <ChevronDown size={13} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
          </div>
          <button
            onClick={() => setShowBook(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Book Appointment
          </button>
        </div>

        {/* Table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Apt ID', 'Patient', 'Doctor', 'Specialization', 'Date', 'Time', 'Room', 'Status', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(a => (
                  <tr key={a.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{a.id}</td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2">
                        <div className="w-6 h-6 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center text-xs font-semibold shrink-0">
                          {a.patient.charAt(0)}
                        </div>
                        <span className="font-medium text-slate-800">{a.patient}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-slate-600">{a.doctor}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{a.specialization}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{a.date}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-700">{a.time}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{a.room}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[a.status]}`}>{a.status}</span>
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

      {/* Book modal */}
      {showBook && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowBook(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-lg">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Book Appointment</h3>
              <button onClick={() => setShowBook(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 grid grid-cols-2 gap-3">
              {['Patient', 'Doctor', 'Date', 'Time', 'Room', 'Notes'].map(f => (
                <div key={f} className={f === 'Notes' ? 'col-span-2' : ''}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  {f === 'Notes'
                    ? <textarea className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 resize-none h-16" placeholder="Any additional notes..." />
                    : <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400" placeholder={`Select ${f.toLowerCase()}`} />
                  }
                </div>
              ))}
              <div className="col-span-2 flex gap-2 mt-2">
                <button onClick={() => setShowBook(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowBook(false)} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Book Appointment</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
