import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Plus, X, RefreshCw, CheckCircle2, AlertCircle } from 'lucide-react';
import { getBeds, allocateBed, dischargeBed, getPatients } from '../api/api';

type Ward = 'All' | 'ICU' | 'Private' | 'General';

export default function BedAllocation() {
  const [allocations, setAllocations] = useState<any[]>([]);
  const [patients, setPatients] = useState<any[]>([]);
  const [selectedWard, setSelectedWard] = useState<Ward>('All');
  const [selectedBed, setSelectedBed] = useState<any | null>(null);
  const [showAdmit, setShowAdmit] = useState(false);
  const [loading, setLoading] = useState(false);

  const [admitForm, setAdmitForm] = useState({
    patientId: '',
    wardType: 'General Ward',
    bedNumber: '101',
    dailyCharge: '1500',
    admitDate: new Date().toISOString().slice(0, 10)
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [bedsData, ptsData] = await Promise.all([getBeds(), getPatients()]);
      if (Array.isArray(bedsData)) setAllocations(bedsData);
      if (Array.isArray(ptsData)) {
        setPatients(ptsData);
        if (ptsData.length > 0 && !admitForm.patientId) {
          setAdmitForm(prev => ({ ...prev, patientId: String(ptsData[0].rawId || ptsData[0].id) }));
        }
      }
    } catch (err) {
      console.warn('Could not load beds from API:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAllocate = async () => {
    if (!admitForm.patientId || !admitForm.bedNumber) {
      alert('Please select patient and bed number');
      return;
    }
    try {
      await allocateBed({
        patientId: admitForm.patientId,
        wardType: admitForm.wardType,
        bedNumber: admitForm.bedNumber,
        admitDate: admitForm.admitDate,
        dailyCharge: parseFloat(admitForm.dailyCharge) || 1500
      });
      setShowAdmit(false);
      await loadData();
      alert('Bed allocated successfully in MySQL database!');
    } catch (err: any) {
      alert('Allocation Error: ' + err.message);
    }
  };

  const handleDischarge = async (allocationId: number) => {
    if (!confirm('Are you sure you want to discharge this patient?')) return;
    try {
      await dischargeBed(allocationId, new Date().toISOString().slice(0, 10));
      setSelectedBed(null);
      await loadData();
      alert('Patient discharged successfully in MySQL!');
    } catch (err: any) {
      alert('Discharge Error: ' + err.message);
    }
  };

  const occupiedCount = allocations.filter(b => b.status === 'Occupied').length;
  const dischargedCount = allocations.filter(b => b.status === 'Discharged').length;

  const filtered = allocations.filter(b => {
    if (selectedWard === 'All') return true;
    return (b.ward || '').toLowerCase().includes(selectedWard.toLowerCase());
  });

  return (
    <Layout title="Bed Allocation" subtitle="Hospital inpatient admissions and ward management">
      <div className="p-6 space-y-5">
        {/* KPI Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Currently Occupied</div>
            <div className="text-2xl font-bold text-amber-600 mt-1">{occupiedCount} Beds</div>
            <div className="text-xs text-slate-400 mt-0.5">Active inpatients</div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Discharged History</div>
            <div className="text-2xl font-bold text-teal-600 mt-1">{dischargedCount} Patients</div>
            <div className="text-xs text-slate-400 mt-0.5">Completed stays</div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Total Bed Allocations</div>
            <div className="text-2xl font-bold text-slate-900 mt-1">{allocations.length} Records</div>
            <div className="text-xs text-slate-400 mt-0.5">Logged in MySQL</div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Database Triggers</div>
            <div className="text-sm font-semibold text-teal-700 mt-2 flex items-center gap-1">
              <CheckCircle2 size={16} /> Single Occupancy Enforced
            </div>
            <div className="text-[11px] text-slate-400">trg_CheckBedAllocationInsert</div>
          </div>
        </div>

        {/* Toolbar */}
        <div className="flex items-center justify-between flex-wrap gap-3">
          <div className="flex items-center gap-2">
            {(['All', 'ICU', 'Private', 'General'] as Ward[]).map(w => (
              <button
                key={w}
                onClick={() => setSelectedWard(w)}
                className={`px-3 py-1.5 rounded-lg text-xs font-medium transition-colors ${selectedWard === w ? 'bg-teal-700 text-white' : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'}`}
              >
                {w === 'All' ? 'All Wards' : `${w} Ward`}
              </button>
            ))}
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
            onClick={() => setShowAdmit(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg shadow-sm"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Admit Patient to Bed
          </button>
        </div>

        {/* Table of Allocations */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">Live Bed Allocations ({filtered.length})</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Bed_Allocation</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Alloc ID', 'Patient', 'Ward', 'Bed Number', 'Admit Date', 'Discharge Date', 'Daily Charge', 'Status', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map((b: any) => (
                  <tr key={b.allocationId || b.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">#{b.allocationId}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{b.patient}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{b.ward}</td>
                    <td className="px-4 py-3 font-mono text-xs font-semibold text-slate-700">{b.bedNumber}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{b.admitDate}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{b.dischargeDate || '—'}</td>
                    <td className="px-4 py-3 font-medium text-teal-700 text-xs">₹{b.dailyCharge}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${b.status === 'Occupied' ? 'bg-amber-100 text-amber-700' : 'bg-green-100 text-green-700'}`}>
                        {b.status}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      {b.status === 'Occupied' && (
                        <button
                          onClick={() => handleDischarge(b.allocationId)}
                          className="px-2.5 py-1 text-xs font-medium text-red-600 bg-red-50 hover:bg-red-100 rounded-md transition-colors"
                        >
                          Discharge
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

      {/* Admit Modal */}
      {showAdmit && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdmit(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Admit Patient to Bed (Saves to MySQL)</h3>
              <button onClick={() => setShowAdmit(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Patient *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={admitForm.patientId}
                  onChange={e => setAdmitForm({ ...admitForm, patientId: e.target.value })}
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
                <label className="block text-xs font-medium text-slate-700 mb-1">Ward Type *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={admitForm.wardType}
                  onChange={e => {
                    const w = e.target.value;
                    setAdmitForm({
                      ...admitForm,
                      wardType: w,
                      dailyCharge: w.includes('ICU') ? '5000' : w.includes('Private') ? '3000' : '1500'
                    });
                  }}
                >
                  <option value="General Ward">General Ward (₹1,500/day)</option>
                  <option value="Private AC Room">Private AC Room (₹3,000/day)</option>
                  <option value="ICU">ICU (₹5,000/day)</option>
                </select>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Bed Number *</label>
                  <input
                    type="text"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    placeholder="e.g. 105 or PVT-206"
                    value={admitForm.bedNumber}
                    onChange={e => setAdmitForm({ ...admitForm, bedNumber: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Daily Charge (₹)</label>
                  <input
                    type="number"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={admitForm.dailyCharge}
                    onChange={e => setAdmitForm({ ...admitForm, dailyCharge: e.target.value })}
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Admit Date</label>
                <input
                  type="date"
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={admitForm.admitDate}
                  onChange={e => setAdmitForm({ ...admitForm, admitDate: e.target.value })}
                />
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdmit(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleAllocate} className="flex-1 py-2 text-sm font-medium text-white rounded-lg transition-opacity hover:opacity-90" style={{ background: '#0F766E' }}>Confirm Admission</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
