import { useState } from 'react';
import Layout from '../components/Layout';
import { ChevronLeft, ChevronRight, Plus } from 'lucide-react';
import { doctors } from '../data/mockData';

const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
const timeSlots = ['08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00'];

const scheduleData: Record<string, Record<string, Record<string, string>>> = {
  'D-001': {
    Monday: { '09:00': 'booked', '10:00': 'booked', '11:00': 'available', '14:00': 'booked', '15:00': 'available' },
    Tuesday: { '09:00': 'available', '10:00': 'booked', '11:00': 'booked', '14:00': 'available' },
    Wednesday: { '09:00': 'booked', '10:00': 'available', '14:00': 'booked', '15:00': 'booked' },
    Thursday: { '09:00': 'available', '10:00': 'booked', '11:00': 'available' },
    Friday: { '09:00': 'booked', '10:00': 'booked', '11:00': 'booked', '14:00': 'available' },
  },
  'D-002': {
    Monday: { '08:00': 'booked', '09:00': 'available', '10:00': 'booked' },
    Tuesday: { '08:00': 'available', '09:00': 'booked', '14:00': 'booked', '15:00': 'available' },
    Wednesday: { '10:00': 'booked', '11:00': 'booked', '14:00': 'available' },
    Thursday: { '08:00': 'booked', '09:00': 'booked', '14:00': 'booked' },
    Friday: { '09:00': 'available', '10:00': 'booked' },
  },
};

export default function DoctorSchedule() {
  const [selectedDoctor, setSelectedDoctor] = useState(doctors[0].id);
  const [weekOffset, setWeekOffset] = useState(0);

  const schedule = scheduleData[selectedDoctor] ?? {};
  const doctor = doctors.find(d => d.id === selectedDoctor);

  const getWeekLabel = () => {
    const base = new Date(2026, 8, 14);
    base.setDate(base.getDate() + weekOffset * 7);
    const end = new Date(base);
    end.setDate(end.getDate() + 6);
    return `${base.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })} – ${end.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}`;
  };

  return (
    <Layout title="Doctor Schedule" subtitle="Manage weekly availability and time slots">
      <div className="p-6 space-y-5">
        {/* Controls */}
        <div className="flex items-center gap-4 flex-wrap">
          <div>
            <label className="block text-xs font-medium text-slate-600 mb-1">Doctor</label>
            <select
              value={selectedDoctor}
              onChange={e => setSelectedDoctor(e.target.value)}
              className="appearance-none pl-3 pr-8 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none bg-white text-slate-800 font-medium"
            >
              {doctors.map(d => (
                <option key={d.id} value={d.id}>{d.name} — {d.specialization}</option>
              ))}
            </select>
          </div>

          <div className="flex items-center gap-2 ml-auto">
            <button onClick={() => setWeekOffset(w => w - 1)} className="p-1.5 rounded-lg border border-slate-200 hover:bg-slate-50 text-slate-600">
              <ChevronLeft size={16} />
            </button>
            <span className="text-sm font-medium text-slate-700 whitespace-nowrap">{getWeekLabel()}</span>
            <button onClick={() => setWeekOffset(w => w + 1)} className="p-1.5 rounded-lg border border-slate-200 hover:bg-slate-50 text-slate-600">
              <ChevronRight size={16} />
            </button>
          </div>

          <button className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>
            <Plus size={15} /> Add Slot
          </button>
        </div>

        {/* Legend */}
        <div className="flex items-center gap-4 text-xs">
          {[
            { label: 'Available', color: 'bg-teal-100 border-teal-300' },
            { label: 'Booked', color: 'bg-slate-200 border-slate-300' },
            { label: 'No schedule', color: 'bg-slate-50 border-slate-200' },
          ].map(({ label, color }) => (
            <div key={label} className="flex items-center gap-1.5">
              <div className={`w-4 h-4 rounded border ${color}`} />
              <span className="text-slate-600">{label}</span>
            </div>
          ))}
        </div>

        {/* Schedule grid */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-x-auto">
          <table className="w-full text-xs border-collapse">
            <thead>
              <tr>
                <th className="w-16 px-3 py-3 text-left text-slate-500 font-medium border-b border-r border-slate-200 bg-slate-50">Time</th>
                {days.map(d => (
                  <th key={d} className="px-3 py-3 text-center font-medium text-slate-700 border-b border-r border-slate-200 bg-slate-50 last:border-r-0 min-w-[100px]">
                    {d.slice(0, 3)}
                    <div className="font-normal text-slate-400 text-[10px]">{d}</div>
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {timeSlots.map(time => (
                <tr key={time} className="hover:bg-slate-50/30">
                  <td className="px-3 py-2.5 font-mono text-slate-500 border-b border-r border-slate-100 font-medium bg-slate-50/50">{time}</td>
                  {days.map(day => {
                    const slot = schedule[day]?.[time];
                    return (
                      <td key={day} className="px-2 py-2 border-b border-r border-slate-100 last:border-r-0 text-center">
                        {slot ? (
                          <div
                            className={`rounded-lg px-2 py-1.5 text-[10px] font-medium cursor-pointer transition-all hover:opacity-80 ${
                              slot === 'available'
                                ? 'bg-teal-100 text-teal-700 border border-teal-200'
                                : 'bg-slate-200 text-slate-600 border border-slate-300'
                            }`}
                          >
                            {slot === 'available' ? '✓ Free' : '✗ Booked'}
                          </div>
                        ) : (
                          <div className="h-7 rounded-lg bg-slate-50 border border-dashed border-slate-200 flex items-center justify-center opacity-0 hover:opacity-100 transition-opacity cursor-pointer">
                            <Plus size={10} className="text-slate-400" />
                          </div>
                        )}
                      </td>
                    );
                  })}
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {doctor && (
          <div className="bg-teal-50 border border-teal-200 rounded-xl p-4 flex items-center gap-3">
            <div className="w-9 h-9 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center font-bold shrink-0">
              {doctor.name.replace('Dr. ', '').charAt(0)}
            </div>
            <div>
              <div className="font-semibold text-teal-900 text-sm">{doctor.name}</div>
              <div className="text-xs text-teal-700">{doctor.specialization} · {doctor.experience} years experience · {doctor.patients} patients</div>
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
}
