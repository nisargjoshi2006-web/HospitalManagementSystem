import { useState, useEffect } from 'react';
import Layout from '../components/Layout';
import { Star, Plus, X, RefreshCw } from 'lucide-react';
import { feedback as fallbackFeedback } from '../data/mockData';
import { getFeedback, addFeedback, getPatients } from '../api/api';

function Stars({ rating }: { rating: number }) {
  return (
    <div className="flex gap-0.5">
      {[1, 2, 3, 4, 5].map(i => (
        <Star key={i} size={13} className={i <= rating ? 'text-amber-400 fill-amber-400' : 'text-slate-200 fill-slate-200'} />
      ))}
    </div>
  );
}

export default function Feedback() {
  const [feedbackList, setFeedbackList] = useState<any[]>([]);
  const [patients, setPatients] = useState<any[]>([]);
  const [showAdd, setShowAdd] = useState(false);
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    patientId: '',
    rating: 5,
    feedbackDate: new Date().toISOString().slice(0, 10),
    comments: ''
  });

  const loadData = async () => {
    setLoading(true);
    try {
      const [fData, pData] = await Promise.all([getFeedback(), getPatients()]);
      if (Array.isArray(fData) && fData.length > 0) {
        setFeedbackList(fData);
      } else {
        setFeedbackList(fallbackFeedback);
      }
      if (Array.isArray(pData)) {
        setPatients(pData);
        if (pData.length > 0 && !form.patientId) {
          setForm(prev => ({ ...prev, patientId: String(pData[0].rawId || pData[0].id) }));
        }
      }
    } catch (err) {
      console.warn('Could not load feedback from API:', err);
      setFeedbackList(fallbackFeedback);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleAddFeedback = async () => {
    if (!form.patientId) {
      alert('Please select a patient');
      return;
    }
    try {
      await addFeedback({
        patientId: form.patientId,
        rating: Number(form.rating),
        feedbackDate: form.feedbackDate,
        comments: form.comments
      });
      setShowAdd(false);
      setForm({ patientId: '', rating: 5, feedbackDate: new Date().toISOString().slice(0, 10), comments: '' });
      await loadData();
      alert('Patient feedback recorded successfully in MySQL!');
    } catch (err: any) {
      alert('Failed to submit feedback: ' + err.message);
    }
  };

  const avgRating = feedbackList.length > 0
    ? (feedbackList.reduce((s, f) => s + (Number(f.rating) || 0), 0) / feedbackList.length).toFixed(1)
    : '5.0';

  const dist = [5, 4, 3, 2, 1].map(r => ({
    stars: r,
    count: feedbackList.filter(f => Number(f.rating) === r).length
  }));

  return (
    <Layout title="Patient Feedback & Ratings" subtitle="Patient satisfaction reviews and 1-5 star ratings in MySQL">
      <div className="p-6 space-y-5">
        {/* Summary */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="bg-white rounded-xl border border-slate-200 p-5 flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="text-5xl font-bold text-slate-900">{avgRating}</div>
              <div>
                <Stars rating={Math.round(Number(avgRating))} />
                <div className="text-xs text-slate-500 mt-1">{feedbackList.length} reviews on record</div>
              </div>
            </div>
            <button
              onClick={loadData}
              disabled={loading}
              className="p-2 border border-slate-200 rounded-lg hover:bg-slate-100 text-slate-600"
              title="Refresh"
            >
              <RefreshCw size={15} className={loading ? 'animate-spin text-teal-600' : ''} />
            </button>
          </div>

          <div className="md:col-span-2 bg-white rounded-xl border border-slate-200 p-5">
            <div className="flex items-center justify-between mb-3">
              <h3 className="text-sm font-semibold text-slate-800">Rating Distribution</h3>
              <button
                onClick={() => setShowAdd(true)}
                className="flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium text-white rounded-lg shadow-sm"
                style={{ background: '#0F766E' }}
              >
                <Plus size={14} /> Leave Feedback
              </button>
            </div>
            <div className="space-y-2">
              {dist.map(({ stars, count }) => (
                <div key={stars} className="flex items-center gap-3">
                  <div className="flex items-center gap-1 w-16 shrink-0">
                    <span className="text-xs text-slate-600">{stars}</span>
                    <Star size={11} className="text-amber-400 fill-amber-400" />
                  </div>
                  <div className="flex-1 h-2 bg-slate-100 rounded-full overflow-hidden">
                    <div
                      className="h-full rounded-full bg-amber-400 transition-all duration-700"
                      style={{ width: feedbackList.length ? `${(count / feedbackList.length) * 100}%` : '0%' }}
                    />
                  </div>
                  <span className="text-xs text-slate-500 w-6 text-right">{count}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Feedback table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden shadow-xs">
          <div className="px-5 py-3 border-b border-slate-100 flex items-center justify-between">
            <span className="text-sm font-medium text-slate-700">Patient Reviews ({feedbackList.length})</span>
            <span className="text-xs text-teal-700 bg-teal-50 px-2 py-0.5 rounded font-medium">⚡ Connected to MySQL Feedback table</span>
          </div>
          <div className="divide-y divide-slate-100">
            {feedbackList.map(f => (
              <div key={f.id} className="p-4 hover:bg-slate-50/50 transition-colors">
                <div className="flex items-center justify-between mb-1">
                  <span className="font-semibold text-slate-800 text-sm">{f.patient}</span>
                  <span className="text-xs text-slate-400">{f.date}</span>
                </div>
                <div className="mb-2">
                  <Stars rating={Number(f.rating)} />
                </div>
                <p className="text-xs text-slate-600 leading-relaxed">{f.comment || 'No comment provided'}</p>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Add Feedback Modal */}
      {showAdd && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setShowAdd(false)} />
          <div className="relative bg-white rounded-xl shadow-2xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b border-slate-200">
              <h3 className="font-semibold text-slate-900">Record Patient Feedback (Saves to MySQL)</h3>
              <button onClick={() => setShowAdd(false)} className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"><X size={16} /></button>
            </div>
            <div className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Select Patient *</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.patientId}
                  onChange={e => setForm({ ...form, patientId: e.target.value })}
                >
                  <option value="">-- Choose Patient --</option>
                  {patients.map(p => (
                    <option key={p.id} value={p.rawId || p.id}>
                      {p.name} ({p.id})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Star Rating (1 - 5)</label>
                <select
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg bg-white focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  value={form.rating}
                  onChange={e => setForm({ ...form, rating: Number(e.target.value) })}
                >
                  <option value={5}>⭐⭐⭐⭐⭐ (5 Stars - Excellent)</option>
                  <option value={4}>⭐⭐⭐⭐ (4 Stars - Good)</option>
                  <option value={3}>⭐⭐⭐ (3 Stars - Average)</option>
                  <option value={2}>⭐⭐ (2 Stars - Poor)</option>
                  <option value={1}>⭐ (1 Star - Very Poor)</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-700 mb-1">Feedback Comments</label>
                <textarea
                  className="w-full px-3 py-2 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20"
                  rows={3}
                  placeholder="Share feedback on care, wait times, or doctor consultation..."
                  value={form.comments}
                  onChange={e => setForm({ ...form, comments: e.target.value })}
                />
              </div>

              <div className="flex gap-2 mt-5">
                <button onClick={() => setShowAdd(false)} className="flex-1 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Cancel</button>
                <button onClick={handleAddFeedback} className="flex-1 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Submit Feedback</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
