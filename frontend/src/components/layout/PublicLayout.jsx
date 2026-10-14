import { BriefcaseBusiness, Menu, X } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom';
import { Button } from '../common/Button';
import { useAuth } from '../../context/AuthContext';

const pageMetadata = { '/': ['InternHub — Internship Management', 'Discover internships, connect with companies, and manage your career journey.'], '/internships': ['Explore internships — InternHub', 'Browse open internship opportunities from verified companies.'], '/login': ['Sign in — InternHub', 'Sign in securely to your InternHub workspace.'], '/register/student': ['Create a student account — InternHub', 'Create an InternHub student account to discover and apply for internships.'], '/register/company': ['Register your company — InternHub', 'Create a company account to publish internship opportunities and review applicants.'] };

export function PublicLayout() {
  const [open, setOpen] = useState(false);
  const { user, clearSession } = useAuth();
  const location = useLocation();
  const dashboardPath = user?.role === 'STUDENT' ? '/student/dashboard' : user?.role === 'COMPANY' ? '/company/dashboard' : '/admin/dashboard';
  const closeMenu = () => setOpen(false);
  const metadata = pageMetadata[location.pathname] || pageMetadata['/'];
  useEffect(() => { document.title = metadata[0]; document.querySelector('meta[name="description"]')?.setAttribute('content', metadata[1]); }, [metadata]);
  return <div className="public-shell">
    <header className="public-header"><nav className="public-nav" aria-label="Main navigation">
      <Link className="brand" to="/" onClick={closeMenu}><BriefcaseBusiness size={22} />InternHub</Link>
      <button className="menu-toggle" aria-label={open ? 'Close navigation menu' : 'Open navigation menu'} aria-expanded={open} onClick={() => setOpen(!open)}>{open ? <X /> : <Menu />}</button>
      <div className={`public-links ${open ? 'public-links--open' : ''}`}>
        <NavLink to="/internships" onClick={closeMenu}>Explore internships</NavLink>
        <NavLink to="/register/company" onClick={closeMenu}>For companies</NavLink>
        {user ? <><Link className="nav-login" to={dashboardPath} onClick={closeMenu}>{user.name || 'Dashboard'}</Link><button className="app-button app-button--secondary" onClick={() => { clearSession(); closeMenu(); }}>Sign out</button></> : <><Link className="nav-login" to="/login" onClick={closeMenu}>Sign in</Link><Link to="/register/student" onClick={closeMenu}><Button>Get started</Button></Link></>}
      </div>
    </nav></header>
    <main><Outlet /></main>
    <footer className="public-footer"><span>© {new Date().getFullYear()} InternHub</span><Link to="/internships">Explore internships</Link>{user ? <Link to={dashboardPath}>Your dashboard</Link> : <Link to="/login">Sign in</Link>}</footer>
  </div>;
}
