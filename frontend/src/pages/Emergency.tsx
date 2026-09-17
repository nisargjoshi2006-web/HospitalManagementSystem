import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Plus, X, AlertTriangle, RefreshCw } from 'lucide-react';
import { emergencyCases as fallbackCases } from '../data/mockData';
import { getEmergency, addEmergency, getPatients, getDoctors } from '../api/api';

const priorityConfig: Record<string, { bg: string; text: string; border: string; dot: string }> = {
  Critical: { bg: 'bg-red-50', text: 'text-red-700', border: 'border-red-300', dot: 'bg-red-500' },
  High: { bg: 'bg-orange-50', text: 'text-orange-700', border: 'border-orange-300', dot: 'bg-orange-500' },
  Medium: { bg: 'bg-yellow-50', text: 'text-yellow-700', border: 'border-yellow-300', dot: 'bg-yellow-500' },
  Low: { bg: 'bg-slate-50', text: 'text-slate-600', border: 'border-slate-300', dot: 'bg-slate-400' },
};

export default function Emergency() {
  const [cases, setCases] = useState<any[]>([]);
  const [patients, setPatients] = useState<any[]>([]);
  const [doctors, setDoctors] = useState<any[]>([]);
  const [showAdd, setShowAdd] = useState(false);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    patientId: '',
    emergencyType: 'Severe Fever',
    priorityLevel: 'High',
    arrivalDate: new Date().toISOString().slice(0, 10),
    arrivalTime: '10:00',
    assignedDoctor: '',
    status: 'Admitted'
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [emData, ptsData, dctsData] = await Promise.all([
        getEmergency(),
        getPatients(),
        getDoctors()
      ]);
      if (Array.isArray(emData) && emData.length > 0) {
        setCases(emData);
      } else {
        setCases(fallbackCases);
      }
      if (Array.isArray(ptsData)) {
        setPatients(ptsData);
        if (ptsData.length > 0 && !form.patientId) {
          setForm(prev => ({ ...prev, patientId: String(ptsData[0].rawId || ptsData[0].id) }));
        }
      }
      if (Array.isArray(dctsData)) {
        setDoctors(dctsData);
      }
    } catch (err) {
      console.warn('Could not load live emergency cases:', err);
      setCases(fallbackCases);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAddCase = async () => {
    if (!form.patientId) {
      alert('Please select a patient');
      return;
    }
    try {
      await addEmergency({
        patientId: form.patientId,
        emergencyType: form.emergencyType,
        priorityLevel: form.priorityLevel,
        arrivalDate: form.arrivalDate,
        arrivalTime: form.arrivalTime.length === 5 ? `${form.arrivalTime}:00` : form.arrivalTime,
        assignedDoctor: form.assignedDoctor || null,
        status: form.status
      });
      setShowAdd(false);
      await loadData();
      alert('Emergency case admitted successfully in MySQL database!');
    } catch (err: any) {
      alert('Failed to admit case: ' + err.message);
    }
  };

  const criticalCount = cases.filter(c => c.priority === 'Critical').length;
  const highCount = cases.filter(c => c.priority === 'High').length;
  const mediumCount = cases.filter(c => c.priority === 'Medium').length;
  const lowCount = cases.filter(c => c.priority === 'Low').length;

  const counts = [
    { label: 'Critical', count: criticalCount, color: 'text-red-600', bg: 'bg-red-50 border-red-200' },
    { label: 'High', count: highCount, color: 'text-orange-600', bg: 'bg-orange-50 border-orange-200' },
    { label: 'Medium', count: mediumCount, color: 'text-yellow-600', bg: 'bg-yellow-50 border-yellow-200' },
    { label: 'Low', count: lowCount, color: 'text-slate-600', bg: 'bg-slate-50 border-slate-200' },
  ];

  return (
    <Layout title="Emergency Room" subtitle="Active ER triage admissions and doctor allocations in MySQL">
      <div className="p-6 space-y-5">
        {/* Alert banner */}
        {criticalCount > 0 && (
          <div className="flex items-center gap-3 bg-red-50 border border-red-200 rounded-xl px-5 py-3">
            <span className="w-2.5 h-2.5 rounded-full bg-red-500 animate-pulse shrink-0" />
            <AlertTriangle size={15} className="text-red-600 shrink-0" />
            <span className="text-sm font-medium text-red-700">
              {criticalCount} Critical case requires immediate attention!
            </span>
          </div>
        )}

        {/* Priority summary */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
          {counts.map(({ label, count, color, bg }) => (
            <div key={label} className={`rounded-xl border p-4 ${bg}`}>
              <div className={`text-3xl font-bold ${color}`}>{count}</div>
              <div className={`text-sm font-medium mt-0.5 ${color}`}>{label} Priority</div>
            </div>
          ))}
        </div>

        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <h3 className="font-semibold text-slate-800">Active Cases ({cases.length})</h3>
            <button
              onClick={loadData}
              disabled={loading}
              className="p-1.5 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600"
              title="Refresh"
            >
              <RefreshCw size={14} className={loading ? 'animate-spin text-teal-600' : ''} />
            </button>
          </div>
          <button
            onClick={() => setShowAdd(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg bg-red-600 hover:bg-red-700 transition-colors shadow-sm"
          >
            <Plus size={15} /> Admit Emergency Case
          </button>
        </div>

        {/* Cases table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">Emergency Room Patients</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Emergency table</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Case ID', 'Patient', 'Priority Level', 'Assigned Doctor', 'Arrival Time', 'Status', 'Symptoms'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {cases.map(c => {
                  const cfg = priorityConfig[c.priority] || priorityConfig['Medium'];
                  return (
                    <tr key={c.id} className="hover:bg-slate-50/50 transition-colors">
                      <td className="px-4 py-3 font-mono text-xs text-slate-500">{c.id}</td>
                      <td className="px-4 py-3 font-medium text-slate-800">{c.patient}</td>
                      <td className="px-4 py-3">
                        <span className={`inline-flex items-center gap-1.5 text-xs px-2.5 py-1 rounded-full font-semibold ${cfg.bg} ${cfg.text} border ${cfg.border}`}>
                          <span className={`w-1.5 h-1.5 rounded-full ${cfg.dot}`} />
                          {c.priority}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-slate-700 text-xs">{c.doctor}</td>
                      <td className="px-4 py-3 font-mono text-xs text-slate-600">{c.arrival}</td>
                      <td className="px-4 py-3">
                        <span className="text-xs px-2 py-0.5 rounded-md bg-slate-100 text-slate-700 font-medium">
                          {c.status}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-xs text-slate-600">{c.symptoms}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* New Case Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Admit Emergency Patient (Saves to MySQL)</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Patient *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.patientId}
                  onChange={e => setForm({ ...form, patientId: e.target.value })}
                >
                  <option value="">-- Choose Patient --</option>
                  {patients.map(p => (
                    <option key={p.id} value={p.rawId || p.id}>
                      {p.name} ({p.id})
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Triage Priority</label>
                  <select
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.priorityLevel}
                    onChange={e => setForm({ ...form, priorityLevel: e.target.value })}
                  >
                    <option value="Critical">Critical</option>
                    <option value="High">High</option>
                    <option value="Medium">Medium</option>
                    <option value="Low">Low</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Status</label>
                  <select
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.status}
                    onChange={e => setForm({ ...form, status: e.target.value })}
                  >
                    <option value="Admitted">Admitted</option>
                    <option value="In Treatment">In Treatment</option>
                    <option value="Stabilizing">Stabilizing</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Assigned Doctor</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.assignedDoctor}
                  onChange={e => setForm({ ...form, assignedDoctor: e.target.value })}
                >
                  <option value="">-- Unassigned --</option>
                  {doctors.map(d => (
                    <option key={d.id} value={d.rawId || d.id}>
                      {d.name} ({d.specialization})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Symptoms / Emergency Details *</label>
                <input
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  placeholder="e.g. Acute chest pain, shortness of breath"
                  value={form.emergencyType}
                  onChange={e => setForm({ ...form, emergencyType: e.target.value })}
                />
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleAddCase} className="flex-1 py-2 text-sm font-medium text-white rounded-lg bg-red-600 hover:bg-red-700">Admit Patient</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
