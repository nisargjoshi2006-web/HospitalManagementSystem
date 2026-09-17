import { useState } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, Eye, Pencil, Trash2, X } from 'lucide-react';
import { prescriptions } from '../data/mockData';

export default function Prescriptions() {
  const [search, setSearch] = useState('');
  const [selected, setSelected] = useState<typeof prescriptions[0] | null>(null);
  const [showAdd, setShowAdd] = useState(false);

  const filtered = prescriptions.filter(p =>
    p.patient.toLowerCase().includes(search.toLowerCase()) ||
    p.id.includes(search) ||
    p.doctor.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <Layout title="Prescriptions" subtitle="Manage medication prescriptions">
      <div className="p-6 space-y-5">
        <div className="flex items-center gap-3">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="text" placeholder="Search prescriptions..." value={search} onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white" />
          </div>
          <button onClick={() => setShowAdd(true)} className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto" style={{ background: '#0F766E' }}>
            <Plus size={15} /> Add Prescription
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
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
                    <td className="px-4 py-3 text-slate-500 text-xs max-w-[180px] truncate" title={p.medicine}>{p.medicine}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{p.nextVisit}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{p.date}</td>
                    <td className="px-4 py-3">
                      <div className="flex gap-1">
                        <button onClick={() => setSelected(p)} className="p-1.5 rounded-lg hover:bg-teal-50 text-slate-400 hover:text-teal-600 transition-colors"><Eye size={13} /></button>
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

      {/* Prescription detail view */}
      {selected && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setSelected(null)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Prescription Detail</h3>
              <button onClick={() => setSelected(null)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            {/* Clinical document style */}
            <div className="p-6">
              <div className="border-2 border-teal-600 rounded-xl overflow-hidden">
                <div className="bg-teal-600 text-white px-5 py-3">
                  <div className="text-base font-bold">CarePoint Hospital</div>
                  <div className="text-teal-100 text-xs">Medical Prescription</div>
                </div>
                <div className="p-5 space-y-4">
                  <div className="grid grid-cols-2 gap-x-4 gap-y-2 text-sm pb-3 border-b border-slate-200">
                    <div><span className="text-slate-500 text-xs">Prescription ID</span><div className="font-mono font-semibold text-slate-800">{selected.id}</div></div>
                    <div><span className="text-slate-500 text-xs">Date</span><div className="font-semibold text-slate-800">{selected.date}</div></div>
                    <div><span className="text-slate-500 text-xs">Patient</span><div className="font-semibold text-slate-800">{selected.patient}</div></div>
                    <div><span className="text-slate-500 text-xs">Physician</span><div className="font-semibold text-slate-800">{selected.doctor}</div></div>
                  </div>
                  <div>
                    <div className="text-xs text-slate-500 mb-1">Diagnosis</div>
                    <div className="font-semibold text-slate-900 text-sm">{selected.diagnosis}</div>
                  </div>
                  <div>
                    <div className="text-xs text-slate-500 mb-1">Prescribed Medication</div>
                    <div className="bg-slate-50 rounded-lg p-3 text-sm text-slate-800 border border-slate-200">{selected.medicine}</div>
                  </div>
                  <div className="grid grid-cols-2 gap-4">
                    <div><span className="text-xs text-slate-500">Next Visit</span><div className="font-semibold text-slate-800 text-sm">{selected.nextVisit}</div></div>
                    <div><span className="text-xs text-slate-500">Remarks</span><div className="font-semibold text-slate-800 text-sm">{selected.remarks}</div></div>
                  </div>
                  <div className="pt-3 border-t border-slate-200 flex justify-between items-end">
                    <div className="text-xs text-slate-400">Authorized signature</div>
                    <div className="text-xs text-teal-600 font-medium">{selected.doctor}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}

      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-lg">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Add Prescription</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 grid grid-cols-2 gap-3">
              {['Patient', 'Doctor', 'Diagnosis', 'Medication', 'Next Visit Date', 'Remarks'].map(f => (
                <div key={f} className={f === 'Medication' || f === 'Remarks' ? 'col-span-2' : ''}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  {f === 'Medication' || f === 'Remarks'
                    ? <textarea className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 resize-none h-16" placeholder={`Enter ${f.toLowerCase()}`} />
                    : <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400" />
                  }
                </div>
              ))}
              <div className="col-span-2 flex gap-2 mt-2">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Create Prescription</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
