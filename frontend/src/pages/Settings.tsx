import Layout from '../components/Layout';
import { Bell, Shield, Database, Monitor, Users } from 'lucide-react';

export default function Settings() {
  return (
    <Layout title="Settings" subtitle="System configuration and preferences">
      <div className="p-6 max-w-3xl space-y-5">
        {[
          {
            icon: Monitor, title: 'General', settings: [
              { label: 'Hospital Name', type: 'text', value: 'CarePoint Hospital' },
              { label: 'Time Zone', type: 'text', value: 'UTC-5 (Eastern)' },
              { label: 'Date Format', type: 'select', value: 'YYYY-MM-DD' },
            ]
          },
          {
            icon: Bell, title: 'Notifications', settings: [
              { label: 'Emergency Alerts', type: 'toggle', value: true },
              { label: 'Appointment Reminders', type: 'toggle', value: true },
              { label: 'Lab Result Updates', type: 'toggle', value: false },
              { label: 'Billing Notifications', type: 'toggle', value: true },
            ]
          },
          {
            icon: Shield, title: 'Security', settings: [
              { label: 'Two-Factor Authentication', type: 'toggle', value: true },
              { label: 'Session Timeout (minutes)', type: 'text', value: '30' },
              { label: 'Audit Log Retention (days)', type: 'text', value: '365' },
            ]
          },
          {
            icon: Database, title: 'Database', settings: [
              { label: 'Auto Backup', type: 'toggle', value: true },
              { label: 'Backup Frequency', type: 'select', value: 'Daily' },
              { label: 'Last Backup', type: 'readonly', value: '2026-09-16 at 11:00 PM' },
            ]
          },
        ].map(({ icon: Icon, title, settings }) => (
          <div key={title} className="bg-white rounded-xl border border-slate-200">
            <div className="flex items-center gap-2.5 px-5 py-4 border-b border-slate-100">
              <Icon size={15} className="text-teal-600" />
              <h3 className="font-semibold text-slate-800 text-sm">{title}</h3>
            </div>
            <div className="divide-y divide-slate-100">
              {settings.map(s => (
                <div key={s.label} className="flex items-center justify-between px-5 py-3.5">
                  <label className="text-sm text-slate-700">{s.label}</label>
                  {s.type === 'toggle' ? (
                    <div className={`w-10 h-5.5 rounded-full relative cursor-pointer transition-colors ${s.value ? 'bg-teal-500' : 'bg-slate-300'}`}
                      style={{ height: '22px', width: '42px' }}>
                      <div className={`absolute top-0.5 w-4.5 h-4.5 bg-white rounded-full shadow transition-transform ${s.value ? 'translate-x-5' : 'translate-x-0.5'}`}
                        style={{ width: '18px', height: '18px', transform: s.value ? 'translateX(22px)' : 'translateX(2px)' }} />
                    </div>
                  ) : s.type === 'readonly' ? (
                    <span className="text-sm text-slate-500 font-mono">{s.value as string}</span>
                  ) : (
                    <input
                      defaultValue={s.value as string}
                      className="px-3 py-1.5 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 w-48 text-right"
                    />
                  )}
                </div>
              ))}
            </div>
          </div>
        ))}

        <div className="flex justify-end gap-2">
          <button className="px-4 py-2 text-sm border border-slate-200 rounded-lg hover:bg-slate-50 text-slate-700">Reset to Defaults</button>
          <button className="px-4 py-2 text-sm font-medium text-white rounded-lg" style={{ background: '#0F766E' }}>Save Changes</button>
        </div>
      </div>
    </Layout>
  );
}
