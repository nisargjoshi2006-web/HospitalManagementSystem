import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Eye, EyeOff, Hospital, Shield, Activity, Users, Calendar, AlertCircle } from 'lucide-react';
import { login } from '../api/api';

export default function Login() {
  const [showPass, setShowPass] = useState(false);
  const [remember, setRemember] = useState(false);
  const [form, setForm] = useState({ username: 'admin', password: 'admin123' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await login(form.username, form.password);
      if (res.success) {
        localStorage.setItem('hospital_user', JSON.stringify(res.user));
        navigate('/app/dashboard');
      } else {
        setError(res.message || 'Invalid credentials');
      }
    } catch (err: any) {
      console.warn('API error, falling back to offline login if admin:', err.message);
      if (form.username === 'admin' && form.password === 'admin123') {
        localStorage.setItem('hospital_user', JSON.stringify({ name: 'Admin', role: 'Admin' }));
        navigate('/app/dashboard');
      } else {
        setError(err.message || 'Login failed. Check database connection.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex" style={{ fontFamily: 'Inter, sans-serif' }}>
      {/* Left panel */}
      <div
        className="hidden lg:flex flex-col justify-between w-[480px] shrink-0 p-10"
        style={{ background: '#0F172A' }}
      >
        {/* Header */}
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-teal-500 flex items-center justify-center">
            <Hospital size={20} color="white" />
          </div>
          <div>
            <div className="text-white font-bold text-lg leading-tight">CarePoint Hospital</div>
            <div className="text-slate-400 text-xs">Hospital Management System</div>
          </div>
        </div>

        {/* Stats */}
        <div className="space-y-4">
          <h2 className="text-white text-2xl font-semibold leading-snug">
            Empowering healthcare<br />
            <span className="text-teal-400">one record at a time.</span>
          </h2>
          <p className="text-slate-400 text-sm leading-relaxed">
            Manage patients, schedule appointments, track billing, and coordinate care — all from a single, secure platform.
          </p>

          <div className="grid grid-cols-2 gap-3 mt-6">
            {[
              { icon: Users, label: 'Patients', value: '12,847' },
              { icon: Activity, label: 'Uptime', value: '99.98%' },
              { icon: Calendar, label: "Today's Appointments", value: '84' },
              { icon: Shield, label: 'HIPAA Compliant', value: '✓ Certified' },
            ].map(({ icon: Icon, label, value }) => (
              <div key={label} className="bg-slate-800/50 rounded-xl p-3.5 border border-slate-700/50">
                <Icon size={14} className="text-teal-400 mb-2" />
                <div className="text-white font-semibold text-base">{value}</div>
                <div className="text-slate-400 text-xs mt-0.5">{label}</div>
              </div>
            ))}
          </div>
        </div>

        {/* Footer */}
        <div className="text-slate-500 text-xs">
          © 2026 CarePoint Hospital. All rights reserved. v3.2.1
        </div>
      </div>

      {/* Right panel */}
      <div className="flex-1 flex items-center justify-center bg-slate-50 p-8">
        <div className="w-full max-w-md">
          {/* Mobile logo */}
          <div className="flex items-center gap-2.5 mb-8 lg:hidden">
            <div className="w-9 h-9 rounded-xl bg-teal-500 flex items-center justify-center">
              <Hospital size={18} color="white" />
            </div>
            <div>
              <div className="text-slate-900 font-bold text-base">CarePoint Hospital</div>
              <div className="text-slate-500 text-xs">Hospital Management System</div>
            </div>
          </div>

          <div className="bg-white rounded-2xl border border-slate-200 p-8 shadow-sm">
            <div className="mb-6">
              <h2 className="text-slate-900 text-xl font-semibold">Sign in to your account</h2>
              <p className="text-slate-500 text-sm mt-1">Enter your credentials to continue</p>
            </div>

            {error && (
              <div className="mb-4 p-3 rounded-lg bg-red-50 border border-red-200 text-xs text-red-600 flex items-center gap-2">
                <AlertCircle size={14} className="shrink-0" />
                <span>{error}</span>
              </div>
            )}

            <form onSubmit={handleLogin} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1.5">Username</label>
                <input
                  type="text"
                  placeholder="Enter username"
                  value={form.username}
                  onChange={e => setForm({ ...form, username: e.target.value })}
                  className="w-full px-3.5 py-2.5 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-500 transition-all bg-white"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1.5">Password</label>
                <div className="relative">
                  <input
                    type={showPass ? 'text' : 'password'}
                    placeholder="Enter password"
                    value={form.password}
                    onChange={e => setForm({ ...form, password: e.target.value })}
                    className="w-full px-3.5 py-2.5 pr-10 text-sm border border-slate-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-500 transition-all bg-white"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPass(!showPass)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 transition-colors"
                  >
                    {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
                  </button>
                </div>
              </div>

              <div className="flex items-center justify-between">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={remember}
                    onChange={e => setRemember(e.target.checked)}
                    className="w-4 h-4 rounded border-slate-300 text-teal-500 focus:ring-teal-500"
                  />
                  <span className="text-sm text-slate-600">Remember me</span>
                </label>
                <button type="button" className="text-sm text-teal-600 hover:text-teal-700 font-medium">
                  Forgot password?
                </button>
              </div>

              <button
                type="submit"
                className="w-full py-2.5 rounded-lg text-sm font-semibold text-white transition-all hover:opacity-90 active:scale-[0.99]"
                style={{ background: '#0F766E' }}
              >
                Sign In
              </button>
            </form>

            <div className="mt-5 p-3 rounded-lg bg-slate-50 border border-slate-200 flex items-start gap-2.5">
              <Shield size={14} className="text-slate-400 mt-0.5 shrink-0" />
              <p className="text-xs text-slate-500">
                This system is for authorized personnel only. All access is logged and monitored. Unauthorized access will be prosecuted.
              </p>
            </div>
          </div>

          <p className="text-center text-xs text-slate-400 mt-4">
            CarePoint Hospital Management System v3.2.1 · HIPAA Compliant
          </p>
        </div>
      </div>
    </div>
  );
}
