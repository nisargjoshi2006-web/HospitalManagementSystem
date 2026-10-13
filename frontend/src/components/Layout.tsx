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
  const [showNotifications, setShowNotifications] = useState(false);
  const [unreadCount, setUnreadCount] = useState(3);
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
          <div className="relative">
            <button
              onClick={() => setShowNotifications(!showNotifications)}
              className="relative p-1.5 rounded-lg hover:bg-slate-100 text-slate-500 transition-colors"
              title="Hospital Alerts & Notifications"
            >
              <Bell size={18} />
              {unreadCount > 0 && (
                <span className="absolute -top-0.5 -right-0.5 w-4 h-4 rounded-full bg-red-500 text-white text-[10px] font-bold flex items-center justify-center border-2 border-white">
                  {unreadCount}
                </span>
              )}
            </button>

            {/* Notification Popover Dropdown */}
            {showNotifications && (
              <>
                <div
                  className="fixed inset-0 z-40"
                  onClick={() => setShowNotifications(false)}
                />
                <div className="absolute right-0 mt-2 w-80 sm:w-96 bg-white rounded-xl shadow-xl border border-slate-200 z-50 overflow-hidden animate-in fade-in slide-in-from-top-2 duration-150">
                  <div className="px-4 py-3 bg-slate-50 border-b border-slate-100 flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      <span className="text-sm font-semibold text-slate-800">Hospital Notifications</span>
                      {unreadCount > 0 && (
                        <span className="text-[11px] bg-red-100 text-red-700 px-1.5 py-0.2 rounded-full font-bold">
                          {unreadCount} New
                        </span>
                      )}
                    </div>
                    {unreadCount > 0 && (
                      <button
                        onClick={() => setUnreadCount(0)}
                        className="text-xs text-teal-700 hover:text-teal-800 font-medium"
                      >
                        Mark all as read
                      </button>
                    )}
                  </div>

                  <div className="divide-y divide-slate-100 max-h-80 overflow-y-auto">
                    <div
                      onClick={() => {
                        navigate('/app/emergency');
                        setShowNotifications(false);
                        setUnreadCount(Math.max(0, unreadCount - 1));
                      }}
                      className="p-3.5 hover:bg-slate-50 transition-colors cursor-pointer flex gap-3 items-start"
                    >
                      <div className="w-8 h-8 rounded-full bg-red-100 text-red-600 flex items-center justify-center shrink-0 mt-0.5">
                        <AlertTriangle size={15} />
                      </div>
                      <div className="min-w-0 flex-1">
                        <p className="text-xs font-semibold text-slate-800">Critical ER Arrival</p>
                        <p className="text-xs text-slate-600 mt-0.5">Acute Myocardial Infarction — Allocated to ICU-101</p>
                        <span className="text-[10px] text-slate-400 mt-1 block">5 minutes ago</span>
                      </div>
                    </div>

                    <div
                      onClick={() => {
                        navigate('/app/appointments');
                        setShowNotifications(false);
                        setUnreadCount(Math.max(0, unreadCount - 1));
                      }}
                      className="p-3.5 hover:bg-slate-50 transition-colors cursor-pointer flex gap-3 items-start"
                    >
                      <div className="w-8 h-8 rounded-full bg-blue-100 text-blue-600 flex items-center justify-center shrink-0 mt-0.5">
                        <Calendar size={15} />
                      </div>
                      <div className="min-w-0 flex-1">
                        <p className="text-xs font-semibold text-slate-800">Today's OPD Consultations</p>
                        <p className="text-xs text-slate-600 mt-0.5">5 patient appointments scheduled for today</p>
                        <span className="text-[10px] text-slate-400 mt-1 block">25 minutes ago</span>
                      </div>
                    </div>

                    <div
                      onClick={() => {
                        navigate('/app/lab-tests');
                        setShowNotifications(false);
                        setUnreadCount(Math.max(0, unreadCount - 1));
                      }}
                      className="p-3.5 hover:bg-slate-50 transition-colors cursor-pointer flex gap-3 items-start"
                    >
                      <div className="w-8 h-8 rounded-full bg-teal-100 text-teal-600 flex items-center justify-center shrink-0 mt-0.5">
                        <FlaskConical size={15} />
                      </div>
                      <div className="min-w-0 flex-1">
                        <p className="text-xs font-semibold text-slate-800">Diagnostic Tests Ready</p>
                        <p className="text-xs text-slate-600 mt-0.5">Cardiac Troponin-I and CBC report uploaded</p>
                        <span className="text-[10px] text-slate-400 mt-1 block">1 hour ago</span>
                      </div>
                    </div>

                    <div
                      onClick={() => {
                        navigate('/app/beds');
                        setShowNotifications(false);
                      }}
                      className="p-3.5 hover:bg-slate-50 transition-colors cursor-pointer flex gap-3 items-start"
                    >
                      <div className="w-8 h-8 rounded-full bg-purple-100 text-purple-600 flex items-center justify-center shrink-0 mt-0.5">
                        <Bed size={15} />
                      </div>
                      <div className="min-w-0 flex-1">
                        <p className="text-xs font-semibold text-slate-800">Bed Ward Utilization</p>
                        <p className="text-xs text-slate-600 mt-0.5">6 inpatient beds occupied across ICU & Private wards</p>
                        <span className="text-[10px] text-slate-400 mt-1 block">2 hours ago</span>
                      </div>
                    </div>
                  </div>

                  <div className="p-2.5 bg-slate-50 border-t border-slate-100 text-center">
                    <span className="text-[11px] text-slate-500 font-medium">⚡ Connected to Live Hospital MySQL Database</span>
                  </div>
                </div>
              </>
            )}
          </div>

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
