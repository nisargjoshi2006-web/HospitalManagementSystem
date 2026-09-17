import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Plus, Trash2, X, RefreshCw, CalendarDays, Clock } from 'lucide-react';
import { getDoctors, getSchedules, addSchedule, deleteSchedule } from '../api/api';

const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

export default function DoctorSchedule() {
  const [doctorsList, setDoctorsList] = useState<any[]>([]);
  const [schedulesList, setSchedulesList] = useState<any[]>([]);
  const [selectedDoctorId, setSelectedDoctorId] = useState<string>('All');
  const [showAdd, setShowAdd] = useState(false);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    doctorId: '',
    dayOfWeek: 'Monday',
    startTime: '09:00',
    endTime: '13:00'
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [dcts, schs] = await Promise.all([getDoctors(), getSchedules()]);
      if (Array.isArray(dcts)) {
        setDoctorsList(dcts);
        if (dcts.length > 0 && !form.doctorId) {
          setForm(prev => ({ ...prev, doctorId: String(dcts[0].rawId || dcts[0].id) }));
        }
      }
      if (Array.isArray(schs)) {
        setSchedulesList(schs);
      }
    } catch (err) {
      console.warn('Could not load schedules from API:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAddSchedule = async () => {
    if (!form.doctorId) {
      alert('Please select a doctor');
      return;
    }
    try {
      await addSchedule({
        doctorId: form.doctorId,
        dayOfWeek: form.dayOfWeek,
        startTime: form.startTime.length === 5 ? `${form.startTime}:00` : form.startTime,
        endTime: form.endTime.length === 5 ? `${form.endTime}:00` : form.endTime
      });
      setShowAdd(false);
      await loadData();
      alert('Doctor schedule saved successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to save schedule: ' + err.message);
    }
  };

  const handleDeleteSchedule = async (id: any) => {
    if (!confirm('Remove this schedule slot?')) return;
    try {
      await deleteSchedule(id);
      await loadData();
      alert('Schedule slot removed successfully!');
    } catch (err: any) {
      alert('Failed to remove schedule: ' + err.message);
    }
  };

  const filtered = schedulesList.filter(s => {
    if (selectedDoctorId === 'All') return true;
    return String(s.doctor_id) === String(selectedDoctorId);
  });

  return (
    <Layout title="Doctor Schedules" subtitle="Manage weekly doctor availability and consultation shifts in MySQL">
      <div className="p-6 space-y-5">
        {/* Filter bar */}
        <div className="flex items-center justify-between flex-wrap gap-3">
          <div className="flex items-center gap-3">
            <div>
              <label className="block text-xs font-medium text-slate-500 mb-1">Filter by Doctor</label>
              <select
                value={selectedDoctorId}
                onChange={e => setSelectedDoctorId(e.target.value)}
                className="pl-3 pr-8 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none"
              >
                <option value="All">All Doctors ({doctorsList.length})</option>
                {doctorsList.map(d => (
                  <option key={d.id} value={d.rawId || d.id}>
                    {d.name} ({d.specialization})
                  </option>
                ))}
              </select>
            </div>

            <button
              onClick={loadData}
              disabled={loading}
              className="mt-5 p-2 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600"
              title="Refresh"
            >
              <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
            </button>
          </div>

          <button
            onClick={() => setShowAdd(true)}
            className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg shadow-sm mt-4"
            style={{ background: '#0F766E' }}
          >
            <Plus size={15} /> Add Schedule Slot
          </button>
        </div>

        {/* Schedule grid / list */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">Configured Shifts ({filtered.length})</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Doctor_Schedule</span>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  {['Schedule ID', 'Doctor', 'Specialization', 'Day of Week', 'Shift Time', 'Actions'].map(h => (
                    <th key={h} className="px-4 py-3 text-left text-xs font-semibold text-slate-500 uppercase tracking-wide">{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {filtered.map(s => (
                  <tr key={s.schedule_id} className="hover:bg-slate-50/50 transition-colors">
                    <td className="px-4 py-3 font-mono text-xs text-slate-500">#{s.schedule_id}</td>
                    <td className="px-4 py-3 font-medium text-slate-800">{s.doctor_name}</td>
                    <td className="px-4 py-3 text-slate-600 text-xs">{s.specialization_name || 'General'}</td>
                    <td className="px-4 py-3 font-medium text-teal-700 text-xs flex items-center gap-1.5">
                      <CalendarDays size={13} className="text-teal-600" />
                      {s.day_of_week}
                    </td>
                    <td className="px-4 py-3 font-mono text-xs text-slate-700">
                      <span className="inline-flex items-center gap-1 bg-slate-100 px-2 py-0.5 rounded">
                        <Clock size={11} className="text-slate-500" />
                        {String(s.start_time).slice(0, 5)} – {String(s.end_time).slice(0, 5)}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <button
                        onClick={() => handleDeleteSchedule(s.schedule_id)}
                        className="p-1.5 rounded-lg hover:bg-red-50 text-slate-400 hover:text-red-600 transition-colors"
                        title="Delete Slot"
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

      {/* Add Schedule Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Add Doctor Schedule (Saves to MySQL)</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Doctor *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.doctorId}
                  onChange={e => setForm({ ...form, doctorId: e.target.value })}
                >
                  <option value="">-- Choose Doctor --</option>
                  {doctorsList.map(d => (
                    <option key={d.id} value={d.rawId || d.id}>
                      {d.name} ({d.specialization})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Day of Week *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.dayOfWeek}
                  onChange={e => setForm({ ...form, dayOfWeek: e.target.value })}
                >
                  {days.map(d => (
                    <option key={d} value={d}>{d}</option>
                  ))}
                </select>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">Start Time *</label>
                  <input
                    type="time"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.startTime}
                    onChange={e => setForm({ ...form, startTime: e.target.value })}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-700 mb-1">End Time *</label>
                  <input
                    type="time"
                    className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                    value={form.endTime}
                    onChange={e => setForm({ ...form, endTime: e.target.value })}
                  />
                </div>
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleAddSchedule} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Save Schedule</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
