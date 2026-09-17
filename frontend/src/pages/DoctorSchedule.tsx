import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import {
  Plus,
  Trash2,
  X,
  RefreshCw,
  CalendarDays,
  Clock,
  LayoutGrid,
  List,
  Calendar,
  Stethoscope,
  CheckCircle2,
  Users
} from 'lucide-react';
import { getDoctors, getSchedules, addSchedule, deleteSchedule } from '../api/api';

const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

const getSpecBadge = (spec: string) => {
  const s = (spec || '').toLowerCase();
  if (s.includes('cardio')) return 'bg-rose-50 text-rose-700 border-rose-200';
  if (s.includes('neuro')) return 'bg-purple-50 text-purple-700 border-purple-200';
  if (s.includes('ortho')) return 'bg-amber-50 text-amber-700 border-amber-200';
  if (s.includes('derma')) return 'bg-sky-50 text-sky-700 border-sky-200';
  return 'bg-teal-50 text-teal-700 border-teal-200';
};

const getDoctorInitials = (name: string) => {
  return name.replace(/^Dr\.\s*/i, '').split(' ').map(n => n[0]).join('').slice(0, 2).toUpperCase() || 'DR';
};

const getShiftLabel = (startTime: string) => {
  const hour = parseInt(String(startTime).slice(0, 2), 10);
  if (hour < 12) return 'Morning OPD';
  if (hour < 16) return 'Afternoon OPD';
  return 'Evening OPD';
};

export default function DoctorSchedule() {
  const [doctorsList, setDoctorsList] = useState<any[]>([]);
  const [schedulesList, setSchedulesList] = useState<any[]>([]);
  const [selectedDoctorId, setSelectedDoctorId] = useState<string>('All');
  const [viewMode, setViewMode] = useState<'matrix' | 'cards' | 'list'>('matrix');
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

  const handleOpenAdd = (presetDoctorId?: string | number, presetDay?: string) => {
    setForm(prev => ({
      ...prev,
      doctorId: presetDoctorId ? String(presetDoctorId) : (prev.doctorId || String(doctorsList[0]?.rawId || '')),
      dayOfWeek: presetDay || prev.dayOfWeek || 'Monday'
    }));
    setShowAdd(true);
  };

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
    } catch (err: any) {
      alert('Failed to remove schedule: ' + err.message);
    }
  };

  const filtered = schedulesList.filter(s => {
    if (selectedDoctorId === 'All') return true;
    return String(s.doctor_id) === String(selectedDoctorId);
  });

  // Calculate quick stats
  const activeDoctorsInSchedule = new Set(schedulesList.map(s => s.doctor_id)).size;
  const weekendShifts = schedulesList.filter(s => s.day_of_week === 'Saturday' || s.day_of_week === 'Sunday').length;

  return (
    <Layout title="Doctor Schedules & Timetable" subtitle="Weekly duty roster and consultation timetable connected to MySQL">
      <div className="p-6 space-y-6">
        {/* KPI Quick Banner */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-teal-50 flex items-center justify-center text-teal-600">
              <CalendarDays size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Total Weekly Shifts</p>
              <p className="text-xl font-bold text-slate-800">{schedulesList.length} Slots</p>
            </div>
          </div>

          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-blue-50 flex items-center justify-center text-blue-600">
              <Users size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Doctors on Schedule</p>
              <p className="text-xl font-bold text-slate-800">{activeDoctorsInSchedule} Specialists</p>
            </div>
          </div>

          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-emerald-50 flex items-center justify-center text-emerald-600">
              <CheckCircle2 size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Hospital Coverage</p>
              <p className="text-xl font-bold text-emerald-700">7 Days / Week</p>
            </div>
          </div>

          <div className="bg-white p-4 rounded-xl border border-slate-200 shadow-xs flex items-center gap-3.5">
            <div className="w-10 h-10 rounded-lg bg-purple-50 flex items-center justify-center text-purple-600">
              <Clock size={20} />
            </div>
            <div>
              <p className="text-xs text-slate-500 font-medium">Weekend Coverage</p>
              <p className="text-xl font-bold text-purple-700">{weekendShifts} Shifts</p>
            </div>
          </div>
        </div>

        {/* Toolbar: Filter, View Switcher, Add button */}
        <div className="flex items-center justify-between flex-wrap gap-3 bg-white p-4 rounded-xl border border-slate-200 shadow-xs">
          <div className="flex items-center gap-3 flex-wrap">
            <div className="flex items-center gap-2">
              <label className="text-xs font-semibold text-slate-500 uppercase tracking-wide">Doctor:</label>
              <select
                value={selectedDoctorId}
                onChange={e => setSelectedDoctorId(e.target.value)}
                className="pl-3 pr-8 py-1.5 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20 text-slate-700 font-medium"
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
              className="p-2 border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-600 transition-colors"
              title="Refresh from MySQL"
            >
              <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
            </button>
          </div>

          <div className="flex items-center gap-2 flex-wrap">
            {/* View Mode Toggle Buttons */}
            <div className="flex items-center bg-slate-100 p-1 rounded-lg border border-slate-200 text-xs">
              <button
                onClick={() => setViewMode('matrix')}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md font-medium transition-all ${
                  viewMode === 'matrix'
                    ? 'bg-white text-teal-700 shadow-xs'
                    : 'text-slate-600 hover:text-slate-900'
                }`}
                title="Doctors vs Days Timetable Matrix"
              >
                <LayoutGrid size={14} />
                <span>Timetable Grid</span>
              </button>

              <button
                onClick={() => setViewMode('cards')}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md font-medium transition-all ${
                  viewMode === 'cards'
                    ? 'bg-white text-teal-700 shadow-xs'
                    : 'text-slate-600 hover:text-slate-900'
                }`}
                title="7-Day Column Board"
              >
                <Calendar size={14} />
                <span>Daily Columns</span>
              </button>

              <button
                onClick={() => setViewMode('list')}
                className={`flex items-center gap-1.5 px-3 py-1.5 rounded-md font-medium transition-all ${
                  viewMode === 'list'
                    ? 'bg-white text-teal-700 shadow-xs'
                    : 'text-slate-600 hover:text-slate-900'
                }`}
                title="Detailed Table View"
              >
                <List size={14} />
                <span>List View</span>
              </button>
            </div>

            <button
              onClick={() => handleOpenAdd()}
              className="flex items-center gap-1.5 px-3.5 py-1.5 text-sm font-medium text-white rounded-lg shadow-sm"
              style={{ background: '#0F766E' }}
            >
              <Plus size={15} /> Add Shift Slot
            </button>
          </div>
        </div>

        {/* ========================================== */}
        {/* VIEW 1: TIMETABLE MATRIX (Doctors × Days) */}
        {/* ========================================== */}
        {viewMode === 'matrix' && (
          <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
            <div className="px-5 py-3.5 border-b border-slate-100 flex items-center justify-between bg-slate-50/50">
              <div className="flex items-center gap-2">
                <span className="text-sm font-semibold text-slate-800">Weekly Duty Timetable</span>
                <span className="text-xs text-slate-500">({filtered.length} active shift allocations)</span>
              </div>
              <span className="text-xs text-teal-700 bg-teal-50 px-2.5 py-1 rounded-md font-medium border border-teal-100">
                ⚡ Synced with MySQL `Doctor_Schedule`
              </span>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full text-sm border-collapse">
                <thead>
                  <tr className="bg-slate-50 border-b border-slate-200">
                    <th className="px-4 py-3 text-left text-xs font-semibold text-slate-600 uppercase tracking-wide sticky left-0 bg-slate-50 z-10 w-64">
                      Doctor / Specialization
                    </th>
                    {days.map(d => (
                      <th
                        key={d}
                        className={`px-3 py-3 text-center text-xs font-semibold tracking-wide uppercase ${
                          d === 'Saturday' || d === 'Sunday'
                            ? 'text-purple-700 bg-purple-50/40'
                            : 'text-slate-600'
                        }`}
                        style={{ minWidth: '135px' }}
                      >
                        {d}
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                  {(selectedDoctorId === 'All'
                    ? doctorsList
                    : doctorsList.filter(d => String(d.rawId || d.id) === String(selectedDoctorId))
                  ).map(doc => {
                    const docRawId = doc.rawId || doc.id;
                    return (
                      <tr key={doc.id} className="hover:bg-slate-50/60 transition-colors">
                        {/* Doctor Info Header Cell */}
                        <td className="px-4 py-3 font-medium text-slate-800 sticky left-0 bg-white shadow-xs z-10 border-r border-slate-100">
                          <div className="flex items-center gap-2.5">
                            <div className="w-8 h-8 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center font-bold text-xs shrink-0">
                              {getDoctorInitials(doc.name)}
                            </div>
                            <div className="min-w-0">
                              <p className="text-sm font-semibold text-slate-800 truncate">{doc.name}</p>
                              <span className={`inline-block text-[11px] px-2 py-0.5 rounded-full border mt-0.5 ${getSpecBadge(doc.specialization)}`}>
                                {doc.specialization}
                              </span>
                            </div>
                          </div>
                        </td>

                        {/* Days Cells */}
                        {days.map(d => {
                          const shifts = schedulesList.filter(
                            s => String(s.doctor_id) === String(docRawId) && s.day_of_week === d
                          );

                          return (
                            <td
                              key={d}
                              className={`px-2 py-2 text-center align-top border-r border-slate-100 last:border-r-0 ${
                                d === 'Saturday' || d === 'Sunday' ? 'bg-purple-50/10' : ''
                              }`}
                            >
                              {shifts.length > 0 ? (
                                <div className="space-y-1.5">
                                  {shifts.map(s => (
                                    <div
                                      key={s.schedule_id}
                                      className="group relative bg-white border border-teal-200 rounded-lg p-1.5 shadow-2xs hover:border-teal-400 hover:shadow-xs transition-all text-left"
                                    >
                                      <div className="flex items-center justify-between gap-1 text-[11px] font-mono text-teal-800 font-semibold">
                                        <div className="flex items-center gap-1">
                                          <Clock size={11} className="text-teal-600 shrink-0" />
                                          <span>
                                            {String(s.start_time).slice(0, 5)} - {String(s.end_time).slice(0, 5)}
                                          </span>
                                        </div>
                                        <button
                                          onClick={() => handleDeleteSchedule(s.schedule_id)}
                                          className="opacity-0 group-hover:opacity-100 text-slate-400 hover:text-red-600 transition-opacity p-0.5 rounded"
                                          title="Remove Slot"
                                        >
                                          <Trash2 size={11} />
                                        </button>
                                      </div>
                                      <div className="text-[10px] text-slate-500 font-sans mt-0.5">
                                        {getShiftLabel(s.start_time)}
                                      </div>
                                    </div>
                                  ))}
                                  <button
                                    onClick={() => handleOpenAdd(docRawId, d)}
                                    className="w-full py-0.5 text-[10px] text-slate-400 hover:text-teal-700 hover:bg-teal-50 rounded border border-dashed border-slate-200 hover:border-teal-300 transition-colors"
                                    title="Add another shift for this day"
                                  >
                                    + Add
                                  </button>
                                </div>
                              ) : (
                                <div className="h-full flex items-center justify-center min-h-[48px]">
                                  <button
                                    onClick={() => handleOpenAdd(docRawId, d)}
                                    className="opacity-20 hover:opacity-100 p-1 text-slate-400 hover:text-teal-600 hover:bg-teal-50 rounded transition-all text-xs flex items-center gap-0.5"
                                    title={`Schedule ${doc.name} on ${d}`}
                                  >
                                    <Plus size={13} />
                                  </button>
                                </div>
                              )}
                            </td>
                          );
                        })}
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        )}

        {/* ========================================== */}
        {/* VIEW 2: 7-DAY COLUMN BOARD                 */}
        {/* ========================================== */}
        {viewMode === 'cards' && (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-7 gap-4">
            {days.map(d => {
              const dayShifts = filtered.filter(s => s.day_of_week === d);
              const isWeekend = d === 'Saturday' || d === 'Sunday';

              return (
                <div
                  key={d}
                  className={`bg-white rounded-xl border ${
                    isWeekend ? 'border-purple-200 shadow-2xs' : 'border-slate-200'
                  } flex flex-col overflow-hidden shadow-xs`}
                >
                  {/* Day Header */}
                  <div
                    className={`px-3.5 py-3 border-b flex items-center justify-between ${
                      isWeekend ? 'bg-purple-50/70 border-purple-100' : 'bg-slate-50 border-slate-100'
                    }`}
                  >
                    <div>
                      <h4 className={`text-sm font-bold ${isWeekend ? 'text-purple-900' : 'text-slate-800'}`}>{d}</h4>
                      <p className="text-[11px] text-slate-500">{dayShifts.length} {dayShifts.length === 1 ? 'Doctor' : 'Doctors'}</p>
                    </div>
                    <button
                      onClick={() => handleOpenAdd(undefined, d)}
                      className="p-1 rounded-md hover:bg-white text-slate-400 hover:text-teal-700 shadow-2xs transition-colors"
                      title={`Add slot to ${d}`}
                    >
                      <Plus size={14} />
                    </button>
                  </div>

                  {/* Shifts List for the Day */}
                  <div className="p-2.5 space-y-2 flex-1 max-h-[500px] overflow-y-auto">
                    {dayShifts.length > 0 ? (
                      dayShifts.map(s => (
                        <div
                          key={s.schedule_id}
                          className="bg-white p-2.5 rounded-lg border border-slate-200 hover:border-teal-400 hover:shadow-xs transition-all text-left relative group"
                        >
                          <div className="flex items-start justify-between gap-1">
                            <div className="min-w-0">
                              <p className="text-xs font-semibold text-slate-800 truncate">{s.doctor_name}</p>
                              <span className={`inline-block text-[10px] px-1.5 py-0.2 rounded border font-medium mt-0.5 ${getSpecBadge(s.specialization_name)}`}>
                                {s.specialization_name || 'General'}
                              </span>
                            </div>
                            <button
                              onClick={() => handleDeleteSchedule(s.schedule_id)}
                              className="opacity-0 group-hover:opacity-100 text-slate-400 hover:text-red-600 transition-opacity p-1 rounded"
                              title="Delete Slot"
                            >
                              <Trash2 size={12} />
                            </button>
                          </div>

                          <div className="mt-2 pt-2 border-t border-slate-100 flex items-center justify-between text-[11px] text-slate-600 font-mono">
                            <div className="flex items-center gap-1 text-teal-700 font-semibold">
                              <Clock size={11} className="text-teal-600" />
                              {String(s.start_time).slice(0, 5)} – {String(s.end_time).slice(0, 5)}
                            </div>
                            <span className="text-[10px] text-slate-400 font-sans">
                              {getShiftLabel(s.start_time)}
                            </span>
                          </div>
                        </div>
                      ))
                    ) : (
                      <div className="py-8 text-center text-xs text-slate-400">
                        No shifts scheduled
                      </div>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        )}

        {/* ========================================== */}
        {/* VIEW 3: DETAILED TABLE LIST                */}
        {/* ========================================== */}
        {viewMode === 'list' && (
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
                      <td className="px-4 py-3 font-medium text-slate-800">
                        <div className="flex items-center gap-2">
                          <div className="w-6 h-6 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center font-bold text-[10px]">
                            {getDoctorInitials(s.doctor_name)}
                          </div>
                          <span>{s.doctor_name}</span>
                        </div>
                      </td>
                      <td className="px-4 py-3 text-slate-600 text-xs">
                        <span className={`inline-block px-2 py-0.5 rounded border text-[11px] ${getSpecBadge(s.specialization_name)}`}>
                          {s.specialization_name || 'General'}
                        </span>
                      </td>
                      <td className="px-4 py-3 font-medium text-teal-700 text-xs">
                        <div className="flex items-center gap-1.5">
                          <CalendarDays size={13} className="text-teal-600" />
                          {s.day_of_week}
                        </div>
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
        )}
      </div>

      {/* Add Schedule Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <div>
                <h3 className="font-semibold text-slate-900">Add Doctor Schedule Slot</h3>
                <p className="text-xs text-slate-500 mt-0.5">Saves directly into MySQL `Doctor_Schedule` table</p>
              </div>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500">
                <X size={16} />
              </button>
            </div>
            <div className="p-5 space-y-4">
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

              {/* Quick Time Presets */}
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1.5">Quick Time Presets</label>
                <div className="grid grid-cols-2 gap-2 text-xs">
                  <button
                    type="button"
                    onClick={() => setForm({ ...form, startTime: '09:00', endTime: '13:00' })}
                    className="py-1 px-2 border border-slate-200 rounded-lg hover:bg-teal-50 hover:text-teal-700 hover:border-teal-300 text-slate-600 transition-colors text-left"
                  >
                    🌅 Morning (09:00 - 13:00)
                  </button>
                  <button
                    type="button"
                    onClick={() => setForm({ ...form, startTime: '14:00', endTime: '18:00' })}
                    className="py-1 px-2 border border-slate-200 rounded-lg hover:bg-teal-50 hover:text-teal-700 hover:border-teal-300 text-slate-600 transition-colors text-left"
                  >
                    ☀️ Afternoon (14:00 - 18:00)
                  </button>
                  <button
                    type="button"
                    onClick={() => setForm({ ...form, startTime: '16:00', endTime: '20:00' })}
                    className="py-1 px-2 border border-slate-200 rounded-lg hover:bg-teal-50 hover:text-teal-700 hover:border-teal-300 text-slate-600 transition-colors text-left"
                  >
                    🌙 Evening (16:00 - 20:00)
                  </button>
                  <button
                    type="button"
                    onClick={() => setForm({ ...form, startTime: '10:00', endTime: '14:00' })}
                    className="py-1 px-2 border border-slate-200 rounded-lg hover:bg-teal-50 hover:text-teal-700 hover:border-teal-300 text-slate-600 transition-colors text-left"
                  >
                    🏥 Mid-day (10:00 - 14:00)
                  </button>
                </div>
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

              <div className="flex gap-2 pt-2">
                <button
                  type="button"
                  onClick={() => setShowAdd(false)}
                  className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700 font-medium"
                >
                  Cancel
                </button>
                <button
                  type="button"
                  onClick={handleAddSchedule}
                  className="flex-1 py-2 text-sm font-medium text-white rounded-lg shadow-sm"
                  style={{ background: '#0F766E' }}
                >
                  Save Schedule Slot
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
