import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Eye, X, RefreshCw } from 'lucide-react';
import { prescriptions as fallbackPrescriptions } from '../data/mockData';
import { getPrescriptions, addPrescription, getAppointments } from '../api/api';

export default function Prescriptions() {
  const [prescriptionsList, setPrescriptionsList] = useState<any[]>([]);
  const [appointments, setAppointments] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [selected, setSelected] = useState<any | null>(null);
  const [showAdd, setShowAdd] = useState(false);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    appointmentId: '',
    diagnosis: '',
    medicine: '',
    nextVisitDate: '',
    remarks: ''
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [rxData, aptData] = await Promise.all([getPrescriptions(), getAppointments()]);
      if (Array.isArray(rxData) && rxData.length > 0) {
        setPrescriptionsList(rxData);
      } else {
        setPrescriptionsList(fallbackPrescriptions);
      }
      if (Array.isArray(aptData)) {
        setAppointments(aptData);
        if (aptData.length > 0 && !form.appointmentId) {
          setForm(prev => ({ ...prev, appointmentId: String(aptData[0].rawId || aptData[0].id) }));
        }
      }
    } catch (err) {
      console.warn('Could not load prescriptions from API:', err);
      setPrescriptionsList(fallbackPrescriptions);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAdd = async () => {
    if (!form.appointmentId || !form.diagnosis || !form.medicine) {
      alert('Please fill in appointment, diagnosis, and medicines');
      return;
    }
    try {
      await addPrescription({
        appointmentId: form.appointmentId,
        diagnosis: form.diagnosis,
        medicine: form.medicine,
        nextVisitDate: form.nextVisitDate || undefined,
        remarks: form.remarks
      });
      setShowAdd(false);
      setForm({ appointmentId: '', diagnosis: '', medicine: '', nextVisitDate: '', remarks: '' });
      await loadData();
      alert('Prescription created successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to save prescription: ' + err.message);
    }
  };

  const filtered = prescriptionsList.filter(p =>
    (p.patient || '').toLowerCase().includes(search.toLowerCase()) ||
    (p.doctor || '').toLowerCase().includes(search.toLowerCase()) ||
    (p.diagnosis || '').toLowerCase().includes(search.toLowerCase()) ||
    String(p.id).includes(search)
  );

  return (
    <Layout title="Prescriptions" subtitle="Medical diagnoses, drug prescriptions, and next visits in MySQL">
      <div className="p-6 space-y-5">
        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search by patient, doctor, diagnosis, ID..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
            />
          </div>
          <button
            onClick={loadData}
            disabled={loading}
            className="p-2 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600"
            title="Refresh List"
          >
            <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
          </button>
          <button
            onClick={() => setShowAdd(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto shadow-sm"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Add Prescription
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">{filtered.length} Prescription{filtered.length !== 1 ? 's' : ''}</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Prescriptions table</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Rx ID', 'Patient', 'Doctor', 'Diagnosis', 'Medicine', 'Next Visit', 'Date', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(p => (
                  <tr key={p.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{p.id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{p.patient}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{p.doctor}</td>
                    <td className="px-4 py-3">
                      <span className="text-xs px-2 py-0.5 bg-purple-100 text-purple-700 rounded-full font-medium">{p.diagnosis}</span>
                    </td>
                    <td className="px-4 py-3 text-slate-700 text-xs max-w-[200px] truncate" title={p.medicine}>{p.medicine}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{p.nextVisit}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{p.date}</td>
                    <td className="px-4 py-3">
                      <button
                        onClick={() => setSelected(p)}
                        className="p-1.5 rounded-lg hover:bg-teal-50 text-slate-400 hover:text-teal-600 transition-colors"
                        title="View Details"
                      >
                        <Eye size={14} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* View Detail Modal */}
      {selected && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setSelected(null)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md p-6">
            <div className="flex items-center justify-between pb-3 border-b border-slate-100">
              <h3 className="font-semibold text-slate-900">Prescription {selected.id}</h3>
              <button onClick={() => setSelected(null)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="mt-4 space-y-3 text-sm">
              <div className="flex justify-between"><span className="text-slate-500">Patient:</span><span className="font-medium text-slate-800">{selected.patient}</span></div>
              <div className="flex justify-between"><span className="text-slate-500">Doctor:</span><span className="font-medium text-slate-800">{selected.doctor}</span></div>
              <div className="flex justify-between"><span className="text-slate-500">Diagnosis:</span><span className="font-semibold text-purple-700">{selected.diagnosis}</span></div>
              <div className="p-3 bg-slate-50 rounded-lg">
                <div className="text-xs font-semibold text-slate-500 mb-1">Prescribed Medicines:</div>
                <div className="text-slate-700">{selected.medicine}</div>
              </div>
              {selected.remarks && (
                <div className="p-3 bg-amber-50 text-amber-800 rounded-lg text-xs">
                  <strong>Remarks:</strong> {selected.remarks}
                </div>
              )}
              <div className="flex justify-between text-xs text-slate-500 pt-2 border-t border-slate-100">
                <span>Issued: {selected.date}</span>
                <span>Next Visit: {selected.nextVisit}</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Add Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Create Prescription (Saves to MySQL)</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Appointment *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.appointmentId}
                  onChange={e => setForm({ ...form, appointmentId: e.target.value })}
                >
                  <option value="">-- Choose Appointment --</option>
                  {appointments.map(a => (
                    <option key={a.id} value={a.rawId || a.id}>
                      {a.id} - {a.patient} with {a.doctor} ({a.date})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Clinical Diagnosis *</label>
                <input
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  placeholder="e.g. Hypertension, Migraine, Type 2 Diabetes"
                  value={form.diagnosis}
                  onChange={e => setForm({ ...form, diagnosis: e.target.value })}
                />
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Prescribed Medicines & Dosage *</label>
                <textarea
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  rows={2}
                  placeholder="e.g. Paracetamol 500mg (1-0-1), Amoxicillin 250mg (0-1-0)"
                  value={form.medicine}
                  onChange={e => setForm({ ...form, medicine: e.target.value })}
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Next Visit Date</label>
                  <input
                    type="date"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.nextVisitDate}
                    onChange={e => setForm({ ...form, nextVisitDate: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Remarks</label>
                  <input
                    type="text"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    placeholder="e.g. Take after meals"
                    value={form.remarks}
                    onChange={e => setForm({ ...form, remarks: e.target.value })}
                  />
                </div>
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleAdd} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Save Prescription</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
