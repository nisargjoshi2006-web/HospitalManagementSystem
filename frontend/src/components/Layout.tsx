import { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard, Users, UserRound, Calendar, CalendarDays, FileText,
  CreditCard, AlertTriangle, FlaskConical, Bed, MessageSquare, BarChart3,
  ScrollText, Settings, LogOut, Bell, Search, Activity, Menu, X, ChevronRight,
  Hospital
} from 'lucide-react';

const navItems = [
  { label: 'Dashboard', icon: LayoutDashboard, path: '/app/dashboard' },
  { label: 'Patients', icon: Users, path: '/app/patients' },
  { label: 'Doctors', icon: UserRound, path: '/app/doctors' },
  { label: 'Doctor Schedule', icon: CalendarDays, path: '/app/schedule' },
  { label: 'Appointments', icon: Calendar, path: '/app/appointments' },
  { label: 'Prescriptions', icon: FileText, path: '/app/prescriptions' },
  { label: 'Billing', icon: CreditCard, path: '/app/billing' },
  { label: 'Emergency', icon: AlertTriangle, path: '/app/emergency' },
  { label: 'Lab Tests', icon: FlaskConical, path: '/app/lab-tests' },
  { label: 'Bed Allocation', icon: Bed, path: '/app/beds' },
  { label: 'Feedback', icon: MessageSquare, path: '/app/feedback' },
  { label: 'Reports', icon: BarChart3, path: '/app/reports' },
  { label: 'Audit Logs', icon: ScrollText, path: '/app/audit' },
  { label: 'Settings', icon: Settings, path: '/app/settings' },
];

interface LayoutProps {
  children: React.ReactNode;
  title: string;
  subtitle?: string;
}

export default function Layout({ children, title, subtitle }: LayoutProps) {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();

  const handleLogout = () => navigate('/login');

  return (
    <div className="flex h-screen bg-slate-50 overflow-hidden">
      {/* Sidebar */}
      <aside
        className="flex flex-col transition-all duration-300 shrink-0"
        style={{
          width: sidebarOpen ? '220px' : '64px',
          background: '#0F172A',
        }}
      >
        {/* Logo */}
        <div className="flex items-center gap-3 px-4 py-5 border-b border-slate-700/50">
          <div className="flex items-center justify-center w-8 h-8 rounded-lg bg-teal-500 shrink-0">
            <Hospital size={16} color="white" />
          </div>
          {sidebarOpen && (
            <div className="min-w-0">
              <div className="text-white font-semibold text-sm leading-tight truncate">CarePoint</div>
              <div className="text-slate-400 text-xs truncate">Hospital</div>
            </div>
          )}
        </div>

        {/* Nav */}
        <nav className="flex-1 overflow-y-auto py-3 space-y-0.5 px-2">
          {navItems.map(({ label, icon: Icon, path }) => (
            <NavLink
              key={path}
              to={path}
              className={({ isActive }) =>
                `flex items-center gap-3 px-2.5 py-2 rounded-lg text-sm transition-all duration-150 group ${
                  isActive
                    ? 'bg-teal-600/20 text-teal-400 font-medium'
                    : 'text-slate-400 hover:bg-slate-700/50 hover:text-white'
                }`
              }
              title={!sidebarOpen ? label : undefined}
            >
              {({ isActive }) => (
                <>
                  <Icon size={16} className="shrink-0" color={isActive ? '#2DD4BF' : undefined} />
                  {sidebarOpen && <span className="truncate">{label}</span>}
                  {sidebarOpen && isActive && <ChevronRight size={12} className="ml-auto opacity-60" />}
                </>
              )}
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div className="p-2 border-t border-slate-700/50">
          <button
            onClick={handleLogout}
            className="flex items-center gap-3 w-full px-2.5 py-2 rounded-lg text-sm text-slate-400 hover:bg-red-500/10 hover:text-red-400 transition-colors"
            title={!sidebarOpen ? 'Logout' : undefined}
          >
            <LogOut size={16} className="shrink-0" />
            {sidebarOpen && <span>Logout</span>}
          </button>
        </div>
      </aside>

      {/* Main */}
      <div className="flex flex-col flex-1 min-w-0 overflow-hidden">
        {/* Top header */}
        <header className="flex items-center gap-4 px-6 py-3 bg-white border-b border-slate-200 shrink-0">
          <button
            onClick={() => setSidebarOpen(!sidebarOpen)}
            className="p-1.5 rounded-lg hover:bg-slate-100 text-slate-500 transition-colors"
          >
            {sidebarOpen ? <X size={18} /> : <Menu size={18} />}
          </button>

          <div className="flex-1 min-w-0">
            <h1 className="text-base font-semibold text-slate-900 leading-tight">{title}</h1>
            {subtitle && <p className="text-xs text-slate-500 truncate">{subtitle}</p>}
          </div>

          {/* Search */}
          <div className="relative hidden md:block">
            <Search size={14} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              placeholder="Search patients, doctors..."
              value={searchQuery}
              onChange={e => setSearchQuery(e.target.value)}
              className="pl-8 pr-4 py-1.5 text-sm bg-slate-50 border border-slate-200 rounded-lg w-64 focus:outline-none focus:ring-2 focus:ring-teal-500/20 focus:border-teal-400 transition-all"
            />
          </div>

          {/* DB Health */}
          <div className="flex items-center gap-1.5 text-xs text-green-600 bg-green-50 px-2.5 py-1 rounded-full border border-green-200 hidden sm:flex">
            <Activity size={11} className="animate-pulse" />
            <span className="font-medium">System Healthy</span>
          </div>

          {/* Notifications */}
          <button className="relative p-1.5 rounded-lg hover:bg-slate-100 text-slate-500 transition-colors">
            <Bell size={18} />
            <span className="absolute top-0.5 right-0.5 w-2 h-2 rounded-full bg-red-500 border border-white" />
          </button>

          {/* User */}
          <div className="flex items-center gap-2.5 pl-3 border-l border-slate-200">
            <div className="w-8 h-8 rounded-full bg-teal-600 flex items-center justify-center text-white text-sm font-semibold shrink-0">
              A
            </div>
            <div className="hidden sm:block leading-tight">
              <div className="text-sm font-medium text-slate-800">Admin User</div>
              <div className="text-xs text-slate-500">Administrator</div>
            </div>
          </div>
        </header>

        {/* Page content */}
        <main className="flex-1 overflow-y-auto">
          {children}
        </main>
      </div>
    </div>
  );
}
