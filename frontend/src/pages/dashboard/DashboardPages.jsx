import { useCallback, useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { EmptyState, ErrorState, LoadingState } from '../../components/common/Feedback';
import { Button } from '../../components/common/Button';
import { apiClient } from '../../services/apiClient';

export function PendingDashboardPage({ role }) { if (role === 'admin') return <AdminDashboard />; return <RoleDashboard role={role} />; }
export function PendingWorkspacePage({ role }) { const location = useLocation(); const title = location.pathname.split('/').filter(Boolean).slice(1).map((part) => part.replaceAll('-', ' ')).join(' · ') || 'Workspace'; return <Page title={title} description={`This ${role} workflow is not available yet.`} />; }
function Page({ title, description }) { return <><header className="page-header"><span className="eyebrow">Secure workspace</span><h1>{title}</h1><p>{description}</p></header><EmptyState title="Ready for secure data" message="This workflow has not been implemented yet." /></>; }

function AdminDashboard() {
  const [metrics, setMetrics] = useState(null); const [error, setError] = useState(null);
  const load = useCallback(async () => { setError(null); try { setMetrics((await apiClient.get('/admin/dashboard')).data); } catch (requestError) { setError(requestError.message); } }, []);
  useEffect(() => { load(); }, [load]);
  return <><header className="page-header"><span className="eyebrow">Secure workspace</span><h1>Administration workspace</h1><p>Monitor platform records and review protected administrative data.</p></header>{error && <ErrorState message={error} onRetry={load} />}{!error && !metrics && <LoadingState label="Loading platform metrics..." />}{metrics && <div className="feature-grid" aria-label="Platform metrics">{[['Students', metrics.totalStudents], ['Companies', metrics.totalCompanies], ['Internships', metrics.totalInternships], ['Applications', metrics.totalApplications]].map(([label, value]) => <article className="feature-card" key={label}><span className="eyebrow">{label}</span><h2>{value}</h2><p>Current database total</p></article>)}</div>}</>;
}

function RoleDashboard({ role }) {
  const [data, setData] = useState(null); const [error, setError] = useState(null); const path = role === 'student' ? '/students/me/dashboard' : '/companies/me/dashboard';
  const load = useCallback(async () => { setError(null); try { setData((await apiClient.get(path)).data); } catch (requestError) { setError(requestError.message); } }, [path]);
  useEffect(() => { load(); }, [load]);
  if (error) return <ErrorState message={error} onRetry={load} />;
  if (!data) return <LoadingState label="Loading dashboard..." />;
  const cards = role === 'student' ? [['Applications', data.totalApplications], ['Open internships', data.totalInternships]] : [['Internships', data.totalInternships], ['Applicants', data.totalApplicants]];
  return <><header className="page-header"><span className="eyebrow">Secure workspace</span><h1>Your {role} workspace</h1><p>Live activity from your account.</p></header><div className="feature-grid">{cards.map(([label, value]) => <article className="feature-card" key={label}><span className="eyebrow">{label}</span><h2>{value}</h2><p>Current database total</p></article>)}</div><div className="hero-actions">{role === 'student' ? <><Link to="/student/internships"><Button>Browse internships</Button></Link><Link to="/student/applications"><Button variant="secondary">View applications</Button></Link><Link to="/student/profile"><Button variant="secondary">Update profile</Button></Link></> : <Link to="/company/internships"><Button>Manage internships</Button></Link>}</div></>;
}
