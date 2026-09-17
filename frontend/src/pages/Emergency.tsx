import { useState } from 'react';
import Layout from '../components/Layout';
import { Plus, X, AlertTriangle } from 'lucide-react';
import { emergencyCases } from '../data/mockData';

const priorityConfig: Record<string, { bg: string; text: string; border: string; dot: string }> = {
  Critical: { bg: 'bg-red-50', text: 'text-red-700', border: 'border-red-300', dot: 'bg-red-500' },
  High: { bg: 'bg-orange-50', text: 'text-orange-700', border: 'border-orange-300', dot: 'bg-orange-500' },
  Medium: { bg: 'bg-yellow-50', text: 'text-yellow-700', border: 'border-yellow-300', dot: 'bg-yellow-500' },
  Low: { bg: 'bg-slate-50', text: 'text-slate-600', border: 'border-slate-300', dot: 'bg-slate-400' },
};

const counts = [
  { label: 'Critical', count: 1, color: 'text-red-600', bg: 'bg-red-50 border-red-200' },
  { label: 'High', count: 2, color: 'text-orange-600', bg: 'bg-orange-50 border-orange-200' },
  { label: 'Medium', count: 1, color: 'text-yellow-600', bg: 'bg-yellow-50 border-yellow-200' },
  { label: 'Low', count: 1, color: 'text-slate-600', bg: 'bg-slate-50 border-slate-200' },
];

export default function Emergency() {
  const [showAdd, setShowAdd] = useState(false);

  return (
    <Layout title="Emergency" subtitle="Active emergency cases and triage management">
      <div className="p-6 space-y-5">
        {/* Alert banner */}
        <div className="flex items-center gap-3 bg-red-50 border border-red-200 rounded-xl px-5 py-3">
          <span className="w-2.5 h-2.5 rounded-full bg-red-500 animate-pulse shrink-0" />
          <AlertTriangle size={15} className="text-red-600 shrink-0" />
          <span className="text-sm font-medium text-red-700">1 Critical case requires immediate attention — EM-001 (Unknown Male, Chest Pain)</span>
        </div>

        {/* Priority summary */}
        <div className="grid grid-cols-4 gap-3">
          {counts.map(({ label, count, color, bg }) => (
            <div key={label} className={`rounded-xl border p-4 ${bg}`}>
              <div className={`text-3xl font-bold ${color}`}>{count}</div>
              <div className={`text-sm font-medium mt-0.5 ${color}`}>{label}</div>
            </div>
          ))}
        </div>

        <div className="flex items-center justify-between">
          <h3 className="font-semibold text-slate-800">Active Cases</h3>
          <button onClick={() => setShowAdd(true)} className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg bg-red-600 hover:bg-red-700 transition-colors">
            <Plus size={15} /> New Case
          </button>
        </div>

        {/* Cases table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Case ID', 'Patient', 'Priority', 'Assigned Doctor', 'Arrival', 'Status', 'Symptoms', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {emergencyCases.map(c => {
                  const cfg = priorityConfig[c.priority];
                  return (
                    <tr key={c.id} className={`transition-colors ${c.priority === 'Critical' ? 'bg-red-50/30' : 'hover:bg-slate-50/50'}`}>
                      <td className="px-4 py-3 font-mono text-xs text-slate-500">{c.id}</td>
                      <td className="px-4 py-3">
                        <div className="flex items-center gap-2">
                          {c.priority === 'Critical' && <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse shrink-0" />}
                          <span className="font-medium text-slate-800">{c.patient}</span>
                        </div>
                      </td>
                      <td className="px-4 py-3">
                        <span className={`text-xs px-2.5 py-1 rounded-full font-semibold border ${cfg.bg} ${cfg.text} ${cfg.border}`}>
                          {c.priority}
                        </span>
                      </td>
                      <td className="px-4 py-3 text-slate-600 text-xs">{c.doctor}</td>
                      <td className="px-4 py-3 font-mono text-xs text-slate-700">{c.arrival}</td>
                      <td className="px-4 py-3">
                        <span className="text-xs px-2 py-0.5 bg-blue-100 text-blue-700 rounded-full font-medium">{c.status}</span>
                      </td>
                      <td className="px-4 py-3 text-slate-500 text-xs max-w-[180px] truncate" title={c.symptoms}>{c.symptoms}</td>
                      <td className="px-4 py-3">
                        <button className="text-xs px-2.5 py-1 bg-teal-50 text-teal-700 rounded-lg hover:bg-teal-100 font-medium transition-colors">Update</button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-lg">
            <div className="flex items-center justify-between p-5 border-b border-red-200 bg-red-50">
              <h3 className="font-semibold text-red-900 flex items-center gap-2"><AlertTriangle size={16} className="text-red-600" />New Emergency Case</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-red-100 text-red-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              {['Patient Name', 'Priority', 'Assigned Doctor', 'Symptoms/Notes'].map(f => (
                <div key={f}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  {f === 'Symptoms/Notes'
                    ? <textarea className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500/20 resize-none h-20" />
                    : <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-red-500/20 focus:border-red-400" />
                  }
                </div>
              ))}
              <div className="flex gap-2 mt-4">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700">Admit Emergency Case</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
