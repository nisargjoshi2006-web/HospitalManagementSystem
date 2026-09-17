import { useState } from 'react';
import Layout from '../components/Layout';
import { Search } from 'lucide-react';
import { auditLogs } from '../data/mockData';

const severityStyle: Record<string, string> = {
  Info: 'bg-blue-100 text-blue-700',
  Warning: 'bg-amber-100 text-amber-700',
  Critical: 'bg-red-100 text-red-700',
};

const actionStyle: Record<string, string> = {
  CREATE: 'bg-green-100 text-green-700',
  UPDATE: 'bg-blue-100 text-blue-700',
  DELETE: 'bg-red-100 text-red-700',
  ALERT: 'bg-red-100 text-red-700',
  LOGIN: 'bg-teal-100 text-teal-700',
  BACKUP: 'bg-slate-100 text-slate-700',
};

export default function AuditLogs() {
  const [search, setSearch] = useState('');
  const [severityFilter, setSeverityFilter] = useState('All');

  const filtered = auditLogs.filter(l => {
    const matchSearch = l.description.toLowerCase().includes(search.toLowerCase()) || l.user.includes(search) || l.module.toLowerCase().includes(search.toLowerCase());
    const matchSev = severityFilter === 'All' || l.severity === severityFilter;
    return matchSearch && matchSev;
  });

  return (
    <Layout title="Audit Logs" subtitle="System activity and access logs (Admin only)">
      <div className="p-6 space-y-5">
        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input type="text" placeholder="Search logs..." value={search} onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white" />
          </div>
          <div className="flex gap-1.5">
            {['All', 'Info', 'Warning', 'Critical'].map(s => (
              <button
                key={s}
                onClick={() => setSeverityFilter(s)}
                className={`px-3 py-1.5 text-xs font-medium rounded-lg border transition-colors ${severityFilter === s ? 'bg-slate-900 text-white border-slate-900' : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'}`}
              >
                {s}
              </button>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Timestamp', 'User', 'Action', 'Module', 'Record ID', 'Description', 'Severity'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(l => (
                  <tr key={l.id} className={`hover:bg-slate-50/50 transition-colors ${l.severity === 'Critical' ? 'bg-red-50/20' : ''}`}>
                    <td className="px-4 py-3 font-mono text-xs text-slate-500 whitespace-nowrap">{l.timestamp}</td>
                    <td className="px-4 py-3">
                      <div className="flex items-center gap-1.5">
                        <div className="w-5 h-5 rounded-full bg-slate-100 text-slate-600 flex items-center justify-center text-xs font-bold">{l.user.charAt(0).toUpperCase()}</div>
                        <span className="text-xs font-mono text-slate-700">{l.user}</span>
                      </div>
                    </td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-semibold font-mono ${actionStyle[l.action] ?? 'bg-slate-100 text-slate-600'}`}>{l.action}</span>
                    </td>
                    <td className="px-4 py-3 text-slate-600 text-xs font-medium">{l.module}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-400">{l.recordId}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs max-w-[280px]">{l.description}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${severityStyle[l.severity] ?? 'bg-slate-100 text-slate-600'}`}>{l.severity}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </Layout>
  );
}
