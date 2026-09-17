import Layout from '../components/Layout';
import { Star } from 'lucide-react';
import { feedback } from '../data/mockData';

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
  const avgRating = (feedback.reduce((s, f) => s + f.rating, 0) / feedback.length).toFixed(1);
  const dist = [5, 4, 3, 2, 1].map(r => ({ stars: r, count: feedback.filter(f => f.rating === r).length }));

  return (
    <Layout title="Patient Feedback" subtitle="Patient satisfaction and service ratings">
      <div className="p-6 space-y-5">
        {/* Summary */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {/* Average rating */}
          <div className="bg-white rounded-xl border border-slate-200 p-5 flex items-center gap-4">
            <div className="text-5xl font-bold text-slate-900">{avgRating}</div>
            <div>
              <Stars rating={Math.round(Number(avgRating))} />
              <div className="text-xs text-slate-500 mt-1">{feedback.length} responses</div>
            </div>
          </div>

          {/* Distribution */}
          <div className="md:col-span-2 bg-white rounded-xl border border-slate-200 p-5">
            <h3 className="text-sm font-semibold text-slate-800 mb-3">Rating Distribution</h3>
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
                      style={{ width: feedback.length ? `${(count / feedback.length) * 100}%` : '0%' }}
                    />
                  </div>
                  <span className="text-xs text-slate-500 w-6 text-right">{count}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Feedback table */}
        <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
          <div className="px-5 py-4 border-b border-slate-100">
            <h3 className="font-semibold text-slate-800 text-sm">Recent Feedback</h3>
          </div>
          <div className="divide-y divide-slate-100">
            {feedback.map(f => (
              <div key={f.id} className="px-5 py-4 hover:bg-slate-50/50 transition-colors">
                <div className="flex items-start justify-between gap-3">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-full bg-teal-100 text-teal-700 flex items-center justify-center text-sm font-semibold shrink-0">
                      {f.patient.charAt(0)}
                    </div>
                    <div>
                      <div className="font-medium text-slate-800 text-sm">{f.patient}</div>
                      <div className="text-xs text-slate-500">{f.date}</div>
                    </div>
                  </div>
                  <Stars rating={f.rating} />
                </div>
                {f.comment && (
                  <p className="text-sm text-slate-600 mt-2 ml-11 leading-relaxed">{f.comment}</p>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </Layout>
  );
}
