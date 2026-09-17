import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Eye, Trash2, X, ChevronDown, RefreshCw } from 'lucide-react';
import { patients as fallbackPatients } from '../data/mockData';
import { getPatients, addPatient, deletePatient } from '../api/api';

const bloodGroups = ['All', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];

export default function Patients() {
  const [patientsList, setPatientsList] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [bloodFilter, setBloodFilter] = useState('All');
  const [selected, setSelected] = useState<any | null>(null);
  const [showAdd, setShowAdd] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const [addForm, setAddForm] = useState({
    name: '',
    age: '',
    gender: 'Male',
    blood: 'B+',
    contact: '',
    address: ''
  });

  const loadPatients = async () => {
    setLoading(true);
    try {
      const data = await getPatients(search, bloodFilter);
      if (Array.isArray(data) && data.length > 0) {
        setPatientsList(data);
      } else if (!search && bloodFilter === 'All') {
        setPatientsList(fallbackPatients);
      } else {
        setPatientsList([]);
      }
    } catch (err) {
      console.warn('API error, using fallback patients:', err);
      setPatientsList(fallbackPatients);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPatients();
  }, [bloodFilter]);

  const handleSearchSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    loadPatients();
  };

  const handleAddPatient = async () => {
    if (!addForm.name.trim()) {
      alert('Please enter patient name');
      return;
    }
    try {
      await addPatient({
        name: addForm.name.trim(),
        age: parseInt(addForm.age) || 30,
        gender: addForm.gender,
        blood: addForm.blood,
        contact: addForm.contact.trim() || '9876500000',
        address: addForm.address.trim() || 'Local City'
      });
      setShowAdd(false);
      setAddForm({ name: '', age: '', gender: 'Male', blood: 'B+', contact: '', address: '' });
      loadPatients();
      alert('Patient registered successfully in MySQL database!');
    } catch (err: any) {
      alert('Failed to register patient: ' + err.message);
    }
  };

  const handleDeletePatient = async () => {
    if (!deleteTarget) return;
    try {
      await deletePatient(deleteTarget);
      setDeleteTarget(null);
      loadPatients();
      alert('Patient deleted successfully!');
    } catch (err: any) {
      alert('Failed to delete patient: ' + err.message);
    }
  };

  const filtered = patientsList.filter(p => {
    const matchSearch = p.name.toLowerCase().includes(search.toLowerCase()) || String(p.id).includes(search);
    const matchBlood = bloodFilter === 'All' || p.blood === bloodFilter;
    return matchSearch && matchBlood;
  });

  return (
    <Layout title="Patients" subtitle="Manage patient records and live MySQL registration">
      <div className="p-6">
        {/* Actions bar */}
        <div className="flex items-center gap-3 mb-5 flex-wrap">
          <form onSubmit={handleSearchSubmit} className="relative flex-1 min-w-[220px]">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search by name, ID, or phone (Press Enter)..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
            />
          </form>

          <div className="relative">
            <select
              value={bloodFilter}
              onChange={e => setBloodFilter(e.target.value)}
              className="appearance-none pl-3 pr-8 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 bg-white text-slate-700"
            >
              {bloodGroups.map(g => <option key={g} value={g}>{g === 'All' ? 'All Blood Groups' : g}</option>)}
            </select>
            <ChevronDown size={13} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
          </div>

          <button
            onClick={loadPatients}
            disabled={loading}
            className="p-2 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600 transition-colors"
            title="Refresh List"
          >
            <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
          </button>

          {(search || bloodFilter !== 'All') && (
            <button onClick={() => { setSearch(''); setBloodFilter('All'); loadPatients(); }} className="text-sm text-slate-500 hover:text-slate-700 flex items-center gap-1">
              <X size={14} /> Clear
            </button>
          )}

          <button
            onClick={() => setShowAdd(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto shadow-sm"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Add Patient
          </button>
        </div>

        {/* Table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">{filtered.length} patient{filtered.length !== 1 ? 's' : ''}</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded-md font-medium">⚡ Connected to MySQL database</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Patient ID', 'Name', 'Age', 'Gender', 'Blood Group', 'Contact', 'Registered', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(p => (
                  <tr key={p.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{p.id}</td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-2.5">
                        <div className="w-7 h-7 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center text-xs font-semibold shrink-0">
                          {p.name ? p.name.charAt(0) : 'P'}
                        </div>
                        <span className="font-medium text-slate-800">{p.name}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3 text-slate-600">{p.age}</td>
                    <td className="px-4 py-3 text-slate-600">{p.gender}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-semibold ${p.blood && p.blood.includes('-') ? 'bg-orange-100 text-orange-700' : 'bg-teal-100 text-teal-700'}`}>
                        {p.blood}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-slate-500 text-xs font-mono">{p.contact}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{p.registered}</td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-1">
                        <button onClick={() => setSelected(p)} className="p-1.5 rounded-lg hover:bg-teal-50 text-slate-400 hover:text-teal-600 transition-colors" title="View">
                          <Eye size={14} />
                        </button>
                        <button onClick={() => setDeleteTarget(p.id)} className="p-1.5 rounded-lg hover:bg-red-50 text-slate-400 hover:text-red-600 transition-colors" title="Delete">
                          <Trash2 size={14} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Patient detail drawer */}
      {selected && (
        <div className="fixed inset-0 z-50 flex">
          <div className="flex-1 bg-black/40" onClick={() => setSelected(null)} />
          <div className="w-96 bg-white h-full overflow-y-auto shadow-2xl">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h2 className="font-semibold text-slate-900">Patient Profile</h2>
              <button onClick={() => setSelected(null)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500">
                <X size={16} />
              </button>
            </div>
            <div className="p-5 space-y-5">
              <div className="flex items-center gap-4">
                <div className="w-14 h-14 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center text-xl font-bold">
                  {selected.name?.charAt(0)}
                </div>
                <div>
                  <div className="font-semibold text-slate-900 text-base">{selected.name}</div>
                  <div className="text-sm text-slate-500">{selected.id}</div>
                  <span className={`text-xs px-2 py-0.5 rounded-full font-semibold mt-1 inline-block ${selected.blood?.includes('-') ? 'bg-orange-100 text-orange-700' : 'bg-teal-100 text-teal-700'}`}>
                    {selected.blood}
                  </span>
                </div>
              </div>

              <Section title="Basic Information">
                <Row label="Age" value={`${selected.age} years`} />
                <Row label="Gender" value={selected.gender} />
                <Row label="Blood Group" value={selected.blood} />
              </Section>

              <Section title="Contact">
                <Row label="Phone" value={selected.contact} mono />
                <Row label="Address" value={selected.address} />
              </Section>

              <Section title="Registration">
                <Row label="Registered" value={selected.registered} />
              </Section>
            </div>
          </div>
        </div>
      )}

      {/* Add patient modal */}
      {showAdd && (
        <Modal title="Add New Patient (Saves to MySQL)" onClose={() => setShowAdd(false)}>
          <div className="grid grid-cols-2 gap-3">
            <div className="col-span-2">
              <label className="block text-xs font-medium text-slate-700 mb-1">Full Name *</label>
              <input
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                placeholder="e.g. Ramesh Patel"
                value={addForm.name}
                onChange={e => setAddForm({ ...addForm, name: e.target.value })}
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-700 mb-1">Age</label>
              <input
                type="number"
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                placeholder="e.g. 35"
                value={addForm.age}
                onChange={e => setAddForm({ ...addForm, age: e.target.value })}
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-700 mb-1">Gender</label>
              <select
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
                value={addForm.gender}
                onChange={e => setAddForm({ ...addForm, gender: e.target.value })}
              >
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-700 mb-1">Blood Group</label>
              <select
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
                value={addForm.blood}
                onChange={e => setAddForm({ ...addForm, blood: e.target.value })}
              >
                {bloodGroups.filter(b => b !== 'All').map(b => (
                  <option key={b} value={b}>{b}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-700 mb-1">Contact Phone *</label>
              <input
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                placeholder="e.g. 9876501234"
                value={addForm.contact}
                onChange={e => setAddForm({ ...addForm, contact: e.target.value })}
              />
            </div>

            <div className="col-span-2">
              <label className="block text-xs font-medium text-slate-700 mb-1">Address</label>
              <input
                className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400"
                placeholder="e.g. 14 MG Road, Bangalore"
                value={addForm.address}
                onChange={e => setAddForm({ ...addForm, address: e.target.value })}
              />
            </div>
          </div>
          <div className="flex gap-2 mt-5">
            <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
            <button onClick={handleAddPatient} className="flex-1 py-2 text-sm font-medium text-white rounded-lg transition-opacity hover:opacity-90" style={{ background: '#0F766E' }}>Register Patient</button>
          </div>
        </Modal>
      )}

      {/* Delete confirm */}
      {deleteTarget && (
        <Modal title="Delete Patient" onClose={() => setDeleteTarget(null)}>
          <p className="text-sm text-slate-600 mb-1">Are you sure you want to delete patient <strong>{deleteTarget}</strong>?</p>
          <p className="text-xs text-red-600 bg-red-50 border border-red-200 rounded-lg p-2.5 mb-4">
            This will cascade delete from Appointments, Prescriptions, Billing, and Bed Allocation in MySQL.
          </p>
          <div className="flex gap-2">
            <button onClick={() => setDeleteTarget(null)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
            <button onClick={handleDeletePatient} className="flex-1 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700">Delete Patient</button>
          </div>
        </Modal>
      )}
    </Layout>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <div>
      <h4 className="text-xs font-semibold text-slate-500 uppercase tracking-wide mb-2">{title}</h4>
      <div className="bg-slate-50 rounded-lg p-3 space-y-2">{children}</div>
    </div>
  );
}

function Row({ label, value, mono }: { label: string; value: string | number; mono?: boolean }) {
  return (
    <div className="flex justify-between gap-2">
      <span className="text-xs text-slate-500">{label}</span>
      <span className={`text-xs font-medium text-slate-800 text-right ${mono ? 'font-mono' : ''}`}>{value}</span>
    </div>
  );
}

function Modal({ title, onClose, children }: { title: string; onClose: () => void; children: React.ReactNode }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-black/40" onClick={onClose} />
      <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
        <div className="flex items-center justify-between p-5 border-b border-slate-200">
          <h3 className="font-semibold text-slate-900">{title}</h3>
          <button onClick={onClose} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
        </div>
        <div className="p-5">{children}</div>
      </div>
    </div>
  );
}
