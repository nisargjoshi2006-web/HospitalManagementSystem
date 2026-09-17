import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, RefreshCw, ShieldCheck } from 'lucide-react';
import { auditLogs as fallbackLogs } from '../data/mockData';
import { getAuditLogs } from '../api/api';

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
  const [logs, setLogs] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [severityFilter, setSeverityFilter] = useState('All');
  const [loading, setLoading] = useState(false);

  const loadData = async () => {
    setLoading(true);
    try {
      const data = await getAuditLogs();
      if (Array.isArray(data) && data.length > 0) {
        setLogs(data);
      } else {
        setLogs(fallbackLogs);
      }
    } catch (err) {
      console.warn('Could not load audit logs from API:', err);
      setLogs(fallbackLogs);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const filtered = logs.filter(l => {
    const matchSearch = (l.description || '').toLowerCase().includes(search.toLowerCase()) ||
      (l.user || '').includes(search) ||
      (l.action || '').toLowerCase().includes(search.toLowerCase());
    const matchSev = severityFilter === 'All' || l.severity === severityFilter;
    return matchSearch && matchSev;
  });

  return (
    <Layout title="Audit Logs & Traceability" subtitle="Automated MySQL trigger activity logs and security compliance trails">
      <div className="p-6 space-y-5">
        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search audit trail..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
            />
          </div>
          <div className="flex gap-1.5 items-center">
            {['All', 'Info', 'Warning', 'Critical'].map(s => (
              <button
                key={s}
                onClick={() => setSeverityFilter(s)}
                className={`px-3 py-1.5 text-xs font-medium rounded-lg border transition-colors ${severityFilter === s ? 'bg-slate-900 text-white border-slate-900' : 'bg-white text-slate-600 border-slate-200 hover:bg-slate-50'}`}
              >
                {s}
              </button>
            ))}
            <button
              onClick={loadData}
              disabled={loading}
              className="p-1.5 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600 ml-1"
              title="Refresh"
            >
              <RefreshCw size={14} className={loading ? 'animate-spin text-teal-600' : ''} />
            </button>
          </div>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">Audit Trail ({filtered.length} entries)</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium flex items-center gap-1">
              <ShieldCheck size={13} /> Automated trigger logging from MySQL Audit_Logs
            </span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Log ID', 'Timestamp', 'User', 'Action Type', 'Record ID', 'Description', 'Severity'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(l => (
                  <tr key={l.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{l.id}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{l.timestamp}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-700 font-medium">{l.user}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-md font-semibold ${actionStyle[l.action] || 'bg-slate-100 text-slate-700'}`}>
                        {l.action}
                      </span>
                    </td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{l.recordId}</td>
                    <td className="px-4 py-3 text-slate-700 text-xs">{l.description}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${severityStyle[l.severity] || 'bg-blue-100 text-blue-700'}`}>
                        {l.severity}
                      </span>
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
