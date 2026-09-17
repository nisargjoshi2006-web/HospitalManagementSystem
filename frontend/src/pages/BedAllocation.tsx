import { useState } from 'react';
import Layout from '../components/Layout';
import { Plus, X } from 'lucide-react';
import { beds } from '../data/mockData';

type Ward = 'ICU' | 'Private' | 'General';

const wardConfig: Record<Ward, { color: string; bg: string; border: string }> = {
  ICU: { color: '#DC2626', bg: '#FEF2F2', border: '#FECACA' },
  Private: { color: '#0284C7', bg: '#F0F9FF', border: '#BAE6FD' },
  General: { color: '#0F766E', bg: '#F0FDFA', border: '#99F6E4' },
};

const statusStyle: Record<string, string> = {
  Available: 'bg-teal-50 border-teal-300 text-teal-700 hover:bg-teal-100',
  Occupied: 'bg-slate-100 border-slate-300 text-slate-600',
  Reserved: 'bg-amber-50 border-amber-300 text-amber-700',
};

export default function BedAllocation() {
  const [selectedWard, setSelectedWard] = useState<Ward>('ICU');
  const [selectedBed, setSelectedBed] = useState<typeof beds['ICU'][0] | null>(null);
  const [showAdmit, setShowAdmit] = useState(false);

  const wardBeds = beds[selectedWard];
  const occupied = wardBeds.filter(b => b.status === 'Occupied').length;
  const available = wardBeds.filter(b => b.status === 'Available').length;
  const reserved = wardBeds.filter(b => b.status === 'Reserved').length;
  const cfg = wardConfig[selectedWard];

  return (
    <Layout title="Bed Allocation" subtitle="Hospital ward capacity and patient admission">
      <div className="p-6 space-y-5">
        {/* Ward selector + summary */}
        <div className="grid grid-cols-3 gap-3">
          {(['ICU', 'Private', 'General'] as Ward[]).map(w => {
            const wbeds = beds[w];
            const wocc = wbeds.filter(b => b.status === 'Occupied').length;
            const wcfg = wardConfig[w];
            return (
              <button
                key={w}
                onClick={() => setSelectedWard(w)}
                className={`bg-white rounded-xl border-2 p-4 text-left transition-all hover:shadow-sm ${selectedWard === w ? '' : 'border-slate-200'}`}
                style={selectedWard === w ? { borderColor: wcfg.color } : undefined}
              >
                <div className="flex items-center justify-between mb-3">
                  <span className="font-semibold text-slate-800">{w}</span>
                  <span className="text-xs px-2 py-0.5 rounded-full font-medium" style={{ background: wcfg.bg, color: wcfg.color }}>
                    {Math.round((wocc / wbeds.length) * 100)}%
                  </span>
                </div>
                <div className="text-2xl font-bold text-slate-900">{wocc}/{wbeds.length}</div>
                <div className="text-xs text-slate-500 mt-0.5">Occupied · {wbeds.length - wocc} free</div>
                <div className="h-1.5 bg-slate-100 rounded-full mt-3 overflow-hidden">
                  <div className="h-full rounded-full transition-all duration-700" style={{ width: `${(wocc / wbeds.length) * 100}%`, background: wcfg.color }} />
                </div>
              </button>
            );
          })}
        </div>

        {/* Stats + actions */}
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4 text-sm">
            <span className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-slate-300" /> Occupied <strong>{occupied}</strong></span>
            <span className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-teal-300" /> Available <strong>{available}</strong></span>
            <span className="flex items-center gap-1.5"><span className="w-3 h-3 rounded bg-amber-300" /> Reserved <strong>{reserved}</strong></span>
          </div>
          <button onClick={() => setShowAdmit(true)} className="flex items-center gap-1.5 px-3.5 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>
            <Plus size={15} /> Admit Patient
          </button>
        </div>

        {/* Bed grid */}
        <div className="bg-white rounded-xl border border-slate-200 p-5">
          <h3 className="font-semibold text-slate-800 mb-4 text-sm">{selectedWard} Ward — Bed Grid</h3>
          <div className="grid grid-cols-4 sm:grid-cols-6 md:grid-cols-8 xl:grid-cols-10 gap-2">
            {wardBeds.map(bed => (
              <button
                key={bed.id}
                onClick={() => setSelectedBed(bed)}
                className={`p-2 rounded-lg border text-center transition-all ${statusStyle[bed.status]} ${bed.status === 'Available' ? 'cursor-pointer' : ''}`}
                title={bed.patient ? `${bed.id}: ${bed.patient}` : `${bed.id}: ${bed.status}`}
              >
                <div className="text-[10px] font-mono font-semibold">{bed.id.split('-')[1]}</div>
                <div className="text-[9px] mt-0.5 truncate">{bed.status === 'Occupied' && bed.patient ? bed.patient.split(' ')[0] : bed.status}</div>
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Bed detail modal */}
      {selectedBed && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setSelectedBed(null)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-sm p-5">
            <div className="flex items-center justify-between mb-4">
              <h3 className="font-semibold text-slate-900">Bed {selectedBed.id}</h3>
              <button onClick={() => setSelectedBed(null)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between"><span className="text-slate-500">Ward</span><span className="font-medium">{selectedBed.ward}</span></div>
              <div className="flex justify-between"><span className="text-slate-500">Status</span>
                <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${selectedBed.status === 'Available' ? 'bg-teal-100 text-teal-700' : selectedBed.status === 'Occupied' ? 'bg-slate-100 text-slate-700' : 'bg-amber-100 text-amber-700'}`}>
                  {selectedBed.status}
                </span>
              </div>
              {selectedBed.patient && <div className="flex justify-between"><span className="text-slate-500">Patient</span><span className="font-medium">{selectedBed.patient}</span></div>}
              {selectedBed.admitted && <div className="flex justify-between"><span className="text-slate-500">Admitted</span><span className="font-medium">{selectedBed.admitted}</span></div>}
            </div>
            {selectedBed.status === 'Occupied' && (
              <button onClick={() => setSelectedBed(null)} className="w-full mt-4 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700">
                Discharge Patient
              </button>
            )}
            {selectedBed.status === 'Available' && (
              <button onClick={() => { setSelectedBed(null); setShowAdmit(true); }} className="w-full mt-4 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>
                Admit Patient to This Bed
              </button>
            )}
          </div>
        </div>
      )}

      {showAdmit && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdmit(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Admit Patient</h3>
              <button onClick={() => setShowAdmit(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              {['Patient', 'Ward', 'Bed Number', 'Admitting Doctor', 'Reason for Admission'].map(f => (
                <div key={f}>
                  <label className="block text-xs font-medium text-slate-700 mb-1">{f}</label>
                  <input className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400" />
                </div>
              ))}
              <div className="flex gap-2 mt-4">
                <button onClick={() => setShowAdmit(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={() => setShowAdmit(false)} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Admit Patient</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
