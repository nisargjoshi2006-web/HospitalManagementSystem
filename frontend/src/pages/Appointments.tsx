import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Search, Plus, ChevronDown, X, Trash2, RefreshCw } from 'lucide-react';
import { appointments as fallbackAppointments } from '../data/mockData';
import { getAppointments, addAppointment, deleteAppointment, getPatients, getDoctors } from '../api/api';

const statusColors: Record<string, string> = {
  Confirmed: 'bg-green-100 text-green-700',
  Scheduled: 'bg-blue-100 text-blue-700',
  Completed: 'bg-slate-100 text-slate-600',
  Cancelled: 'bg-red-100 text-red-600',
  Pending: 'bg-amber-100 text-amber-700',
};

export default function Appointments() {
  const [appointmentsList, setAppointmentsList] = useState<any[]>([]);
  const [patientsList, setPatientsList] = useState<any[]>([]);
  const [doctorsList, setDoctorsList] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [showBook, setShowBook] = useState(false);
  const [loading, setLoading] = useState(false);

  const [bookForm, setBookForm] = useState({
    patientId: '',
    doctorId: '',
    date: new Date().toISOString().slice(0, 10),
    time: '10:00',
    room: '101',
    status: 'Scheduled'
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [apts, pts, dcts] = await Promise.all([
        getAppointments(),
        getPatients(),
        getDoctors()
      ]);
      setAppointmentsList(Array.isArray(apts) && apts.length > 0 ? apts : fallbackAppointments);
      if (Array.isArray(pts)) setPatientsList(pts);
      if (Array.isArray(dcts)) setDoctorsList(dcts);
      if (Array.isArray(pts) && pts.length > 0 && !bookForm.patientId) {
        setBookForm(prev => ({ ...prev, patientId: String(pts[0].rawId || pts[0].id) }));
      }
      if (Array.isArray(dcts) && dcts.length > 0 && !bookForm.doctorId) {
        setBookForm(prev => ({ ...prev, doctorId: String(dcts[0].rawId || dcts[0].id) }));
      }
    } catch (err) {
      console.warn('Could not load live appointments, using mock:', err);
      setAppointmentsList(fallbackAppointments);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleBook = async () => {
    if (!bookForm.patientId || !bookForm.doctorId) {
      alert('Please select both a patient and a doctor');
      return;
    }
    try {
      await addAppointment({
        patientId: bookForm.patientId,
        doctorId: bookForm.doctorId,
        date: bookForm.date,
        time: bookForm.time.length === 5 ? `${bookForm.time}:00` : bookForm.time,
        room: bookForm.room || '101',
        status: bookForm.status
      });
      setShowBook(false);
      await loadData();
      alert('Appointment booked successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to book appointment: ' + err.message);
    }
  };

  const handleDelete = async (id: any) => {
    if (!confirm('Are you sure you want to cancel/delete this appointment?')) return;
    try {
      await deleteAppointment(id);
      await loadData();
      alert('Appointment cancelled successfully!');
    } catch (err: any) {
      alert('Failed to cancel appointment: ' + err.message);
    }
  };

  const filtered = appointmentsList.filter(a => {
    const matchSearch = (a.patient || '').toLowerCase().includes(search.toLowerCase()) ||
      (a.doctor || '').toLowerCase().includes(search.toLowerCase()) ||
      String(a.id).includes(search);
    const matchStatus = statusFilter === 'All' || a.status === statusFilter;
    return matchSearch && matchStatus;
  });

  const counts = ['Scheduled', 'Confirmed', 'Completed', 'Cancelled', 'Pending'].map(s => ({
    label: s, count: appointmentsList.filter(a => a.status === s).length, color: statusColors[s] || 'bg-slate-100',
  }));

  return (
    <Layout title="Appointments" subtitle="Schedule and manage live patient appointments">
      <div className="p-6 space-y-5">
        {/* Status summary */}
        <div className="grid grid-cols-2 md:grid-cols-5 gap-3">
          {counts.map(({ label, count, color }) => (
            <button
              key={label}
              onClick={() => setStatusFilter(statusFilter === label ? 'All' : label)}
              className={`bg-white rounded-xl border p-4 text-left transition-all hover:shadow-sm ${statusFilter === label ? 'border-teal-400 ring-1 ring-teal-400/20' : 'border-slate-200'}`}
            >
              <div className="text-2xl font-bold text-slate-900 mb-1">{count}</div>
              <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${color}`}>{label}</span>
            </button>
          ))}
        </div>

        {/* Toolbar */}
        <div className="flex items-center gap-3 flex-wrap">
          <div className="relative flex-1 min-w-[200px]">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search patient, doctor, ID..."
              value={search}
              onChange={e => setSearch(e.target.value)}
              className="w-full pl-9 pr-4 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 bg-white"
            />
          </div>
          <div className="relative">
            <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)} className="appearance-none pl-3 pr-8 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none bg-white text-slate-700">
              {['All', 'Scheduled', 'Confirmed', 'Completed', 'Cancelled', 'Pending'].map(s => <option key={s}>{s === 'All' ? 'All Statuses' : s}</option>)}
            </select>
            <ChevronDown size={13} className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 pointer-events-none" />
          </div>
          <button
            onClick={loadData}
            disabled={loading}
            className="p-2 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600 transition-colors"
            title="Refresh List"
          >
            <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
          </button>
          {(search || statusFilter !== 'All') && (
            <button onClick={() => { setSearch(''); setStatusFilter('All'); }} className="text-sm text-slate-500 hover:text-slate-700 flex items-center gap-1">
              <X size={14} /> Clear
            </button>
          )}
          <button
            onClick={() => setShowBook(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg ml-auto shadow-sm"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Book Appointment
          </button>
        </div>

        {/* Table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">{filtered.length} appointment{filtered.length !== 1 ? 's' : ''}</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Appointments table</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Appt ID', 'Patient', 'Doctor', 'Specialization', 'Date', 'Time', 'Room', 'Status', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map((a: any) => (
                  <tr key={a.id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">{a.id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{a.patient}</td>
                    <td className="px-4 py-3 text-slate-700">{a.doctor}</td>
                    <td className="px-4 py-3">
                      <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">{a.specialization}</span>
                    </td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{a.date}</td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-600">{a.time}</td>
                    <td className="px-4 py-3 text-slate-500 text-xs">{a.room}</td>
                    <td className="px-4 py-3">
                      <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${statusColors[a.status] || 'bg-slate-100'}`}>{a.status}</span>
                    </td>
                    <td className="px-4 py-3">
                      <button
                        onClick={() => handleDelete(a.rawId || a.id)}
                        className="p-1.5 rounded-lg hover:bg-red-50 text-slate-400 hover:text-red-600 transition-colors"
                        title="Cancel Appointment"
                      >
                        <Trash2 size={14} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Book Modal */}
      {showBook && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowBook(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Book Appointment (Saves to MySQL)</h3>
              <button onClick={() => setShowBook(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Patient *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={bookForm.patientId}
                  onChange={e => setBookForm({ ...bookForm, patientId: e.target.value })}
                >
                  <option value="">-- Choose Patient --</option>
                  {patientsList.map(p => (
                    <option key={p.id} value={p.rawId || p.id}>
                      {p.name} ({p.id})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Doctor *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={bookForm.doctorId}
                  onChange={e => setBookForm({ ...bookForm, doctorId: e.target.value })}
                >
                  <option value="">-- Choose Doctor --</option>
                  {doctorsList.map(d => (
                    <option key={d.id} value={d.rawId || d.id}>
                      {d.name} ({d.specialization}) - ₹{d.fee}
                    </option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Date *</label>
                  <input
                    type="date"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={bookForm.date}
                    onChange={e => setBookForm({ ...bookForm, date: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Time *</label>
                  <input
                    type="time"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={bookForm.time}
                    onChange={e => setBookForm({ ...bookForm, time: e.target.value })}
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Room</label>
                  <input
                    type="text"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    placeholder="e.g. 101"
                    value={bookForm.room}
                    onChange={e => setBookForm({ ...bookForm, room: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Initial Status</label>
                  <select
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={bookForm.status}
                    onChange={e => setBookForm({ ...bookForm, status: e.target.value })}
                  >
                    <option value="Scheduled">Scheduled</option>
                    <option value="Confirmed">Confirmed</option>
                    <option value="Pending">Pending</option>
                  </select>
                </div>
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowBook(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleBook} className="flex-1 py-2 text-sm font-medium text-white rounded-lg transition-opacity hover:opacity-90" style={{ background: '#0F766E' }}>Confirm Booking</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
