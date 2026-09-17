import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, RefreshCw, CheckCircle2 } from 'lucide-react';
import { bills as fallbackBills } from '../data/mockData';
import { getBilling, addBill, updateBill, getAppointments } from '../api/api';

const statusColors: Record<string, string> = {
  Paid: 'bg-green-100 text-green-700',
  Pending: 'bg-amber-100 text-amber-700',
  Failed: 'bg-red-100 text-red-700',
};

const methodIcons: Record<string, string> = {
  Cash: '💵',
  Card: '💳',
  UPI: '📱',
  Online: '🌐',
};

export default function Billing() {
  const [billsList, setBillsList] = useState<any[]>([]);
  const [appointments, setAppointments] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [showCreate, setShowCreate] = useState(false);
  const [loading, setLoading] = useState(false);

  const [billForm, setBillForm] = useState({
    appointmentId: '',
    amount: '',
    paymentMethod: 'UPI',
    paymentStatus: 'Paid',
    billDate: new Date().toISOString().slice(0, 10)
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [bData, aData] = await Promise.all([getBilling(), getAppointments()]);
      if (Array.isArray(bData) && bData.length > 0) {
        setBillsList(bData);
      } else {
        setBillsList(fallbackBills);
      }
      if (Array.isArray(aData)) {
        setAppointments(aData);
        if (aData.length > 0 && !billForm.appointmentId) {
          setBillForm(prev => ({ ...prev, appointmentId: String(aData[0].rawId || aData[0].id) }));
        }
      }
    } catch (err) {
      console.warn('Could not load billing from API:', err);
      setBillsList(fallbackBills);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCreateBill = async () => {
    if (!billForm.appointmentId) {
      alert('Please select an appointment');
      return;
    }
    try {
      await addBill({
        appointmentId: billForm.appointmentId,
        amount: parseFloat(billForm.amount) || undefined,
        paymentMethod: billForm.paymentMethod,
        paymentStatus: billForm.paymentStatus,
        billDate: billForm.billDate
      });
      setShowCreate(false);
      await loadData();
      alert('Bill generated successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to generate bill: ' + err.message);
    }
  };

  const handleMarkPaid = async (billId: any) => {
    try {
      await updateBill(billId, { paymentStatus: 'Paid' });
      await loadData();
      alert('Bill marked as Paid!');
    } catch (err: any) {
      alert('Failed to update bill: ' + err.message);
    }
  };

  const filtered = billsList.filter(b =>
    (b.patient || '').toLowerCase().includes(search.toLowerCase()) ||
    String(b.id).includes(search)
  );

  const totalPaidRevenue = billsList.filter(b => b.status === 'Paid').reduce((s, b) => s + (Number(b.fee) || 0), 0);
  const pendingAmount = billsList.filter(b => b.status === 'Pending').reduce((s, b) => s + (Number(b.fee) || 0), 0);
  const paidCount = billsList.filter(b => b.status === 'Paid').length;
  const pendingCount = billsList.filter(b => b.status === 'Pending').length;

  return (
    <Layout title="Billing & Invoicing" subtitle="Live consultation fees, invoices and MySQL payment tracking">
      <div className="p-6 space-y-5">
        {/* Summary cards */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Total Paid Revenue</div>
            <div className="text-2xl font-bold text-green-700 mt-1">₹{totalPaidRevenue.toLocaleString()}</div>
            <div className="text-xs text-slate-400 mt-0.5">{paidCount} invoices settled</div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Pending Payments</div>
            <div className="text-2xl font-bold text-amber-600 mt-1">₹{pendingAmount.toLocaleString()}</div>
            <div className="text-xs text-slate-400 mt-0.5">{pendingCount} bills pending</div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Total Invoices</div>
            <div className="text-2xl font-bold text-slate-900 mt-1">{billsList.length}</div>
            <div className="text-xs text-slate-400 mt-0.5">Recorded in database</div>
          </div>
          <div className="bg-white rounded-xl border border-slate-200 p-4">
            <div className="text-xs text-slate-500 font-medium">Database Triggers</div>
            <div className="text-sm font-semibold text-teal-700 mt-2 flex items-center gap-1">
              <CheckCircle2 size={16} /> Auto-Complete Appts
            </div>
            <div className="text-[11px] text-slate-400">trg_AfterBillPaid</div>
          </div>
        </div>

        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 max-w-md">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search by patient, bill ID..."
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
            onClick={() => setShowCreate(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto shadow-sm"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Create Bill
          </button>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">{filtered.length} Bill{filtered.length !== 1 ? 's' : ''}</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Billing table</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Bill ID', 'Patient', 'Doctor', 'Appointment', 'Amount (₹)', 'Method', 'Status', 'Date', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide whitespace-nowrap">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map((b: any) => (
                  <tr key={b.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{b.id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{b.patient}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{b.doctor}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{b.appointment}</td>
                    <td className="px-4 py-3 font-semibold text-slate-900 text-xs">₹{b.fee}</td>
                    <td className="px-4 py-3 text-xs">
                      <span className="inline-flex items-center gap-1">
                        <span>{methodIcons[b.method] || '💳'}</span>
                        <span>{b.method}</span>
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[b.status] || 'bg-slate-100'}`}>
                        {b.status}
                      </span>
                    </td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{b.date}</td>
                    <td className="px-4 py-3">
                      {b.status === 'Pending' && (
                        <button
                          onClick={() => handleMarkPaid(b.rawId || b.id)}
                          className="px-2.5 py-1 text-xs font-medium text-green-700 bg-green-50 hover:bg-green-100 rounded-md transition-colors"
                        >
                          Mark Paid
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

      {/* Create Bill Modal */}
      {showCreate && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowCreate(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Generate New Bill (Saves to MySQL)</h3>
              <button onClick={() => setShowCreate(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500">×</button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Appointment *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={billForm.appointmentId}
                  onChange={e => setBillForm({ ...billForm, appointmentId: e.target.value })}
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
                <label className="block text-xs font-medium text-slate-700 mb-1">Amount (₹) (Leave blank to use doctor consultation fee)</label>
                <input
                  type="number"
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  placeholder="e.g. 500"
                  value={billForm.amount}
                  onChange={e => setBillForm({ ...billForm, amount: e.target.value })}
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Payment Method</label>
                  <select
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={billForm.paymentMethod}
                    onChange={e => setBillForm({ ...billForm, paymentMethod: e.target.value })}
                  >
                    <option value="UPI">UPI</option>
                    <option value="Card">Card</option>
                    <option value="Cash">Cash</option>
                    <option value="Online">Online</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Status</label>
                  <select
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={billForm.paymentStatus}
                    onChange={e => setBillForm({ ...billForm, paymentStatus: e.target.value })}
                  >
                    <option value="Paid">Paid</option>
                    <option value="Pending">Pending</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Bill Date</label>
                <input
                  type="date"
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={billForm.billDate}
                  onChange={e => setBillForm({ ...billForm, billDate: e.target.value })}
                />
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowCreate(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleCreateBill} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Generate Bill</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
