import { BriefcaseBusiness, LayoutDashboard, LogOut, Menu, UserRound, X } from 'lucide-react';
import { useState } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { apiClient } from '../../services/apiClient';

const navigation = {
  student: [['Dashboard', '/student/dashboard'], ['Browse internships', '/student/internships'], ['My applications', '/student/applications'], ['My profile', '/student/profile']],
  company: [['Dashboard', '/company/dashboard'], ['My internships', '/company/internships'], ['Post internship', '/company/internships/new'], ['Company profile', '/company/profile']],
  admin: [['Dashboard', '/admin/dashboard'], ['Students', '/admin/students'], ['Companies', '/admin/companies'], ['Internships', '/admin/internships'], ['Applications', '/admin/applications'], ['Audit logs', '/admin/audit-logs']],
};

export function DashboardLayout({ role }) {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
  const { user, clearSession } = useAuth();
  const links = navigation[role];
  const handleLogout = async () => {
    try {
      await apiClient.post('/auth/logout');
    } catch {
      // A stateless JWT logout can still complete locally if the API is unavailable.
    } finally {
      clearSession();
      navigate('/login', { replace: true });
    }
  };
  return <div className="dashboard-shell">
    <aside className={`sidebar ${open ? 'sidebar--open' : ''}`} aria-label={`${role} navigation`}>
      <div className="sidebar-brand"><BriefcaseBusiness size={22} />InternHub<button className="sidebar-close" onClick={() => setOpen(false)} aria-label="Close navigation"><X size={20} /></button></div>
      <nav>{links.map(([label, path]) => <NavLink key={path} to={path} onClick={() => setOpen(false)}>{label}</NavLink>)}</nav>
      <button className="sidebar-logout" onClick={handleLogout}><LogOut size={17} />Sign out</button>
    </aside>
    <div className="dashboard-main"><header className="dashboard-topbar"><button className="menu-toggle" aria-label="Open navigation" onClick={() => setOpen(true)}><Menu /></button><span className="topbar-context"><LayoutDashboard size={17} />{role} workspace</span><span className="user-chip"><UserRound size={17} />{user?.name || 'Account'}</span></header><main className="dashboard-content"><Outlet /></main></div>
  </div>;
}
