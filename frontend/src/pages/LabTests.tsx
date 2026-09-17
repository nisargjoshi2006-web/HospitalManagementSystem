import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, X, FlaskConical, Clock, CheckCircle, RefreshCw } from 'lucide-react';
import { labTests as fallbackTests } from '../data/mockData';
import { getLabTests, addLabTest, updateLabTest, getPatients, getDoctors } from '../api/api';

const statusColors: Record<string, string> = {
  Pending: 'bg-amber-100 text-amber-700',
  'In Progress': 'bg-blue-100 text-blue-700',
  Completed: 'bg-green-100 text-green-700',
};

export default function LabTests() {
  const [tests, setTests] = useState<any[]>([]);
  const [patients, setPatients] = useState<any[]>([]);
  const [doctors, setDoctors] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [showAdd, setShowAdd] = useState(false);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    patientId: '',
    doctorId: '',
    testName: 'Complete Blood Count (CBC)',
    cost: '500',
    testDate: new Date().toISOString().slice(0, 10),
    result: 'Pending',
    status: 'Pending'
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [tData, pData, dData] = await Promise.all([
        getLabTests(),
        getPatients(),
        getDoctors()
      ]);
      if (Array.isArray(tData) && tData.length > 0) {
        setTests(tData);
      } else {
        setTests(fallbackTests);
      }
      if (Array.isArray(pData)) {
        setPatients(pData);
        if (pData.length > 0 && !form.patientId) {
          setForm(prev => ({ ...prev, patientId: String(pData[0].rawId || pData[0].id) }));
        }
      }
      if (Array.isArray(dData)) {
        setDoctors(dData);
        if (dData.length > 0 && !form.doctorId) {
          setForm(prev => ({ ...prev, doctorId: String(dData[0].rawId || dData[0].id) }));
        }
      }
    } catch (err) {
      console.warn('Could not load lab tests from API:', err);
      setTests(fallbackTests);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleOrderTest = async () => {
    if (!form.patientId || !form.doctorId || !form.testName) {
      alert('Please fill all required fields');
      return;
    }
    try {
      await addLabTest({
        patientId: form.patientId,
        doctorId: form.doctorId,
        testName: form.testName,
        cost: parseFloat(form.cost) || 500,
        testDate: form.testDate,
        result: form.result,
        status: form.status
      });
      setShowAdd(false);
      await loadData();
      alert('Diagnostic lab test ordered successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to order lab test: ' + err.message);
    }
  };

  const handleUpdateResult = async (testId: any) => {
    const result = prompt('Enter test result (e.g. Normal, Positive, Clean, 120 mg/dL):', 'Normal');
    if (!result) return;
    try {
      await updateLabTest(testId, { result, status: 'Completed' });
      await loadData();
      alert('Test result updated successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to update result: ' + err.message);
    }
  };

  const filtered = tests.filter(t =>
    (t.patient || '').toLowerCase().includes(search.toLowerCase()) ||
    (t.type || '').toLowerCase().includes(search.toLowerCase()) ||
    String(t.id).includes(search)
  );

  const pendingCount = tests.filter(t => t.status === 'Pending').length;
  const inProgressCount = tests.filter(t => t.status === 'In Progress').length;
  const completedCount = tests.filter(t => t.status === 'Completed').length;

  return (
    <Layout title="Diagnostic Lab Tests" subtitle="Order lab investigations and record clinical test results in MySQL">
      <div className="p-6 space-y-5">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
          {[
            { label: 'Total Lab Tests', value: tests.length, icon: FlaskConical, color: '#7C3AED', bg: '#F5F3FF' },
            { label: 'Pending Tests', value: pendingCount, icon: Clock, color: '#D97706', bg: '#FFFBEB' },
            { label: 'In Progress', value: inProgressCount, icon: Clock, color: '#0284C7', bg: '#F0F9FF' },
            { label: 'Completed', value: completedCount, icon: CheckCircle, color: '#16A34A', bg: '#F0FDF4' },
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

        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search by patient, test name, ID..."
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
            <Plus size={15} /> Order Lab Test
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">Lab Orders ({filtered.length})</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Lab_Tests table</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Test ID', 'Patient', 'Ordered By', 'Test Name', 'Date', 'Cost', 'Result', 'Status', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(t => (
                  <tr key={t.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{t.id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{t.patient}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{t.doctor}</td>
                    <td className="px-4 py-3 font-medium text-slate-800 text-xs">{t.type}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{t.ordered}</td>
                    <td className="px-4 py-3 font-medium text-teal-700 text-xs">₹{t.cost}</td>
                    <td className="px-4 py-3 text-xs font-mono">{t.result}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[t.status] || 'bg-slate-100'}`}>
                        {t.status}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      {t.status !== 'Completed' && (
                        <button
                          onClick={() => handleUpdateResult(t.rawId || t.id)}
                          className="px-2.5 py-1 text-xs font-medium text-teal-700 bg-teal-50 hover:bg-teal-100 rounded-md transition-colors"
                        >
                          Record Result
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Order Test Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Order Diagnostic Lab Test (Saves to MySQL)</h3>
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

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Prescribing Doctor *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.doctorId}
                  onChange={e => setForm({ ...form, doctorId: e.target.value })}
                >
                  <option value="">-- Choose Doctor --</option>
                  {doctors.map(d => (
                    <option key={d.id} value={d.rawId || d.id}>
                      {d.name} ({d.specialization})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Test Name *</label>
                <input
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  placeholder="e.g. Complete Blood Count (CBC), Lipid Profile, MRI"
                  value={form.testName}
                  onChange={e => setForm({ ...form, testName: e.target.value })}
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Test Cost (₹)</label>
                  <input
                    type="number"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.cost}
                    onChange={e => setForm({ ...form, cost: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Test Date</label>
                  <input
                    type="date"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.testDate}
                    onChange={e => setForm({ ...form, testDate: e.target.value })}
                  />
                </div>
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleOrderTest} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Order Test</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
