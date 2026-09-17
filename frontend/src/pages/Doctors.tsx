import { useState } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Pencil, Trash2, X, ChevronDown, Phone, DollarSign } from 'lucide-react';
import { doctors } from '../data/mockData';

const specializations = ['All', 'Cardiology', 'Neurology', 'Orthopedics', 'Pediatrics', 'Dermatology', 'Oncology', 'Psychiatry', 'Emergency Medicine'];
const specColors: Record<string, string> = {
  Cardiology: 'bg-red-100 text-red-700',
  Neurology: 'bg-purple-100 text-purple-700',
  Orthopedics: 'bg-orange-100 text-orange-700',
  Pediatrics: 'bg-pink-100 text-pink-700',
  Dermatology: 'bg-yellow-100 text-yellow-700',
  Oncology: 'bg-indigo-100 text-indigo-700',
  Psychiatry: 'bg-blue-100 text-blue-700',
  'Emergency Medicine': 'bg-red-100 text-red-700',
};

export default function Doctors() {
  const [search, setSearch] = useState('');
  const [specFilter, setSpecFilter] = useState('All');
  const [showAdd, setShowAdd] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<string | null>(null);

  const filtered = doctors.filter(d => {
    const matchSearch = d.name.toLowerCase().includes(search.toLowerCase()) || d.id.includes(search);
    const matchSpec = specFilter === 'All' || d.specialization === specFilter;
    return matchSearch && matchSpec;
  });

  return (
    <Layout title="Doctors" subtitle="Manage physician records and availability">
      <div className="p-6">
        <div className="flex items-center gap-3 mb-5 flex-wrap">
          <div className="relative flex-1 min-w-[200px]">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search doctors..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
            />
          </div>
          <div className="relative">
            <select
              value={specFilter}
              onChange={e => setSpecFilter(e.target.value)}
              className="appearance-none pl-3 pr-8 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none bg-white text-slate-700"
            >
              {specializations.map(s => <option key={s}>{s === 'All' ? 'All Specializations' : s}</option>)}
            </select>
            <ChevronDown size={13} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
          </div>
          <button
            onClick={() => setShowAdd(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Add Doctor
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
          {filtered.map(d => (
            <div key={d.id} className="bg-white rounded-xl border border-slate-200 p-5 hover:shadow-sm transition-shadow">
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  <div className="w-11 h-11 rounded-full bg-indigo-100 text-indigo-700 flex items-center justify-center text-base font-bold shrink-0">
                    {d.name.replace('Dr. ', '').charAt(0)}
                  </div>
                  <div>
                    <div className="font-semibold text-slate-900 text-sm leading-tight">{d.name}</div>
                    <div className="text-xs text-slate-500 font-mono mt-0.5">{d.id}</div>
                  </div>
                </div>
                <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${d.available ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
                  {d.available ? 'Available' : 'Unavailable'}
                </span>
              </div>

              <span className={`text-xs px-2.5 py-1 rounded-full font-medium inline-block mb-3 ${specColors[d.specialization] ?? 'bg-slate-100 text-slate-600'}`}>
                {d.specialization}
              </span>

              <div className="grid grid-cols-3 gap-2 text-center py-3 border-y border-slate-100 mb-3">
                <div>
                  <div className="text-sm font-bold text-slate-900">{d.experience}y</div>
                  <div className="text-xs text-slate-500">Experience</div>
                </div>
                <div>
                  <div className="text-sm font-bold text-slate-900">{d.patients}</div>
                  <div className="text-xs text-slate-500">Patients</div>
                </div>
                <div>
                  <div className="text-sm font-bold text-slate-900">${d.fee}</div>
                  <div className="text-xs text-slate-500">Fee</div>
                </div>
              </div>

              <div className="flex items-center gap-1 text-xs text-slate-500 mb-3">
                <Phone size={11} />
                <span className="font-mono">{d.contact}</span>
              </div>

              <div className="flex gap-2">
                <button className="flex-1 py-1.5 text-xs font-medium border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-600 flex items-center justify-center gap-1">
                  <Pencil size={12} /> Edit
                </button>
                <button onClick={() => setDeleteTarget(d.id)} className="py-1.5 px-3 text-xs border border-red-200 rounded-lg hover:bg-red-50 text-red-600 flex items-center justify-center gap-1">
                  <Trash2 size={12} />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Add Doctor</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              {['Full Name', 'Specialization', 'Consultation Fee', 'Phone', 'Experience (years)'].map(f => (
                <div key={f}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400" placeholder={`Enter ${f.toLowerCase()}`} />
                </div>
              ))}
              <div className="flex gap-2 mt-4">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Add Doctor</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {deleteTarget && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setDeleteTarget(null)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-sm p-5">
            <h3 className="font-semibold text-slate-900 mb-2">Remove Doctor</h3>
            <p className="text-sm text-slate-600 mb-3">Remove doctor <strong>{deleteTarget}</strong> from the system?</p>
            <p className="text-xs text-amber-700 bg-amber-50 border border-amber-200 rounded-lg p-2.5 mb-4">Their scheduled appointments will need to be reassigned.</p>
            <div className="flex gap-2">
              <button onClick={() => setDeleteTarget(null)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
              <button onClick={() => setDeleteTarget(null)} className="flex-1 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700">Remove</button>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
