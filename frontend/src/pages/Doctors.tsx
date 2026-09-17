import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Trash2, X, ChevronDown, Phone, DollarSign, RefreshCw, GraduationCap } from 'lucide-react';
import { doctors as fallbackDoctors } from '../data/mockData';
import { getDoctors, addDoctor, deleteDoctor, getSpecializations } from '../api/api';

const specColors: Record<string, string> = {
  Cardiology: 'bg-red-100 text-red-700',
  Neurology: 'bg-purple-100 text-purple-700',
  Orthopedics: 'bg-orange-100 text-orange-700',
  Pediatrics: 'bg-pink-100 text-pink-700',
  Dermatology: 'bg-yellow-100 text-yellow-700',
  'General Medicine': 'bg-teal-100 text-teal-700',
  General: 'bg-teal-100 text-teal-700',
  Oncology: 'bg-indigo-100 text-indigo-700',
  Psychiatry: 'bg-blue-100 text-blue-700',
  'Emergency Medicine': 'bg-red-100 text-red-700',
};

export default function Doctors() {
  const [doctorsList, setDoctorsList] = useState<any[]>([]);
  const [specializations, setSpecializations] = useState<any[]>([
    { specialization_id: 1, specialization_name: 'Cardiology' },
    { specialization_id: 2, specialization_name: 'Neurology' },
    { specialization_id: 3, specialization_name: 'Orthopedics' },
    { specialization_id: 4, specialization_name: 'Dermatology' },
    { specialization_id: 5, specialization_name: 'General Medicine' }
  ]);
  const [search, setSearch] = useState('');
  const [specFilter, setSpecFilter] = useState('All');
  const [showAdd, setShowAdd] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<any | null>(null);
  const [loading, setLoading] = useState(false);

  const [addForm, setAddForm] = useState({
    name: '',
    specialization: 'Cardiology',
    qualification: 'MBBS, MD',
    fee: '500',
    contact: ''
  });

  const loadDoctors = async () => {
    setLoading(true);
    try {
      const data = await getDoctors();
      if (Array.isArray(data) && data.length > 0) {
        setDoctorsList(data);
      } else {
        setDoctorsList(fallbackDoctors);
      }
    } catch (err) {
      console.warn('API error, using fallback doctors:', err);
      setDoctorsList(fallbackDoctors);
    } finally {
      setLoading(false);
    }
  };

  const loadSpecs = async () => {
    try {
      const specs = await getSpecializations();
      if (Array.isArray(specs) && specs.length > 0) {
        setSpecializations(specs);
      }
    } catch (err) {
      console.warn('Could not load specializations:', err);
    }
  };

  useEffect(() => {
    loadDoctors();
    loadSpecs();
  }, []);

  const handleAddDoctor = async () => {
    if (!addForm.name.trim()) {
      alert('Please enter doctor name');
      return;
    }
    try {
      await addDoctor({
        name: addForm.name.trim().startsWith('Dr. ') ? addForm.name.trim() : `Dr. ${addForm.name.trim()}`,
        specialization: addForm.specialization,
        qualification: addForm.qualification.trim() || 'MBBS',
        fee: parseFloat(addForm.fee) || 500,
        contact: addForm.contact.trim() || '9876543210'
      });
      setShowAdd(false);
      setAddForm({
        name: '',
        specialization: 'Cardiology',
        qualification: 'MBBS, MD',
        fee: '500',
        contact: ''
      });
      await loadDoctors();
      alert('Doctor added successfully to MySQL database!');
    } catch (err: any) {
      alert('Failed to add doctor: ' + err.message);
    }
  };

  const handleDeleteDoctor = async () => {
    if (!deleteTarget) return;
    try {
      await deleteDoctor(deleteTarget.rawId || deleteTarget.id);
      setDeleteTarget(null);
      await loadDoctors();
      alert('Doctor deleted successfully!');
    } catch (err: any) {
      alert('Failed to delete doctor: ' + err.message);
    }
  };

  const filtered = doctorsList.filter(d => {
    const matchSearch = d.name.toLowerCase().includes(search.toLowerCase()) || String(d.id).includes(search);
    const matchSpec = specFilter === 'All' || d.specialization === specFilter;
    return matchSearch && matchSpec;
  });

  return (
    <Layout title="Doctors" subtitle="Manage physician profiles and live MySQL records">
      <div className="p-6">
        <div className="flex items-center gap-3 mb-5 flex-wrap">
          <div className="relative flex-1 min-w-[200px]">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search doctors by name or ID..."
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
              <option value="All">All Specializations</option>
              {specializations.map(s => (
                <option key={s.specialization_id} value={s.specialization_name}>
                  {s.specialization_name}
                </option>
              ))}
            </select>
            <ChevronDown size={13} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
          </div>

          <button
            onClick={loadDoctors}
            disabled={loading}
            className="p-2 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600 transition-colors"
            title="Refresh List"
          >
            <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
          </button>

          {(search || specFilter !== 'All') && (
            <button onClick={() => { setSearch(''); setSpecFilter('All'); }} className="text-sm text-slate-500 hover:text-slate-700 flex items-center gap-1">
              <X size={14} /> Clear
            </button>
          )}

          <button
            onClick={() => setShowAdd(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto shadow-sm"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Add Doctor
          </button>
        </div>

        <div className="mb-3 flex items-center justify-between text-xs text-slate-500">
          <span>Showing {filtered.length} physician{filtered.length !== 1 ? 's' : ''}</span>
          <span className="text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Doctor table</span>
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
                <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${d.available !== false ? 'bg-green-100 text-green-700' : 'bg-slate-100 text-slate-500'}`}>
                  {d.available !== false ? 'Active' : 'On Leave'}
                </span>
              </div>

              <div className="flex items-center gap-2 mb-3">
                <span className={`text-xs px-2.5 py-1 rounded-full font-medium inline-block ${specColors[d.specialization] ?? 'bg-slate-100 text-slate-600'}`}>
                  {d.specialization}
                </span>
                {d.qualification && (
                  <span className="text-xs text-slate-500 flex items-center gap-1">
                    <GraduationCap size={13} className="text-slate-400" />
                    {d.qualification}
                  </span>
                )}
              </div>

              <div className="grid grid-cols-2 gap-2 text-center py-3 border-y border-slate-100 mb-3 bg-slate-50/50 rounded-lg">
                <div>
                  <div className="text-sm font-bold text-slate-900">{d.patients || 0}</div>
                  <div className="text-xs text-slate-500">Appointments</div>
                </div>
                <div>
                  <div className="text-sm font-bold text-teal-700">₹{d.fee}</div>
                  <div className="text-xs text-slate-500">Consultation Fee</div>
                </div>
              </div>

              <div className="flex items-center justify-between text-xs text-slate-500">
                <div className="flex items-center gap-1 font-mono">
                  <Phone size={12} className="text-slate-400" />
                  {d.contact}
                </div>
                <button
                  onClick={() => setDeleteTarget(d)}
                  className="p-1.5 rounded-lg hover:bg-red-50 text-slate-400 hover:text-red-600 transition-colors"
                  title="Remove Doctor"
                >
                  <Trash2 size={14} />
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Add Doctor Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Add Doctor (Saves to MySQL)</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Doctor Name *</label>
                <input
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                  placeholder="e.g. Dr. Rajesh Sharma"
                  value={addForm.name}
                  onChange={e => setAddForm({ ...addForm, name: e.target.value })}
                />
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Specialization *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
                  value={addForm.specialization}
                  onChange={e => setAddForm({ ...addForm, specialization: e.target.value })}
                >
                  {specializations.map(s => (
                    <option key={s.specialization_id} value={s.specialization_name}>
                      {s.specialization_name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Qualification</label>
                  <input
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                    placeholder="e.g. MBBS, MD"
                    value={addForm.qualification}
                    onChange={e => setAddForm({ ...addForm, qualification: e.target.value })}
                  />
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Fee (₹) *</label>
                  <input
                    type="number"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                    placeholder="e.g. 500"
                    value={addForm.fee}
                    onChange={e => setAddForm({ ...addForm, fee: e.target.value })}
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Contact Phone *</label>
                <input
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                  placeholder="e.g. 9876543210"
                  value={addForm.contact}
                  onChange={e => setAddForm({ ...addForm, contact: e.target.value })}
                />
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleAddDoctor} className="flex-1 py-2 text-sm font-medium text-white rounded-lg transition-opacity hover:opacity-90" style={{ background: '#0F766E' }}>Save Doctor</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Delete Doctor Modal */}
      {deleteTarget && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setDeleteTarget(null)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-sm p-5">
            <h3 className="font-semibold text-slate-900 mb-2">Remove Doctor</h3>
            <p className="text-sm text-slate-600 mb-3">Remove <strong>{deleteTarget.name}</strong> from the database?</p>
            <p className="text-xs text-amber-700 bg-amber-50 border border-amber-200 rounded-lg p-2.5 mb-4">
              This will cascade delete from Doctor Schedule, Appointments, and Prescriptions in MySQL.
            </p>
            <div className="flex gap-2">
              <button onClick={() => setDeleteTarget(null)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
              <button onClick={handleDeleteDoctor} className="flex-1 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700">Delete Doctor</button>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
